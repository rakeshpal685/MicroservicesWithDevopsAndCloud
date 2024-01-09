package com.rakesh.accounts.service.impl;

import com.rakesh.accounts.constants.AccountsConstants;
import com.rakesh.accounts.dto.AccountsDto;
import com.rakesh.accounts.dto.CustomerDto;
import com.rakesh.accounts.entity.Accounts;
import com.rakesh.accounts.entity.Customer;
import com.rakesh.accounts.exception.CustomerAlreadyExistsException;
import com.rakesh.accounts.exception.ResourceNotFoundException;
import com.rakesh.accounts.mapper.AccountsMapper;
import com.rakesh.accounts.mapper.CustomerMapper;
import com.rakesh.accounts.repository.AccountRepository;
import com.rakesh.accounts.repository.CustomerRepository;
import com.rakesh.accounts.service.AccountServiceInterf;
import java.util.Optional;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountServiceInterf {

  private AccountRepository accountRepository;
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
    accountRepository.save(createNewAccount(savedCustomer));
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

    CustomerDto customerDto = CustomerMapper.mapTeCustomerDto(customer, new CustomerDto());
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
}
