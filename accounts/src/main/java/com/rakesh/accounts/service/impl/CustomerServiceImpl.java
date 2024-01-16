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
import com.rakesh.accounts.service.client.CardsFeignClient;
import com.rakesh.accounts.service.client.LoansFeignClient;
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
   * @param mobileNumber- Input Mobile Number
   * @return Customer Details based on the given Mobile Number
   */
  @Override
  public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
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
        loansFeignClient.fetchLoanDetails(mobileNumber);
    customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

    ResponseEntity<CardsDto> cardsDtoResponseEntity =
        cardsFeignClient.fetchCardDetails(mobileNumber);
    customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

    return customerDetailsDto;
  }
}
