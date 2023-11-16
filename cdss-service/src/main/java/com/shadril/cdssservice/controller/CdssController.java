package com.shadril.cdssservice.controller;

import com.shadril.cdssservice.dto.ResponseMessageDto;
import com.shadril.cdssservice.exception.CustomException;
import com.shadril.cdssservice.service.CdssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/cdss")
public class CdssController {
    @Autowired
    private CdssService cdssService;

    @GetMapping("/health-recommendation")
    public ResponseEntity<ResponseMessageDto> getHealthRecommendation(@RequestBody(required = true) String prompt)
            throws CustomException {
        log.info("inside getHealthRecommendation method from CdssController class");
        String response = cdssService.getResponseFromOpenAi(prompt);
        return new ResponseEntity<>(new ResponseMessageDto(response, HttpStatus.OK), HttpStatus.OK);
    }
}
