package com.shadril.communityportalservice.service;

import com.shadril.communityportalservice.dto.PostDto;
import com.shadril.communityportalservice.exception.CustomException;

public interface PostService {
    void createPost(PostDto postDto) throws CustomException;
}
