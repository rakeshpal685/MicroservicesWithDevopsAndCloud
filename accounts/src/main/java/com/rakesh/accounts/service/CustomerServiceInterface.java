package com.rakesh.accounts.service;

import com.rakesh.accounts.dto.CustomerDetailsDto;

public interface CustomerServiceInterface {

  /**
   * @param mobileNumber- Input Mobile Number
   * @return Customer Details based on a given mobileNumber
   */
  CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
