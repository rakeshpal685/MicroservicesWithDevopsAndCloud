package com.rakesh.gatewayserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

  @RequestMapping(value = "/contactSupport")
  public Mono<String> contactSupport() {
    return Mono.just("An error occured. Please try after some time or contact support team!!!");
  }
}
