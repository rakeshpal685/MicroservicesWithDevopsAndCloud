package com.rakesh.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

  private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

  @Autowired FilterUtility filterUtility;

  @Bean
  public GlobalFilter postGlobalFilter() {

    return (exchange, chain) -> {
      return chain
          .filter(exchange)
          .then(
              Mono.fromRunnable(
                  () -> {
                    HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                    String correlationId = filterUtility.getCorrelationId(requestHeaders);
                    /* Here we are doing a check whether our Response header already contains the given key or not,
                     * this is because we have implemented retry pattern circuit breaker, hence it will get the response header everytime
                     * it hits the URl during retry, hence we have kept  this check  */
                    if (!(exchange
                        .getResponse()
                        .getHeaders()
                        .containsKey(filterUtility.CORRELATION_ID))) {
                      logger.debug(
                          "Updated the correlation id to the outbound headers: {}", correlationId);
                      exchange
                          .getResponse()
                          .getHeaders()
                          .add(filterUtility.CORRELATION_ID, correlationId);
                    }
                  }));
    };
  }
}