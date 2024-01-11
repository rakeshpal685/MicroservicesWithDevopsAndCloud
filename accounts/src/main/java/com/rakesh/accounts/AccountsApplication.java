package com.rakesh.accounts;

import com.rakesh.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
/*This annotation tells spring to see the POJO class where we are mapping our properties from the yml file*/
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
public class AccountsApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountsApplication.class, args);
  }
}
