package com.shadril.cdssservice.service.implementation;
import com.shadril.cdssservice.constants.OpenAiConstants;
import com.shadril.cdssservice.dto.*;
import com.shadril.cdssservice.entity.HealthRecommendationEntity;
import com.shadril.cdssservice.enums.RecommendationType;
import com.shadril.cdssservice.exception.CustomException;
import com.shadril.cdssservice.networkmanager.PatientServiceFeignClient;
import com.shadril.cdssservice.repository.HealthRecommendationRepository;
import com.shadril.cdssservice.service.CdssService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CdssServiceImplementation implements CdssService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PatientServiceFeignClient patientServiceFeignClient;
    @Autowired
    private HealthRecommendationRepository healthRecommendationRepository;
    @Autowired
    private ModelMapper modelMapper;


    private String getResponseFromOpenAi(String prompt)
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

    private String getResopnseOffline(List<HealthRecordDto> healthRecordDtoList){
        //Todo: create custom offline recommendation method for generating recommendation
        return "";
    }

    private String createHealthPrompt(List<HealthRecordDto> healthRecords) {
        if (healthRecords.isEmpty()) {
            return "No health records available to generate a prompt.";
        }

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Generate a health recommendation based on the following patient health records:\n");

        for (HealthRecordDto record : healthRecords) {
            promptBuilder.append("Record Date: ").append(record.getCheckupDate()).append("\n")
                    .append("Height: ").append(record.getHeightInCm()).append(" cm, ")
                    .append("Weight: ").append(record.getWeightInKg()).append(" kg, ")
                    .append("Blood Pressure: ").append(record.getBloodPressure()).append(", ")
                    .append("Blood Sugar: ").append(record.getBloodSugar()).append(", ")
                    .append("Body Temperature: ").append(record.getBodyTemperature()).append(", ")
                    .append("Pulse Rate: ").append(record.getPulseRate()).append("\n")
                    .append("Allergies: ").append(record.getAllergies()).append(", ")
                    .append("Past Surgeries: ").append(record.getPastSurgeries()).append(", ")
                    .append("Diabetes: ").append(record.getHasDiabetes() ? "Yes" : "No").append(", ")
                    .append("Hypertension: ").append(record.getIsHypertensive() ? "Yes" : "No").append(", ")
                    .append("Heart Disease: ").append(record.getHasHeartDisease() ? "Yes" : "No").append(", ")
                    .append("Kidney Disease: ").append(record.getHasKidneyDisease() ? "Yes" : "No").append(", ")
                    .append("Liver Disease: ").append(record.getHasLiverDisease() ? "Yes" : "No").append(", ")
                    .append("Cancer: ").append(record.getHasCancer() ? "Yes" : "No").append(", ")
                    .append("HIV: ").append(record.getHasHiv() ? "Yes" : "No").append(", ")
                    .append("TB: ").append(record.getHasTb() ? "Yes" : "No").append(", ")
                    .append("Physical Disability: ").append(record.getPhysicalDisability()).append(", ")
                    .append("Vaccine Info: ").append(record.getVaccineInfo()).append(", ")
                    .append("Smoker: ").append(record.getIsSmoker() ? "Yes" : "No").append(", ")
                    .append("Alcoholic: ").append(record.getIsAlcoholic() ? "Yes" : "No").append(", ")
                    .append("Active: ").append(record.getIsActive() ? "Yes" : "No").append("\n\n");
        }

        promptBuilder.append("Based on the above information, provide comprehensive health advice or recommendations. The recommendation must be within 2 lines.");

        return promptBuilder.toString();
    }


    @Override
    public String generateHealthRecommendation()
            throws CustomException {
        log.info("inside getHealthRecommendation method from CdssServiceImplementation class");

        PatientDto patientDto = patientServiceFeignClient.getCurrentPatient().getBody();
        if (patientDto == null){
            throw new CustomException(new ResponseMessageDto("The cdss service is not available right now. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<HealthRecordDto> healthRecords = patientServiceFeignClient.getPatientsHealthRecords(patientDto.getPatientId()).getBody();
        if (healthRecords == null || healthRecords.isEmpty()) {
            throw new CustomException(new ResponseMessageDto("No health records found for the patient.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String prompt = createHealthPrompt(healthRecords);

        String recommendationResponse = getResponseFromOpenAi(prompt);
        if (recommendationResponse == null || recommendationResponse.isEmpty()) {
            recommendationResponse = getResopnseOffline(healthRecords);
        }

        HealthRecommendationEntity healthRecommendationEntity = new HealthRecommendationEntity();
        healthRecommendationEntity.setPatientId(patientDto.getPatientId());
        healthRecommendationEntity.setRecommendation(recommendationResponse);
        healthRecommendationEntity.setRecommendationType(RecommendationType.HealthRecommendation);
        healthRecommendationEntity.setCreatedAt(LocalDateTime.now());
        healthRecommendationRepository.save(healthRecommendationEntity);

        return recommendationResponse;
    }

    @Override
    public List<HealthRecommendationDto> getAllHealthRecommendations() throws CustomException {
        try {
            log.info("inside getAllHealthRecommendations method from CdssServiceImplementation class");
            List<HealthRecommendationEntity> healthRecommendationList = healthRecommendationRepository.findAll();
            if (healthRecommendationList.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("No health recommendations found.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return healthRecommendationList.stream()
                    .map(entity -> modelMapper.map(entity, HealthRecommendationDto.class))
                    .toList();

        } catch (Exception e) {
            log.error("error in getAllHealthRecommendations method from CdssServiceImplementation class: {}", e.getMessage());
            throw new CustomException(new ResponseMessageDto("The cdss service is not available right now. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public HealthRecommendationDto getHealthRecommendationById(Long id) throws CustomException {
        try{
            log.info("inside getHealthRecommendationById method from CdssServiceImplementation class");
            Optional<HealthRecommendationEntity> healthRecommendationEntity = healthRecommendationRepository.findById(id);
            if (healthRecommendationEntity.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("No health recommendation found with the given id.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return modelMapper.map(healthRecommendationEntity, HealthRecommendationDto.class);

        } catch (Exception e) {
            log.error("error in getHealthRecommendationById method from CdssServiceImplementation class: {}", e.getMessage());
            throw new CustomException(new ResponseMessageDto("The cdss service is not available right now. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<HealthRecommendationDto> getHealthRecommendationByPatientId(String patientId) throws CustomException {
        try{
            log.info("inside getHealthRecommendationByPatientId method from CdssServiceImplementation class");
            List<HealthRecommendationEntity> healthRecommendationList = healthRecommendationRepository.findAllByPatientId(patientId);
            if (healthRecommendationList.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("No health recommendations found for the given patient id.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return healthRecommendationList.stream()
                    .map(entity -> modelMapper.map(entity, HealthRecommendationDto.class))
                    .toList();

        } catch (Exception e) {
            log.error("error in getHealthRecommendationByPatientId method from CdssServiceImplementation class: {}", e.getMessage());
            throw new CustomException(new ResponseMessageDto("The cdss service is not available right now. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
