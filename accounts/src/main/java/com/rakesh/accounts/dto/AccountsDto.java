package com.rakesh.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountsDto {

  @NotEmpty(message = "Account number cannot be null or emplty")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits" )
  private Long accountNumber;

  @NotEmpty(message = "Account Type cannot be null or empty")
  private String accountType;

  @NotEmpty(message = "Account Type cannot be null or empty")
  private String branchAddress;
}
