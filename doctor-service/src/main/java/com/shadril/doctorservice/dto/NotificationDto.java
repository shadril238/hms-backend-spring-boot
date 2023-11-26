package com.shadril.doctorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long notificationId;
    private Long userId;
    private String notificationType;
    private String notificationText;
    private LocalDateTime timestamp;
    private Boolean isRead = false;
}
