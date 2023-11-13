package com.shadril.communityportalservice.service.implementation;

import com.shadril.communityportalservice.dto.PatientDto;
import com.shadril.communityportalservice.dto.PostDto;
import com.shadril.communityportalservice.dto.ResponseMessageDto;
import com.shadril.communityportalservice.entity.PostEntity;
import com.shadril.communityportalservice.exception.CustomException;
import com.shadril.communityportalservice.networkmanager.PatientServiceFeignClient;
import com.shadril.communityportalservice.repository.PostRepository;
import com.shadril.communityportalservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
            postEntity.setPatientId(patientDtoResponse.getBody().getPatientId());
            postRepository.save(postEntity);
            log.info("post created successfully");
        } catch(CustomException ex) {
            log.error("error occurred while creating post: " + ex.getMessage());
            throw ex;
        }
    }
}
