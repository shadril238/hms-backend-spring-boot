package com.shadril.patientservice.controller;

import com.shadril.patientservice.dtos.ResponseMessageDto;
import com.shadril.patientservice.exception.CustomException;
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

//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<ResponseMessageDto> handleFeignException(FeignException e) {
//        HttpStatus status = HttpStatus.valueOf(e.status());
//        String errorMessage = "Error occurred while calling " + e.getMessage();
//        ResponseMessageDto responseMessage = new ResponseMessageDto(errorMessage, status);
//        return new ResponseEntity<>(responseMessage, status);
//    }
//
//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<ResponseMessageDto> handleNullPointerException(NullPointerException e) {
//        ResponseMessageDto responseMessage = new ResponseMessageDto("Null Pointer Exception", HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
//    }
}