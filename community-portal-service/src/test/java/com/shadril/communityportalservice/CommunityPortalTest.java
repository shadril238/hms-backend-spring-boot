package com.shadril.communityportalservice;

import com.shadril.communityportalservice.dto.PatientDto;
import com.shadril.communityportalservice.dto.PostDto;
import com.shadril.communityportalservice.entity.PostEntity;
import com.shadril.communityportalservice.exception.CustomException;
import com.shadril.communityportalservice.networkmanager.PatientServiceFeignClient;
import com.shadril.communityportalservice.repository.PostRepository;
import com.shadril.communityportalservice.service.implementation.PostServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommunityPortalTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private PatientServiceFeignClient patientServiceFeignClient;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostServiceImplementation postService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createPostUnauthorizedTest() {
        ResponseEntity<PatientDto> patientDtoResponse = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        when(patientServiceFeignClient.getCurrentPatient()).thenReturn(patientDtoResponse);

        PostDto postDto = new PostDto();

        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.createPost(postDto);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    public void getPostByIdNotFoundTest() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.getPostById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void getAllPostsSuccessTest() throws CustomException {
        List<PostEntity> postEntities = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            PostEntity postEntity = new PostEntity();
            postEntity.setPostId((long) i);
            postEntity.setPostTitle("Test Title " + i);
            postEntity.setPostContent("Test Content " + i);
            postEntity.setActive(true);
            postEntities.add(postEntity);
        }
    }
}