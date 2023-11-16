package com.shadril.cdssservice.service.implementation;
import com.shadril.cdssservice.constants.OpenAiConstants;
import com.shadril.cdssservice.dto.OpenAiRequestDto;
import com.shadril.cdssservice.dto.OpenAiResponseDto;
import com.shadril.cdssservice.dto.ResponseMessageDto;
import com.shadril.cdssservice.exception.CustomException;
import com.shadril.cdssservice.service.CdssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CdssServiceImplementation implements CdssService {
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public String getResponseFromOpenAi(String prompt)
            throws CustomException {
        try{
            log.info("inside getResponseFromOpenAi method from CdssServiceImplementation class");

            OpenAiRequestDto request = new OpenAiRequestDto(OpenAiConstants.GPT_MODEL, prompt);
            OpenAiResponseDto response = restTemplate.postForObject(OpenAiConstants.API_URL, request, OpenAiResponseDto.class);
            if (response == null) {
                throw new CustomException(new ResponseMessageDto("The cdss service is not available right now. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("response from OpenAi: {}", response);

            return response.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("error in getResponseFromOpenAi method from CdssServiceImplementation class: {}", e.getMessage());
            throw new CustomException(new ResponseMessageDto("The cdss service is not available right now. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
