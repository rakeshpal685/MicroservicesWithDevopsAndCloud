package com.rakesh.accounts.controller;

import com.rakesh.accounts.constants.AccountsConstants;
import com.rakesh.accounts.dto.AccountsContactInfoDto;
import com.rakesh.accounts.dto.CustomerDto;
import com.rakesh.accounts.dto.ErrorResponseDto;
import com.rakesh.accounts.dto.ResponseDto;
import com.rakesh.accounts.service.AccountServiceInterf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    value = "/api",
    produces = {MediaType.APPLICATION_JSON_VALUE})
/* As we are using @Value to read a value from the yml file, and we are using constructor injection
this will throw an error that it need a bean for the String buildVersion, We can resolve this by either
manually creating a constructor or by using @Autowired over the declaration
@AllArgsConstructor*/
/*So this annotation will tell to my spring boot framework to perform validations on all the REST APIs
that I have defined inside these AccountsController.*/
@Validated
// Below annotations are for swagger
@Tag(
    name = "CRUD REST APIs for Accounts in Rakesh Bank",
    description =
        "CRUD REST APIs for Accounts in Rakesh Bank to CREATE,UPDATE,DELETE, FETCH account details")
public class AccountsController {
  private final AccountServiceInterf accountServiceInterf;

  //  This is used to read the value from the property file
  @Value("${build.version}")
  private String buildVersion;

  /*   We can use Environment also to read the value from the environment, this is alternative to
  @Value*/
  @Autowired private Environment environment;

  /*We can use this as a replacement for @Value and Environment when we have a lot of properties to map*/
  @Autowired private AccountsContactInfoDto accountsContactInfoDto;

  /*    Here we are doing constructor injection, and if we have only one constructor then @Autowired
  over it is not necessary*/
  public AccountsController(AccountServiceInterf accountServiceInterf) {
    this.accountServiceInterf = accountServiceInterf;
  }

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
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
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

  @Operation(
      summary = "Get Build Information",
      description = "REST API to get the build information",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP status NOT_FOUND"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      })
  @GetMapping("/build-info")
  public ResponseEntity<String> getBuildInfo() {
    return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
  }

  @Operation(
      summary = "Get path Information of the system using Environment variable",
      description =
          "REST API to get the information about path variables and processor detail stored in the system environment ",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP status NOT_FOUND"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      })
  @GetMapping("/java-version")
  public ResponseEntity<String> getJavaInfo() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            environment.getProperty("path")
                + " \nThe processor detail is "
                + environment.getProperty("PROCESSOR_IDENTIFIER"));
  }

  @Operation(
      summary = "Get the contact info from the properties.yml file",
      description =
          "REST API to get the contact information from the properties.yml using @EnableConfigurationProperties and Java class",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP status NOT_FOUND"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
      })
  @GetMapping("/contact-info")
  public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
    return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfoDto);
  }


}
