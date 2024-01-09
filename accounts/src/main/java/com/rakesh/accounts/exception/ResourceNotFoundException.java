package com.rakesh.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*In Spring MVC, we have many ways to set the status code of an HTTP response.
When an endpoint returns successfully, Spring provides an HTTP 200 (OK) response.
If we want to specify the response status of a controller method, we can mark that method with @ResponseStatus.
Hence when inside the code if ResourceNotFoundException happens then it will through the HttpStatus
response code as NOT_FOUND*/
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
    /*   As super() will take a single string, hence we are creating a single string using format method and
    using the three parameters that we will get when we will throw the error*/
    super(
        String.format(
            "%s not found with the given input data %s: '%s'",
            resourceName, fieldName, fieldValue));
  }

}
