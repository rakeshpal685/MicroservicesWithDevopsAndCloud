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

  /**
   *
   * @param accountNumber - Long
   * @return boolean indicating if the update of communication status is successful or not
   * This is for RabbitMQ streams
   */
  boolean updateCommunicationStatus(Long accountNumber);

}

