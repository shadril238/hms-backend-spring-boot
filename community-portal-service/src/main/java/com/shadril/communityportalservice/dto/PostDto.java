package com.shadril.communityportalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private String patientId;
    private String postTitle;
    private String postContent;
    private LocalDateTime createdAt;
}