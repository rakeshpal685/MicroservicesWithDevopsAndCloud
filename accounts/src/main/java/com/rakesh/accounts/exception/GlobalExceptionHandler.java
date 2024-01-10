package com.rakesh.accounts.exception;

import com.rakesh.accounts.dto.ErrorResponseDto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*@RestControllerAdvice and @ControllerAdvice are annotations that allow you to handle exceptions and
other common aspects of controller methods in a global way. The main difference is that
@RestControllerAdvice automatically adds @ResponseBody to all the methods, which means
that the return values are serialized to JSON or XML and written to the response body.
On the other hand, @ControllerAdvice does not have this behavior, and you need to use
@ResponseBody or ResponseEntity explicitly if you want to return data instead of views.
You can use @RestControllerAdvice for RESTful web services, and @ControllerAdvice for MVC applications
or mixed scenarios.*/

/*Here we are extending the parent class, so that we can override the handleMethodArgumentNotValid method
which is invoked when spring validation fails and this method will help us to send the appropriate response to the user */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

  /*  This will handle all other exceptions that might occur in our code during runtime and are not user
  defined like others as above.
  If any exception occurs then spring framework will first check if there is any ExceptionHandler present or
  not, if found it will execute that else will execute this*/
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGlobalException(
      Exception exception, WebRequest webRequest) {
    ErrorResponseDto errorResponseDTO =
        new ErrorResponseDto(
            webRequest.getDescription(false),
            HttpStatus.INTERNAL_SERVER_ERROR,
            exception.getMessage(),
            LocalDateTime.now());

    return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // This method is overriden from the parent, this will be invoked when we have any spring
  // validation failure
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, String> validationErrors = new HashMap<>();
    List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

    validationErrorList.forEach(
        (error) -> {
          String fieldName = ((FieldError) error).getField();
          String validationMsg = error.getDefaultMessage();
          validationErrors.put(fieldName, validationMsg);
        });
    return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
  }
}
