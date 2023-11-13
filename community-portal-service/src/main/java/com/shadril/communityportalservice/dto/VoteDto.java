package com.shadril.communityportalservice.dto;

import com.shadril.communityportalservice.enums.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {
    private Long voteId;
    private String patientId;
    private Long postId;
    private VoteType voteType;
    private boolean isActive;
}
