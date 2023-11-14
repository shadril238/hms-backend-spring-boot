package com.shadril.communityportalservice.service.implementation;

import com.shadril.communityportalservice.dto.CommentDto;
import com.shadril.communityportalservice.dto.PatientDto;
import com.shadril.communityportalservice.dto.PostDto;
import com.shadril.communityportalservice.dto.ResponseMessageDto;
import com.shadril.communityportalservice.entity.CommentEntity;
import com.shadril.communityportalservice.entity.PostEntity;
import com.shadril.communityportalservice.exception.CustomException;
import com.shadril.communityportalservice.networkmanager.PatientServiceFeignClient;
import com.shadril.communityportalservice.repository.CommentRepository;
import com.shadril.communityportalservice.repository.PostRepository;
import com.shadril.communityportalservice.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImplementation implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PatientServiceFeignClient patientServiceFeignClient;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createComment(CommentDto commentDto) throws CustomException {
        try{
            log.info("inside createComment method of CommentServiceImplementation");
            ResponseEntity<PatientDto> patientDtoResponse = patientServiceFeignClient.getCurrentPatient();
            if(patientDtoResponse.getBody() == null || patientDtoResponse.getStatusCode() != HttpStatus.OK) {
                throw new CustomException(new ResponseMessageDto("You are not authorized to create a comment", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }

            Optional<PostEntity> postEntity = postRepository.findById(commentDto.getPostId());
            if(postEntity.isEmpty() || !postEntity.get().isActive()){
                throw new CustomException(new ResponseMessageDto("Post not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setPatientId(patientDtoResponse.getBody().getPatientId());
            commentEntity.setPost(postEntity.get());
            commentEntity.setCommentContent(commentDto.getCommentContent());
            commentEntity.setCreatedAt(LocalDateTime.now());
            commentEntity.setActive(true);
            commentRepository.save(commentEntity);
            log.info("comment created successfully");
        } catch (CustomException ex) {
            log.error("error occurred while creating comment: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateComment(CommentDto commentDto) throws CustomException {
        // will be implemented in future
    }

    @Override
    public void deleteComment(Long commentId) throws CustomException {
        // will be implemented in future
    }

    @Override
    public CommentDto getCommentById(Long commentId) throws CustomException {
        try{
            log.info("inside getCommentById method of CommentServiceImplementation");
            Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
            if(commentEntity.isEmpty() || !commentEntity.get().isActive()){
                throw new CustomException(new ResponseMessageDto("Comment not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            CommentDto commentDto = new CommentDto();
            commentDto.setCommentId(commentEntity.get().getCommentId());
            commentDto.setPatientId(commentEntity.get().getPatientId());
            commentDto.setPostId(commentEntity.get().getPost().getPostId());
            commentDto.setCommentContent(commentEntity.get().getCommentContent());
            commentDto.setCreatedAt(commentEntity.get().getCreatedAt());
            commentDto.setActive(commentEntity.get().isActive());

            return commentDto;
        } catch (CustomException ex) {
            log.error("error occurred while fetching comment: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(Long postId) throws CustomException {
        try{
            log.info("inside getAllCommentsByPostId method of CommentServiceImplementation");
            Optional<PostEntity> postEntity = postRepository.findById(postId);
            if(postEntity.isEmpty() || !postEntity.get().isActive()){
                throw new CustomException(new ResponseMessageDto("Post not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("post found, fetching comments");

            List<CommentEntity> commentEntities = commentRepository.findByPostId(postId);
            if(commentEntities.isEmpty()){
                throw new CustomException(new ResponseMessageDto("No comments found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("comments fetched successfully");

            return commentEntities.stream()
                    .filter(CommentEntity::isActive)
                    .map(commentEntity -> {
                        CommentDto commentDto = new CommentDto();
                        commentDto.setCommentId(commentEntity.getCommentId());
                        commentDto.setPatientId(commentEntity.getPatientId());
                        commentDto.setCommentContent(commentEntity.getCommentContent());
                        commentDto.setCreatedAt(commentEntity.getCreatedAt());
                        commentDto.setActive(commentEntity.isActive());
                        commentDto.setPostId(commentEntity.getPost().getPostId());
                        return commentDto;
                    })
                    .toList();

        } catch (CustomException ex) {
            log.error("error occurred while fetching comments: " + ex.getMessage());
            throw ex;
        }
    }
}
