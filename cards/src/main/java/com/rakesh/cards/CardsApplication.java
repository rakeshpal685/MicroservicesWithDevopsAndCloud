package com.rakesh.cards;

import com.rakesh.cards.dto.CardsContactInfoDto;
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
/*@ComponentScans({ @ComponentScan("com.rakesh.cards.controller") })
@EnableJpaRepositories("com.rakesh.cards.repository")
@EntityScan("com.rakesh.cards.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
    info =
        @Info(
            title = "Cards microservice REST API Documentation",
            description = "EazyBank Cards microservice REST API Documentation",
            version = "v1",
            contact =
                @Contact(
                    name = "Madan Reddy",
                    email = "tutor@rakesh.com",
                    url = "https://www.rakesh.com"),
            license = @License(name = "Apache 2.0", url = "https://www.rakesh.com")),
    externalDocs =
        @ExternalDocumentation(
            description = "EazyBank Cards microservice REST API Documentation",
            url = "https://www.rakesh.com/swagger-ui.html"))
/*This annotation tells spring to see the POJO class where we are mapping our properties from the yml file*/
@EnableConfigurationProperties(value = {CardsContactInfoDto.class})
public class CardsApplication {
	public static void main(String[] args) {
    SpringApplication.run(CardsApplication.class, args);
  }
}
