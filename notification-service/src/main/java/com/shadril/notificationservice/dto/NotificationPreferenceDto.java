package com.shadril.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferenceDto {
    private Long notificationPreferenceId;
    private Long userId;
    private String prefType;
    private boolean enabled;
}
