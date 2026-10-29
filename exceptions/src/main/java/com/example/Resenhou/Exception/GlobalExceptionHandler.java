package com.example.Resenhou.Exception;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> handleEmailNotFound(EmailNotFoundException emailNotFound, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                emailNotFound.getMessage(),
                request.getDescription(false),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value()

        );

        return new  ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException UserNotFound, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                UserNotFound.getMessage(),
                request.getDescription(false),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value()

        );

        return new  ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodNotValidException(MethodArgumentNotValidException methodArgumentNotValid, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                methodArgumentNotValid.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
        return new  ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFound resourceNotFound, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                resourceNotFound.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
        return new  ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<Object> handleDataFormatException(DataFormatException dataFormat, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                dataFormat.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
        return new  ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException validEx, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                validEx.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
        return new  ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NameErrorException.class)
    public ResponseEntity<Object> handleNameErrorException(NameErrorException nameError, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                nameError.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
        return new  ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> handleSameValueException(DuplicateException duplicate, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                System.currentTimeMillis(),
                duplicate.getMessage(),
                request.getDescription(false),
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value()
        );
        return new  ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
