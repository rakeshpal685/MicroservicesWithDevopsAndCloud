package com.rakesh.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayserverApplication.class, args);
  }

  /*	This method is going to create a bean of type RouteLocator. And inside this RouteLocator only we are going to send all
   our custom routing requirements. We are saying that whatever request that is coming to the route method should be
  filtered out, Here what I'm saying is after this prefix path, like easybank/accounts, whatever path is available,
  please assume that as a segment. Using the same segment value. I want the request to be forwarded to the actual
  microservice.
  Here, I'm trying to use a variable name as segment.	So whatever comes after these accounts in the path,
  I'm calling that as a segment and whatever path I have inside this segment variable, I'm trying to consider that as a base
  path when I'm trying to invoke the actual microservice.
  Hence we can say that whenever the client will hit the URL /eazybank/accounts/**, we will capture the ** path and use that
  as my original path for my microservice and then using the uri method and the Microservice name registered in the eureka, it
  will loadbalance and hit the microservice
  Here we are using eazybank as a custom routing
  We can do these by defining inside properties file also*/

  @Bean
  public RouteLocator rakeshBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
    return routeLocatorBuilder
        .routes()
        .route(
            p ->
                p.path("/eazybank/accounts/**")
                    .filters(
                        f ->
                            f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response-Time", LocalDateTime.now().toString()))
                    .uri("lb://ACCOUNTS"))
        .route(
            p ->
                p.path("/eazybank/loans/**")
                    .filters(
                        f ->
                            f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response-Time", LocalDateTime.now().toString()))
                    .uri("lb://LOANS"))
        .route(
            p ->
                p.path("/eazybank/cards/**")
                    .filters(
                        f ->
                            f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response-Time", LocalDateTime.now().toString()))
                    .uri("lb://CARDS"))
        .build();
  }
}
