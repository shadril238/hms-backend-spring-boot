package com.shadril.analyticresearchservice.controller;

import com.shadril.analyticresearchservice.dto.ResearcherDto;
import com.shadril.analyticresearchservice.dto.ResponseMessageDto;
import com.shadril.analyticresearchservice.exception.CustomException;
import com.shadril.analyticresearchservice.service.ResearcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@Slf4j
@RequestMapping("/research")
public class ResearchController {

    @Autowired
    private ResearcherService researcherService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessageDto> registerResearcher(@RequestBody ResearcherDto researcherDto)
            throws CustomException {
        log.info("Inside registerResearcher method of ResearchController");
        researcherService.registerResearcher(researcherDto);
        return new ResponseEntity<>(new ResponseMessageDto("Researcher registered successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/applied/all")
    public ResponseEntity<ResponseMessageDto> getAllTakenResearchers()
            throws CustomException {
        log.info("Inside getAllTakenResearchers method of ResearchController");
        researcherService.getAllValidResearchers();
        return new ResponseEntity<>(new ResponseMessageDto("Researcher registered successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/registered/all")
    public ResponseEntity<ResponseMessageDto> getAllResearchers()
            throws CustomException {
        log.info("Inside getAllResearchers method of ResearchController");
        researcherService.getAllResearchers();
        return new ResponseEntity<>(new ResponseMessageDto("Researcher registered successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/taken/all")
    public ResponseEntity<ResponseMessageDto> getAllValidResearchers()
            throws CustomException {
        log.info("Inside getAllValidResearchers method of ResearchController");
        researcherService.getAllValidResearchers();
        return new ResponseEntity<>(new ResponseMessageDto("Researcher registered successfully", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @GetMapping("/get/analytic-data/{id}")
    public ResponseEntity<ByteArrayResource> healthDataToCsv(@PathVariable("id") Long id)
            throws CustomException {
        String filename = "user_health_data-research.csv";
        researcherService.healthDataToCsv(id, filename);

        try{
            byte[] csvData = Files.readAllBytes(Paths.get(filename));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", filename);

            ByteArrayResource resource = new ByteArrayResource(csvData);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(csvData.length)
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/give-access/{id}")
    public ResponseEntity<ResponseMessageDto> giveAccessToResearcher(@PathVariable("id") Long id)
            throws CustomException {
        log.info("Inside giveAccessToResearcher method of ResearchController");
        researcherService.giveAccessToResearcher(id);
        return new ResponseEntity<>(new ResponseMessageDto("Access granted successfully!", HttpStatus.OK), HttpStatus.OK);
    }
}
