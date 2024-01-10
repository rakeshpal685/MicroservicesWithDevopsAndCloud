package com.rakesh.accounts.controller;

import com.rakesh.accounts.constants.AccountsConstants;
import com.rakesh.accounts.dto.CustomerDto;
import com.rakesh.accounts.dto.ErrorResponseDto;
import com.rakesh.accounts.dto.ResponseDto;
import com.rakesh.accounts.service.AccountServiceInterf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    value = "/api",
    produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
/*So this annotation will tell to my spring boot framework to perform validations on all the REST APIs
that I have defined inside these AccountsController.*/
@Validated
// Below annotations are for swagger
@Tag(
    name = "CRUD REST APIs for Accounts in Rakesh Bank",
    description =
        "CRUD REST APIs for Accounts in Rakesh Bank to CREATE,UPDATE,DELETE, FETCH account details")
public class AccountsController {

  private AccountServiceInterf accountServiceInterf;

  @PostMapping("/create")
  // Below are for swagger
  @Operation(
      summary = "Create Account REST API",
      description = "REST API to create a new customer in the bank",
      responses = {
        @ApiResponse(responseCode = "201", description = "HTTP status CREATED"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      })
  public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
    accountServiceInterf.createAccount(customerDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
  }

  @GetMapping("/fetch")
  // Below are for swagger
  @Operation(
      summary = "Fetch Account REST API",
      description = "REST API to fetch customer and account details based on the mobile number",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP status NOT_FOUND"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      })
  public ResponseEntity<CustomerDto> fetchAccountDetails(
      @RequestParam
      @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
          String mobileNumber) {
    CustomerDto customerDto = accountServiceInterf.fetchAccount(mobileNumber);
    return ResponseEntity.status(HttpStatus.OK).body(customerDto);
  }

  @PutMapping("/update")
  // Below are for swagger
  @Operation(
      summary = "Update Account Details REST API",
      description = "REST API to update customer and account details based on the account number",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP status NOT_FOUND"),
        @ApiResponse(responseCode = "417", description = "Exception Failed"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            /* Here I'm telling to spring that for these update operation, there is a good chance a 500 error can happen.
            Whenever that 500 error happens, I'm going to send the ErrorResponse by following the schema defined inside this ErrorResponseDto.*/
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  public ResponseEntity<ResponseDto> updateAccountDetails(
      @Valid @RequestBody CustomerDto customerDto) {
    boolean isUpdated = accountServiceInterf.updateAccount(customerDto);
    if (isUpdated) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(
              new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
    }
  }

  @DeleteMapping("/delete")
  // Below are for swagger
  @Operation(
      summary = "Delete Account Details REST API",
      description = "REST API to delete customer and account details based on the account number",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP status NOT_FOUND"),
        @ApiResponse(responseCode = "417", description = "Exception Failed"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      })
  public ResponseEntity<ResponseDto> deleteAccountDetails(
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
          String mobileNumber) {
    boolean isDeleted = accountServiceInterf.deleteAccount(mobileNumber);
    if (isDeleted) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(
              new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
    }
  }
}
