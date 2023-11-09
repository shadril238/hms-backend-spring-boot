package com.shadril.securityservice.controllers;

import com.shadril.securityservice.dtos.ResponseMessageDto;
import com.shadril.securityservice.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseMessageDto> handleCustomException(CustomException e) {
        ResponseMessageDto response = new ResponseMessageDto(e.getMessage(), e.getStatus());
        return new ResponseEntity<>(response, e.getStatus());
    }
}
