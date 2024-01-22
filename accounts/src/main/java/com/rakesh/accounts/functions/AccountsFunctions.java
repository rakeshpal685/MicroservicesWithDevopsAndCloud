package com.rakesh.accounts.functions;

import com.rakesh.accounts.service.AccountServiceInterf;
import java.util.function.Consumer;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* This class is created to accept the message from the message microservice via RabbitMQ and then using accountsService
we are updating the DB with the dta that is received, we have told spring about this method in yml file*/
@Configuration
@Log4j2
public class AccountsFunctions {

  @Bean
  public Consumer<Long> updateCommunication(AccountServiceInterf accountsService) {
    return accountNumber -> {
      log.info(
          "Updating Communication status for the account number : " + accountNumber.toString());
      accountsService.updateCommunicationStatus(accountNumber);
    };
  }
}
