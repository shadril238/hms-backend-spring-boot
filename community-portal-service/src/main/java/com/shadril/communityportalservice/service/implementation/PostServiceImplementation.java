package com.shadril.communityportalservice.service.implementation;

import com.shadril.communityportalservice.dto.PatientDto;
import com.shadril.communityportalservice.dto.PostDto;
import com.shadril.communityportalservice.dto.ResponseMessageDto;
import com.shadril.communityportalservice.entity.PostEntity;
import com.shadril.communityportalservice.exception.CustomException;
import com.shadril.communityportalservice.networkmanager.PatientServiceFeignClient;
import com.shadril.communityportalservice.repository.PostRepository;
import com.shadril.communityportalservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImplementation implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PatientServiceFeignClient patientServiceFeignClient;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createPost(PostDto postDto) throws CustomException {
        try {
            log.info("inside createPost method of PostServiceImplementation");
            ResponseEntity<PatientDto> patientDtoResponse = patientServiceFeignClient.getCurrentPatient();
            if(patientDtoResponse.getBody() == null || patientDtoResponse.getStatusCode() != HttpStatus.OK) {
                throw new CustomException(new ResponseMessageDto("You are not authorized to create a post", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }

            PostEntity postEntity = new PostEntity();
            postEntity.setPostTitle(postDto.getPostTitle());
            postEntity.setPostContent(postDto.getPostContent());
            postEntity.setCreatedAt(LocalDateTime.now());
            postEntity.setActive(true);
            postEntity.setPatientId(patientDtoResponse.getBody().getPatientId());
            postRepository.save(postEntity);
            log.info("post created successfully");
        } catch(CustomException ex) {
            log.error("error occurred while creating post: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public PostDto getPostById(Long postId) throws CustomException {
        try{
            log.info("inside getPostById method of PostServiceImplementation");
            Optional<PostEntity> postEntity = postRepository.findById(postId);
            if(postEntity.isEmpty() || !postEntity.get().isActive()){
                throw new CustomException(new ResponseMessageDto("Post not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            return modelMapper.map(postEntity, PostDto.class);
        } catch (CustomException ex) {
            log.error("error occurred while fetching post: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<PostDto> getAllPosts() throws CustomException {
        try{
            log.info("inside getAllPosts method of PostServiceImplementation");
            List<PostEntity> postEntities = postRepository.findAll();

            if (postEntities.isEmpty()) {
                throw new CustomException(
                        new ResponseMessageDto("No posts found", HttpStatus.NOT_FOUND),
                        HttpStatus.NOT_FOUND);
            }

            return postEntities.stream()
                    .filter(PostEntity::isActive)
                    .map(postEntity -> modelMapper.map(postEntity, PostDto.class))
                    .collect(Collectors.toList());
        } catch (CustomException ex) {
            log.error("error occurred while fetching posts: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updatePost(PostDto postDto) throws CustomException {
        try{
            log.info("inside updatePost method of PostServiceImplementation");
            ResponseEntity<PatientDto> patientDtoResponse = patientServiceFeignClient.getCurrentPatient();
            if(patientDtoResponse.getBody() == null || patientDtoResponse.getStatusCode() != HttpStatus.OK) {
                throw new CustomException(new ResponseMessageDto("You are not authorized to update the post", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }

            Optional<PostEntity> postEntity = postRepository.findById(postDto.getPostId());
            if(postEntity.isEmpty() || !postEntity.get().isActive()){
                throw new CustomException(new ResponseMessageDto("Post not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            // check if the post belongs to the user
            if(!postEntity.get().getPatientId().equals(patientDtoResponse.getBody().getPatientId())){
                throw new CustomException(new ResponseMessageDto("You are not authorized to update the post", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }
            log.info("All validations passed, updating post");

            PostEntity updatedPostEntity = postEntity.get();
            updatedPostEntity.setPostTitle(postDto.getPostTitle() != null ? postDto.getPostTitle() : updatedPostEntity.getPostTitle());
            updatedPostEntity.setPostContent(postDto.getPostContent() != null ? postDto.getPostContent() : updatedPostEntity.getPostContent());
            updatedPostEntity.setActive(true);

            postRepository.save(updatedPostEntity);
            log.info("post updated successfully");
        } catch (CustomException ex) {
            log.error("error occurred while updating post: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void deletePost(Long postId) throws CustomException {
        // will be implemented later
    }
}
