package com.rakesh.accounts.controller;

import com.rakesh.accounts.dto.CustomerDetailsDto;
import com.rakesh.accounts.dto.ErrorResponseDto;
import com.rakesh.accounts.service.CustomerServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "REST API for Customers in Rakesh Bank",
    description = "REST APIs in Rakesh Bank to FETCH customer details")
@RestController
@RequestMapping(
    path = "/api",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Log4j2
public class CustomerController {
  private final CustomerServiceInterface customerServiceInterface;

  public CustomerController(CustomerServiceInterface customerServiceInterface) {
    this.customerServiceInterface = customerServiceInterface;
  }

  @Operation(
      summary = "Fetch Customer Details REST API",
      description = "REST API to fetch Customer details based on a mobile number",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP status NOT_FOUND"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      })
  @GetMapping("/fetchCustomerDetails")
  public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
      /*With this, this REST API will have access to the request header that is being sent by my gateway server.
       * Which we can use in the logger and will see that during the API call whether the same
       * correlationId is travelling or not, this is helpful during debug*/
      @RequestHeader("eazybank-correlation-id") String correlationId,
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
          String mobileNumber) {
    log.debug("eazyBank-correlation-id found:{}", correlationId);
    CustomerDetailsDto customerDetailsDto =
        customerServiceInterface.fetchCustomerDetails(mobileNumber, correlationId);
    return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);
  }
}
