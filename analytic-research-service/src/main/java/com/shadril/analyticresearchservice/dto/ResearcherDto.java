package com.shadril.analyticresearchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearcherDto {
    private Long id;
    private String name;
    private String email;
    private String designation;
    private String institute;
    private String purpose;
    private Boolean isValid;
    private Boolean isTaken;
}
