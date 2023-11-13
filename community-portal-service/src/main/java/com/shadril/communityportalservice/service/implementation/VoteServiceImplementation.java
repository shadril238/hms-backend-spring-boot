package com.shadril.communityportalservice.service.implementation;

import com.shadril.communityportalservice.dto.PatientDto;
import com.shadril.communityportalservice.dto.ResponseMessageDto;
import com.shadril.communityportalservice.dto.VoteCountDto;
import com.shadril.communityportalservice.dto.VoteDto;
import com.shadril.communityportalservice.entity.PostEntity;
import com.shadril.communityportalservice.entity.VoteEntity;
import com.shadril.communityportalservice.enums.VoteType;
import com.shadril.communityportalservice.exception.CustomException;
import com.shadril.communityportalservice.networkmanager.PatientServiceFeignClient;
import com.shadril.communityportalservice.repository.PostRepository;
import com.shadril.communityportalservice.repository.VoteRepository;
import com.shadril.communityportalservice.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VoteServiceImplementation implements VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private PatientServiceFeignClient patientServiceFeignClient;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepository postRepository;
    @Override
    public void castVote(VoteDto voteDto) throws CustomException {
        try {
            log.info("inside castVote method of VoteServiceImplementation");
            ResponseEntity<PatientDto> patientDtoResponse = patientServiceFeignClient.getCurrentPatient();
            if(patientDtoResponse.getBody() == null || patientDtoResponse.getStatusCode() != HttpStatus.OK) {
                throw new CustomException(new ResponseMessageDto("You are not authorized to vote this post", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
            }

            Optional<PostEntity> postEntity = postRepository.findById(voteDto.getPostId());
            if(postEntity.isEmpty() || !postEntity.get().isActive()){
                throw new CustomException(new ResponseMessageDto("Post not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            String patientId = patientDtoResponse.getBody().getPatientId();

            // Check if the user has already voted
            Optional<VoteEntity> existingVote = voteRepository.findByPatientIdAndPostId(patientId, voteDto.getPostId());
            if (existingVote.isPresent() && existingVote.get().isActive()) {
                // If the user has already voted, update the vote if different
                VoteEntity vote = existingVote.get();
                if (!vote.getVoteType().equals(voteDto.getVoteType())) {
                    vote.setVoteType(voteDto.getVoteType());
                    voteRepository.save(vote);
                } else {
                    // If the user has already voted and the vote is same, throw CustomException
                    throw new CustomException(new ResponseMessageDto("You have already "+vote.getVoteType()+"d this post", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
                }
            } else {
                // If the user has not voted, create a new vote
                VoteEntity newVote = new VoteEntity();
                newVote.setPatientId(patientId);
                newVote.setVoteType(voteDto.getVoteType());
                newVote.setPost(postEntity.get());
                newVote.setActive(true);
                newVote.setPatientId(patientId);
                voteRepository.save(newVote);
            }
        } catch (CustomException ex) {
            log.error("Error occurred while casting vote: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public VoteCountDto countVotes(Long postId) throws CustomException {
        try{
            log.info("inside countVotes method of VoteServiceImplementation");
            Optional<PostEntity> postEntity = postRepository.findById(postId);
            if(postEntity.isEmpty() || !postEntity.get().isActive()){
                throw new CustomException(new ResponseMessageDto("Post not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            Integer upVoteCount = voteRepository.countByPostIdAndVoteType(postId, VoteType.Upvote);
            Integer downVoteCount = voteRepository.countByPostIdAndVoteType(postId, VoteType.Downvote);

            VoteCountDto voteCountDto = new VoteCountDto();
            voteCountDto.setPostId(postId);
            voteCountDto.setUpVoteCount(upVoteCount);
            voteCountDto.setDownVoteCount(downVoteCount);
            return voteCountDto;
        } catch (CustomException ex) {
            log.error("Error occurred while counting votes: " + ex.getMessage());
            throw ex;
        }
    }
}
