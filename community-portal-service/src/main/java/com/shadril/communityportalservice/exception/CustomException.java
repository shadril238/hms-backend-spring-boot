package com.shadril.communityportalservice.exception;

import com.shadril.communityportalservice.dto.ResponseMessageDto;
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
