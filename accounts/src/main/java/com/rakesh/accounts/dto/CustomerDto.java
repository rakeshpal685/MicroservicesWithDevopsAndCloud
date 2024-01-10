package com.rakesh.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
// For swagger
@Schema(name = "Customer", description = "Schema to hold customer and Account information")
public class CustomerDto {

  // For validation
  @NotEmpty(message = "Name cannot be null or empty")
  @Size(min = 5, max = 30, message = "Name should be between 5 and 30 characters")
  // For Swagger
  @Schema(description = "Name of the customer", example = "Rakesh")
  private String name;

  @NotEmpty(message = "Email cannot be null or empty")
  @Email(message = "Email address should be valid")
  @Schema(description = "Email address of the customer", example = "abc@xyz.com")
  private String email;

  // Validation
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
  // swagger
  @Schema(description = "Mobile number of the customer", example = "1234567890")
  private String mobileNumber;

  @Schema(description = "Account details of the customer")
  private AccountsDto accountsDto;
}
