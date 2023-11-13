package com.shadril.communityportalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteCountDto {
    private Long postId;
    private Integer upVoteCount;
    private Integer downVoteCount;
}
