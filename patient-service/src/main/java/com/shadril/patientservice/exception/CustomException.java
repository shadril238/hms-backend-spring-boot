package com.shadril.patientservice.exception;

import com.shadril.patientservice.dtos.ResponseMessageDto;
import org.springframework.http.HttpStatus;

public class CustomException extends Exception {
    private final HttpStatus status;
    public CustomException(ResponseMessageDto responseMessageDto, HttpStatus status) {
        super(responseMessageDto.getMessage());
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
