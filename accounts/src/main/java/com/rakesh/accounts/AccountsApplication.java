package com.rakesh.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/*If we are following the standard approach and creating the packages parallel to main class then spring
will auto scan them but if we are creating the packages in some other location then we have to specify the
location of our classes so that spring can create bean of them, using the below ways...
@ComponentScans({@ComponentScan("com.rakesh.accounts.controller")})
@EnableJpaRepositories("com.rakesh.accounts.repository")
@EntityScan("com.rakesh.accounts.model")*/

/*This way we are telling to the spring boot framework. Please activate the JPAAuditing and please
use the bean with the name auditAwareImpl to understand the current auditor.*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
// This is used for enabling documentation using swagger
@OpenAPIDefinition(
    info =
        @Info(
            title = "Account microservice REST API Documentation",
            description = "Here I can give description",
            version = "v1",
            contact = @Contact(name = "Rakesh Pal", email = "abc@xyz.com", url = "www.abc.com"),
            license = @License(name = "Apache 2.0", url = "www.abc.com")),
    externalDocs =
        @ExternalDocumentation(
            description = "This is external documentation for further reference",
            url = "www.springboot.com"))
public class AccountsApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountsApplication.class, args);
  }
}
