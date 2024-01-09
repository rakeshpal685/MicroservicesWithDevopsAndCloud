package com.rakesh.accounts.exception;

import com.rakesh.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/*@RestControllerAdvice and @ControllerAdvice are annotations that allow you to handle exceptions and
other common aspects of controller methods in a global way. The main difference is that
@RestControllerAdvice automatically adds @ResponseBody to all the methods, which means
that the return values are serialized to JSON or XML and written to the response body.
On the other hand, @ControllerAdvice does not have this behavior, and you need to use
@ResponseBody or ResponseEntity explicitly if you want to return data instead of views.
You can use @RestControllerAdvice for RESTful web services, and @ControllerAdvice for MVC applications
or mixed scenarios.*/

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
      ResourceNotFoundException exception, WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO =
        new ErrorResponseDto(
            webRequest.getDescription(false),
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            LocalDateTime.now());

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CustomerAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(
      CustomerAlreadyExistsException exception, WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO =
        new ErrorResponseDto(
            webRequest.getDescription(false),
            HttpStatus.BAD_REQUEST,
            exception.getMessage(),
            LocalDateTime.now());

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
  }
}
