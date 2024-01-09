package com.rakesh.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
/*This way we are telling to the spring boot framework. Please activate the JPAAuditing and please
use the bean with the name auditAwareImpl to understand the current auditor.*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AccountsApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountsApplication.class, args);
  }
}
