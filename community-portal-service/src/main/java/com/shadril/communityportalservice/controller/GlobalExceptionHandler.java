package com.shadril.communityportalservice.controller;


import com.shadril.communityportalservice.dto.ResponseMessageDto;
import com.shadril.communityportalservice.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseMessageDto> handleCustomException(CustomException e) {
        ResponseMessageDto errorResponse = new ResponseMessageDto(e.getMessage(), e.getStatus());
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseMessageDto> handleResponseStatusException(ResponseStatusException e) {
        ResponseMessageDto responseMessage = new ResponseMessageDto(e.getReason(), (HttpStatus) e.getStatusCode());
        return new ResponseEntity<>(responseMessage, e.getStatusCode());
    }

}