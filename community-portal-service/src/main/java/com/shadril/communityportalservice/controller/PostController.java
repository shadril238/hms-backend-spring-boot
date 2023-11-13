package com.shadril.communityportalservice.controller;

import com.shadril.communityportalservice.dto.PostDto;
import com.shadril.communityportalservice.dto.ResponseMessageDto;
import com.shadril.communityportalservice.exception.CustomException;
import com.shadril.communityportalservice.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessageDto> createPost(@Valid @RequestBody PostDto postDto)
            throws CustomException {
        log.info("inside createPost method of PostController");
        postService.createPost(postDto);
        log.info("post created successfully");
        return new ResponseEntity<>(new ResponseMessageDto("Post created successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }
}
