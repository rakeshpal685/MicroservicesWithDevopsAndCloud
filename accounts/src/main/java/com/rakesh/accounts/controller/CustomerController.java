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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "REST API for Customers in Rakesh Bank",
    description = "REST APIs in Rakesh Bank to FETCH customer details")
@RestController
@RequestMapping(
    path = "/api",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
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
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
          String mobileNumber) {
    CustomerDetailsDto customerDetailsDto =
        customerServiceInterface.fetchCustomerDetails(mobileNumber);
    return ResponseEntity.status(HttpStatus.OK).body(customerDetailsDto);
  }
}
