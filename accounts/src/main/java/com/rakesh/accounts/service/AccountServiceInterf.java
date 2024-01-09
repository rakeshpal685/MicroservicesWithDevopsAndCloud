package com.rakesh.accounts.service;

import com.rakesh.accounts.dto.CustomerDto;

public interface AccountServiceInterf {

  /**
   * @param customerDto - CustomerDto Object
   */
  void createAccount(CustomerDto customerDto);

  CustomerDto fetchAccount(String mobileNumber);

  boolean updateAccount(CustomerDto customerDto);

  boolean deleteAccount(String mobileNumber);
}
