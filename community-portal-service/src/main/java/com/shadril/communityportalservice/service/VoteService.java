package com.shadril.communityportalservice.service;

import com.shadril.communityportalservice.dto.VoteDto;
import com.shadril.communityportalservice.exception.CustomException;

public interface VoteService {
    void castVote(VoteDto voteDto) throws CustomException;
}
