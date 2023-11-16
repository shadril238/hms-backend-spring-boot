package com.shadril.cdssservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiChoiceDto {
    private Integer index;
    private OpenAiMessageDto message;
}
