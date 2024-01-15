package com.rakesh.loans.controller;

import com.rakesh.loans.constants.LoansConstants;
import com.rakesh.loans.dto.ErrorResponseDto;
import com.rakesh.loans.dto.LoansContactInfoDto;
import com.rakesh.loans.dto.LoansDto;
import com.rakesh.loans.dto.ResponseDto;
import com.rakesh.loans.service.ILoansService;
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

/**
 * @author Rakesh Pal
 */
@Tag(
    name = "CRUD REST APIs for Loans in Rakesh Bank",
    description = "CRUD REST APIs in Rakesh Bank to CREATE, UPDATE, FETCH AND DELETE loan details")
@RestController
@RequestMapping(
    path = "/api",
    produces = {MediaType.APPLICATION_JSON_VALUE})
/* As we are using @Value to read a value from the yml file, and we are using constructor injection
this will throw an error that it need a bean for the String buildVersion, We can resolve this by either
manually creating a constructor or by using @Autowired over the declaration
@AllArgsConstructor*/
@Validated
public class LoansController {

  private final ILoansService iLoansService;

  public LoansController(ILoansService iLoansService) {
    this.iLoansService = iLoansService;
  }

  //  This is used to read the value from the property file
  @Value("${build.version}")
  private String buildVersion;

  /*   We can use Environment also to read the value from the environment, this is alternative to
  @Value*/
  @Autowired private Environment environment;

  /*We can use this as a replacement for @Value and Environment when we have a lot of properties to map*/
  @Autowired private LoansContactInfoDto loansContactInfoDto;

  @Operation(
      summary = "Create Loan REST API",
      description = "REST API to create new loan inside Rakesh Bank",
      responses = {
        @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP Status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createLoan(
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
          String mobileNumber) {
    iLoansService.createLoan(mobileNumber);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
  }

  @Operation(
      summary = "Fetch Loan Details REST API",
      description = "REST API to fetch loan details based on a mobile number",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP Status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @GetMapping("/fetch")
  public ResponseEntity<LoansDto> fetchLoanDetails(
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
          String mobileNumber) {
    LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);
    return ResponseEntity.status(HttpStatus.OK).body(loansDto);
  }

  @Operation(
      summary = "Update Loan Details REST API",
      description = "REST API to update loan details based on a loan number",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP Status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoansDto loansDto) {
    boolean isUpdated = iLoansService.updateLoan(loansDto);
    if (isUpdated) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
    }
  }

  @Operation(
      summary = "Delete Loan Details REST API",
      description = "REST API to delete Loan details based on a mobile number",
      responses = {
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP Status Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
      })
  @DeleteMapping("/delete")
  public ResponseEntity<ResponseDto> deleteLoanDetails(
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
          String mobileNumber) {
    boolean isDeleted = iLoansService.deleteLoan(mobileNumber);
    if (isDeleted) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
    } else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
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
  public ResponseEntity<LoansContactInfoDto> getContactInfo() {
    return ResponseEntity.status(HttpStatus.OK).body(loansContactInfoDto);
  }
}
