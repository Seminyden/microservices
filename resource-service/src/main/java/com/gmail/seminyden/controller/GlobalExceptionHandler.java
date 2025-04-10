package com.gmail.seminyden.controller;

import com.gmail.seminyden.exception.ResourceNotFoundException;
import com.gmail.seminyden.model.ErrorResponse;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .errorMessage("Validation error")
                        .errorCode("400")
                        .details(getDetails(e.getBindingResult()))
                        .build()
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HandlerMethodValidationException e) {
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .errorMessage("Validation error")
                        .errorCode("400")
                        .details(getDetails(e.getParameterValidationResults()))
                        .build()
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .errorMessage(e.getMessage())
                        .errorCode("400")
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMetadataNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(
                ErrorResponse.builder()
                        .errorMessage(e.getMessage())
                        .errorCode("404")
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(
                ErrorResponse.builder()
                        .errorMessage("Internal Server Error")
                        .errorCode("500")
                        .build()
        );
    }

    private Map<String, String> getDetails(BindingResult result) {
        Map<String, String> details = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            details.put(error.getField(), error.getDefaultMessage());
        }
        return details;
    }

    private Map<String, String> getDetails(List<ParameterValidationResult> result) {
        Map<String, String> details = new HashMap<>();
        for (ParameterValidationResult validationResult : result) {
            String parameterName = getParameterName(validationResult);
            for (MessageSourceResolvable resolvableError : validationResult.getResolvableErrors()) {
                details.put(parameterName, resolvableError.getDefaultMessage());
            }
        }
        return details;
    }

    private String getParameterName(ParameterValidationResult validationResult) {
        return validationResult.getMethodParameter().getParameterName();
    }
}