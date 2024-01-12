package com.rakesh.loans.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*This class is used to read the properties from the yml file, this is the replacement for @Value or Environment
variable because we have a lot of properties under the same name, by using the above two annotations we have to
create a long list of variable for each property

our message property is simple string
contactDetails have key:value pairs
onCallSupport has list

the @ConfigurationProperties tells spring to map the properties that start with the given prefix with the
value in this class/record, the field names defined here must match with the variable names defined in the
properties file*/

@ConfigurationProperties(prefix = "loans")
@Getter
@Setter
public class LoansContactInfoDto {

  private String message;
  private Map<String, String> contactDetails;
  private List<String> onCallSupport;
}
