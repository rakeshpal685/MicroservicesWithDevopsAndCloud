package com.rakesh.accounts.service.impl;

import com.rakesh.accounts.constants.AccountsConstants;
import com.rakesh.accounts.dto.AccountsDto;
import com.rakesh.accounts.dto.AccountsMsgDto;
import com.rakesh.accounts.dto.CustomerDto;
import com.rakesh.accounts.entity.Accounts;
import com.rakesh.accounts.entity.Customer;
import com.rakesh.accounts.exception.CustomerAlreadyExistsException;
import com.rakesh.accounts.exception.ResourceNotFoundException;
import com.rakesh.accounts.mapper.AccountsMapper;
import com.rakesh.accounts.mapper.CustomerMapper;
import com.rakesh.accounts.repository.AccountsRepository;
import com.rakesh.accounts.repository.CustomerRepository;
import com.rakesh.accounts.service.AccountServiceInterf;
import java.util.Optional;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountServiceInterf {

  //  This is for RabbitMQ streams
  private final StreamBridge streamBridge;
  private AccountsRepository accountRepository;
  private CustomerRepository customerRepository;

  /**
   * @param customerDto - CustomerDto Object
   */
  @Override
  public void createAccount(CustomerDto customerDto) {

    // Here all the data from customerDto will be transferred to customer object
    Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

    Optional<Customer> optionalCustomer =
        customerRepository.findByMobileNumber(customerDto.getMobileNumber());
    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException(
          "Customer already registered with the given mobile number - "
              + customerDto.getMobileNumber());
    }
    Customer savedCustomer = customerRepository.save(customer);
    Accounts savedAccount = accountRepository.save(createNewAccount(savedCustomer));
    sendCommunication(savedAccount, savedCustomer);
  }

  @Override
  public CustomerDto fetchAccount(String mobileNumber) {
    Customer customer =
        customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobilenumber", mobileNumber));

    Accounts accounts =
        accountRepository
            .findByCustomerId(customer.getCustomerId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account", "customerId", customer.getCustomerId().toString()));

    CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
    customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

    return customerDto;
  }

  /**
   * @param customerDto - CustomerDto Object
   * @return boolean indicating if the update of Account details is successful or not
   */
  @Override
  public boolean updateAccount(CustomerDto customerDto) {
    boolean isUpdated = false;
    AccountsDto accountsDto = customerDto.getAccountsDto();
    if (accountsDto != null) {
      Accounts accounts =
          accountRepository
              .findById(accountsDto.getAccountNumber())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Account", "AccountNumber", accountsDto.getAccountNumber().toString()));

      AccountsMapper.mapToAccounts(accountsDto, accounts);
      accounts = accountRepository.save(accounts);

      Long customerId = accounts.getCustomerId();
      Customer customer =
          customerRepository
              .findById(customerId)
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Customer", "CustomerID", customerId.toString()));

      CustomerMapper.mapToCustomer(customerDto, customer);
      customerRepository.save(customer);
      isUpdated = true;
    }
    return isUpdated;
  }

  @Override
  public boolean deleteAccount(String mobileNumber) {
    Customer customer =
        customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

    accountRepository.deleteByCustomerId(customer.getCustomerId());
    customerRepository.deleteById(customer.getCustomerId());
    return true;
  }

  /**
   * @param customer - Customer Object
   * @return the new account details
   */
  private Accounts createNewAccount(Customer customer) {
    Accounts newAccount = new Accounts();
    newAccount.setCustomerId(customer.getCustomerId());
    long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

    newAccount.setAccountNumber(randomAccNumber);
    newAccount.setAccountType(AccountsConstants.SAVINGS);
    newAccount.setBranchAddress(AccountsConstants.ADDRESS);
    return newAccount;
  }

  // This method is created to send message to RabbitMQ
  private void sendCommunication(Accounts account, Customer customer) {
    var accountsMsgDto =
        new AccountsMsgDto(
            account.getAccountNumber(),
            customer.getName(),
            customer.getEmail(),
            customer.getMobileNumber());
    log.info("Sending Communication request for the details: {}", accountsMsgDto);
    var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
    log.info("Is the Communication request successfully triggered ? : {}", result);
  }

  /**
   * @param accountNumber - Long
   * @return boolean indicating if the update of communication status is successful or not This
   *     method is created to receive message from RabbitMQ and update the DB table
   */
  @Override
  public boolean updateCommunicationStatus(Long accountNumber) {
    boolean isUpdated = false;
    if (accountNumber != null) {
      Accounts accounts =
          accountRepository
              .findById(accountNumber)
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Account", "AccountNumber", accountNumber.toString()));
      accounts.setCommunicationSw(true);
      accountRepository.save(accounts);
      isUpdated = true;
    }
    return isUpdated;
  }
}
