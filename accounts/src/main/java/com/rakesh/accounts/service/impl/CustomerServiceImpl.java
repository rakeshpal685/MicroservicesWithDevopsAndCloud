package com.rakesh.accounts.service.impl;

import com.rakesh.accounts.dto.AccountsDto;
import com.rakesh.accounts.dto.CardsDto;
import com.rakesh.accounts.dto.CustomerDetailsDto;
import com.rakesh.accounts.dto.LoansDto;
import com.rakesh.accounts.entity.Accounts;
import com.rakesh.accounts.entity.Customer;
import com.rakesh.accounts.exception.ResourceNotFoundException;
import com.rakesh.accounts.mapper.AccountsMapper;
import com.rakesh.accounts.mapper.CustomerMapper;
import com.rakesh.accounts.repository.AccountsRepository;
import com.rakesh.accounts.repository.CustomerRepository;
import com.rakesh.accounts.service.CustomerServiceInterface;
import com.rakesh.accounts.service.feignClient.CardsFeignClient;
import com.rakesh.accounts.service.feignClient.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerServiceInterface {

  private CardsFeignClient cardsFeignClient;
  private AccountsRepository accountsRepository;
  private CustomerRepository customerRepository;
  private LoansFeignClient loansFeignClient;

  /**
   * @param mobileNumber - Input Mobile Number
   * @param correlationId - This id is generated from the gateway server,and coming here by the
   *     CustomerController which can be used to trace the logs
   * @return Customer Details based on the given Mobile Number
   */
  @Override
  public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
    Customer customer =
        customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobilenumber", mobileNumber));

    Accounts accounts =
        accountsRepository
            .findByCustomerId(customer.getCustomerId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account", "customerId", customer.getCustomerId().toString()));

    CustomerDetailsDto customerDetailsDto =
        CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
    customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

    ResponseEntity<LoansDto> loansDtoResponseEntity =
        loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
    if (null != loansDtoResponseEntity) {
      customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
    }

    ResponseEntity<CardsDto> cardsDtoResponseEntity =
        cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
    if (null != cardsDtoResponseEntity) {
      customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
    }
    return customerDetailsDto;
  }
}
