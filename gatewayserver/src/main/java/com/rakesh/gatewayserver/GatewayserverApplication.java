package com.rakesh.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

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
            "AccountsServiceRoute",
            p ->
                p.path("/eazybank/accounts/**")
                    .filters(
                        f ->
                            f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
                                /*  Here we are adding response Headers */
                                .addResponseHeader(
                                    "X-Response-Time", LocalDateTime.now().toString())
                                .addResponseHeader("Company-Name", "Rakesh Bank")
                                /*Here we are implementing circuit breaker pattern for my account service with any name and specifying the values in yml*/
                                .circuitBreaker(
                                    config ->
                                        config
                                            .setName("accountCircuitBreaker")
                                            /*This is to execute the fallback method when the service is down in case of circuit breaker,
                                             * we have created a separate controller for this*/
                                            .setFallbackUri("forward:/contactSupport")))
                    .uri("lb://ACCOUNTS"))
        .route(
            "LoansServiceRoute",
            p ->
                p.path("/eazybank/loans/**")
                    .filters(
                        f ->
                            f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response-Time", LocalDateTime.now().toString())
                                /*Here I am implementing Retry pattern, I am providing how many times I should retry and on
                                 * which types of methods I should retry, we have to be careful while retrying, it is ok with GET
                                 * but for POST/PUT/DELETE but must be careful because retrying on these types of methods may cause
                                 * issues,
                                 * unlike circuit breaker pattern as above. we cannot call a fallback method here, for this we have
                                 * to go the method in controller/service which need the retry and define fallback method there, eg:-
                                 * getBuildInfo method in AccountsController*/
                                .retry(
                                    retryConfig ->
                                        retryConfig
                                            .setRetries(3)
                                            .setMethods(HttpMethod.GET)
                                            /*Here we are defining the retry strategy, the very first parameter indicates what is the first backoff that you want to follow.
                                            So using this first backoff value, my spring cloud gateway will wait for 100 milliseconds whenever it is trying to initiate the
                                            very first retry operation. with the second parameter maxBackoff() at any point of time, my spring cloud gateway will wait only for a maximum
                                            of 1000 milliseconds or one second between two retry operations.
                                            And the third parameter indicates what is a factor that we want the spring cloud gateway to apply based upon the previous backoff value.
                                            the fourth one, which is a Boolean parameter.So based upon these boolean, we are trying to tell Spring Cloud Gateway whether it needs to apply the
                                            factor value on the previous backoff number or the initial backoff number.
                                            Since we are setting these boolean as true, my Spring Cloud Gateway always considered the previous
                                            backoff.*/
                                            .setBackoff(
                                                Duration.ofMillis(100),
                                                Duration.ofMillis(1000),
                                                2,
                                                true)))
                    .uri("lb://LOANS"))
        .route(
            "CardsServiceRoute",
            p ->
                p.path("/eazybank/cards/**")
                    .filters(
                        f ->
                            f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader(
                                    "X-Response-Time", LocalDateTime.now().toString())
                                /*Here I am using rate limiter pattern for resiliency, I am passing a bean of type redisRateLimiter and userKeyResolver,
                                which is defined below*/
                                .requestRateLimiter(
                                    config ->
                                        config
                                            .setRateLimiter(redisRateLimiter())
                                            .setKeyResolver(userKeyResolver())))
                    .uri("lb://CARDS"))
        .build();
  }

  /*This is to change the default values that is given to circuit break by spring,like how much time it should wait before
  changing state, if this is required in our project then we can do it this way , here it is for reference*/
  @Bean
  public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
    return factory ->
        factory.configureDefault(
            id ->
                new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(
                        TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                    .build());
  }

  /*Below two beans are for RateLimiter, userKeyResolver() is used to get the user if not present then put anonymous,
   redisRateLimiter() has 3 parameters that defines how many tokens to be added in the given time, here I am specifying
  1 token in each second with burst capacity as 1*/
  @Bean
  public RedisRateLimiter redisRateLimiter() {
    return new RedisRateLimiter(1, 1, 1);
  }

  @Bean
  public KeyResolver userKeyResolver() {
    return exchange ->
        Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
            .defaultIfEmpty("anonymous");
  }
}
