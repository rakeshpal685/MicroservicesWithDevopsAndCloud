package com.rakesh.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
// For swagger
@Schema(name = "Accounts", description = "Schema to hold Account information")
public class AccountsDto {

  @NotEmpty(message = "Account number cannot be null or empty")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
  // For swagger
  @Schema(description = "Account number of RakeshBank account", example = "9876543210")
  private Long accountNumber;

  @NotEmpty(message = "Account Type cannot be null or empty")
  // For swagger
  @Schema(description = "Account type of RakeshBank account", example = "Savings")
  private String accountType;

  @NotEmpty(message = "Account Type cannot be null or empty")
  // For swagger
  @Schema(description = "RakeshBank branch address", example = "123 NewYork")
  private String branchAddress;
}
