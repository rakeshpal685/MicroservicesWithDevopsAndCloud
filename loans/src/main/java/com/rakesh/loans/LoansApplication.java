package com.rakesh.loans;

import com.rakesh.loans.dto.LoansContactInfoDto;
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
/*@ComponentScans({ @ComponentScan("com.rakesh.loans.controller") })
@EnableJpaRepositories("com.rakesh.loans.repository")
@EntityScan("com.rakesh.loans.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
    info =
        @Info(
            title = "Loans microservice REST API Documentation",
            description = "EazyBank Loans microservice REST API Documentation",
            version = "v1",
            contact =
                @Contact(
                    name = "Madan Reddy",
                    email = "tutor@rakesh.com",
                    url = "https://www.rakesh.com"),
            license = @License(name = "Apache 2.0", url = "https://www.rakesh.com")),
    externalDocs =
        @ExternalDocumentation(
            description = "EazyBank Loans microservice REST API Documentation",
            url = "https://www.rakesh.com/swagger-ui.html"))
/*This annotation tells spring to see the POJO class where we are mapping our properties from the yml file*/
@EnableConfigurationProperties(value = {LoansContactInfoDto.class})
public class LoansApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoansApplication.class, args);
  }
}
