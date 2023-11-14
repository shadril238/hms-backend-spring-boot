package com.shadril.communityportalservice.service;

import com.shadril.communityportalservice.dto.PostDto;
import com.shadril.communityportalservice.exception.CustomException;

import java.util.List;

public interface PostService {
    void createPost(PostDto postDto) throws CustomException;
    PostDto getPostById(Long postId) throws CustomException;
    List<PostDto> getAllPosts() throws CustomException;
    void updatePost(PostDto postDto) throws CustomException;
    void deletePost(Long postId) throws CustomException;

}
