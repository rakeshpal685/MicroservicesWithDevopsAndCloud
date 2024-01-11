package com.rakesh.accounts.dto;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*This class is used to read the properties from the yml file, this is the replacement for @Value or Environment
variable because we have a lot of properties under the same name, by using the above two annotations we have to
create a long list of variable for each property
We have created record because record will only have getters but no setters,
our message property is simple string
contactDetails have key:value pairs
onCallSupport has list

the @ConfigurationProperties tells spring to map the properties that start with the given prefix with the
value in this class/record, the field names defined here must match with the variable names defined in the
properties file*/

@ConfigurationProperties(prefix = "accounts")
public record AccountsContactInfoDto(
    String message, Map<String, String> contactDetails, List<String> onCallSupport) {}
