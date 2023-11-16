package com.shadril.cdssservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class OpenAiRequestDto {
    private String model;
    private List<OpenAiMessageDto> messages;

    public OpenAiRequestDto(String model, String prompt) {
        this.model = model;
        this.messages = List.of(new OpenAiMessageDto("user", prompt));
    }
}
