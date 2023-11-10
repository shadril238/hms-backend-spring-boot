package com.shadril.doctorservice.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseMessageDto {
    private String message;
    private HttpStatus status;
}
