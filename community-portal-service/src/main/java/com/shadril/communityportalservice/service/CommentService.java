package com.shadril.communityportalservice.service;

import com.shadril.communityportalservice.dto.CommentDto;
import com.shadril.communityportalservice.exception.CustomException;

public interface CommentService {
    void createComment(CommentDto commentDto) throws CustomException;
}
