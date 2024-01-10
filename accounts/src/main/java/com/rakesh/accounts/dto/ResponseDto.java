package com.rakesh.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// For swagger
@Schema(name = "Response", description = "Schema to hold successful response information")
public class ResponseDto {

  // For swagger
  @Schema(description = "Status code in the response")
  private String statusCode;

  // For swagger

  @Schema(description = "Status message in the response")
  private String statusMsg;
}
