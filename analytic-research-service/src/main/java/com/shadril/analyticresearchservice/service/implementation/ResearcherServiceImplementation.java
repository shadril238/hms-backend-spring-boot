package com.shadril.analyticresearchservice.service.implementation;

import com.opencsv.CSVWriter;
import com.shadril.analyticresearchservice.builderpattern.ResearcherBuilder;
import com.shadril.analyticresearchservice.dto.HealthRecordDto;
import com.shadril.analyticresearchservice.dto.ResearcherDto;
import com.shadril.analyticresearchservice.dto.ResponseMessageDto;
import com.shadril.analyticresearchservice.entity.ResearcherEntity;
import com.shadril.analyticresearchservice.exception.CustomException;
import com.shadril.analyticresearchservice.networkmanager.PatientServiceFeignClient;
import com.shadril.analyticresearchservice.repository.AnalyticResearchRepository;
import com.shadril.analyticresearchservice.service.ResearcherService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ResearcherServiceImplementation implements ResearcherService {
    @Autowired
    private AnalyticResearchRepository analyticResearchRepository;
    @Autowired
    private PatientServiceFeignClient patientServiceFeignClient;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void registerResearcher(ResearcherDto researcherDto)  throws CustomException {
        try{
            log.info("Registering researcher with email: {}", researcherDto.getEmail());
            if (analyticResearchRepository.findByEmail(researcherDto.getEmail()).isPresent()) {
                log.error("Researcher with email: {} already exists", researcherDto.getEmail());
                throw new CustomException(new ResponseMessageDto("Researcher with email: " + researcherDto.getEmail() + " already exists", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            ResearcherEntity researcherEntity = new ResearcherBuilder()
                    .name(researcherDto.getName())
                    .email(researcherDto.getEmail())
                    .designation(researcherDto.getDesignation())
                    .institute(researcherDto.getInstitute())
                    .purpose(researcherDto.getPurpose())
                    .isValid(false)
                    .isTaken(false)
                    .build();

            analyticResearchRepository.save(researcherEntity);
            log.info("Researcher with email: {} registered successfully", researcherDto.getEmail());
        } catch (Exception e) {
            log.error("Error while registering researcher with email: {}", researcherDto.getEmail());
            throw new CustomException(new ResponseMessageDto("Error while registering researcher with email: " + researcherDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ResearcherDto> getAllResearchers() throws CustomException{
        try{
            log.info("Getting all researchers");
            List<ResearcherEntity> researcherEntities = analyticResearchRepository.findAll();
            List<ResearcherDto> researcherDtos = null;
            for (ResearcherEntity researcherEntity : researcherEntities) {
                researcherDtos.add(modelMapper.map(researcherEntity, ResearcherDto.class));
            }
            log.info("All researchers retrieved successfully");
            return researcherDtos;
        } catch (Exception e) {
            log.error("Error while getting all researchers");
            throw new CustomException(new ResponseMessageDto("Error while getting all researchers", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ResearcherDto> getAllValidResearchers() throws CustomException{
        try{
            log.info("Getting all valid researchers");
            List<ResearcherEntity> researcherEntities = analyticResearchRepository.findAllByIsValid(true);
            List<ResearcherDto> researcherDtos = null;
            for (ResearcherEntity researcherEntity : researcherEntities) {
                researcherDtos.add(modelMapper.map(researcherEntity, ResearcherDto.class));
            }
            log.info("All valid researchers retrieved successfully");
            return researcherDtos;
        } catch (Exception e) {
            log.error("Error while getting all valid researchers");
            throw new CustomException(new ResponseMessageDto("Error while getting all valid researchers", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ResearcherDto> getAllTakenResearchers() throws CustomException {
        try{
            log.info("Getting all taken researchers");
            List<ResearcherEntity> researcherEntities = analyticResearchRepository.findAllByIsTaken(true);
            List<ResearcherDto> researcherDtos = null;
            for (ResearcherEntity researcherEntity : researcherEntities) {
                researcherDtos.add(modelMapper.map(researcherEntity, ResearcherDto.class));
            }
            log.info("All taken researchers retrieved successfully");
            return researcherDtos;
        } catch (Exception e) {
            log.error("Error while getting all taken researchers");
            throw new CustomException(new ResponseMessageDto("Error while getting all taken researchers", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void healthDataToCsv(Long id, String fileName) throws CustomException{
        try{

            Optional<ResearcherEntity> researcher = analyticResearchRepository.findById(id);
            if (researcher.isEmpty() || !researcher.get().getIsValid())
                throw new CustomException(new ResponseMessageDto("Researcher with id: " + id + " does not exist", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            List<HealthRecordDto> healthRecordsList = patientServiceFeignClient.getAllHealthRecords().getBody();
            if (healthRecordsList == null)
                throw new CustomException(new ResponseMessageDto("No health records found", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);


            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(fileName)))){
                String[] header = {
                        "Patient ID", "Checkup Date", "Height (cm)", "Weight (kg)", "Blood Pressure",
                        "Blood Sugar", "Body Temperature", "Pulse Rate", "Allergies", "Past Surgeries",
                        "Has Diabetes", "Is Hypertensive", "Has Heart Disease", "Has Kidney Disease",
                        "Has Liver Disease", "Has Cancer", "Has HIV", "Has TB", "Physical Disability",
                        "Vaccine Info", "Is Smoker", "Is Alcoholic", "Is Active"
                };

                csvWriter.writeNext(header);

                for (HealthRecordDto userHealthRecordDto : healthRecordsList){
                    String[] data = {
                            userHealthRecordDto.getPatientId(),
                            String.valueOf(userHealthRecordDto.getCheckupDate()),
                            String.valueOf(userHealthRecordDto.getHeightInCm()),
                            String.valueOf(userHealthRecordDto.getWeightInKg()),
                            String.valueOf(userHealthRecordDto.getBloodPressure()),
                            String.valueOf(userHealthRecordDto.getBloodSugar()),
                            String.valueOf(userHealthRecordDto.getBodyTemperature()),
                            String.valueOf(userHealthRecordDto.getPulseRate()),
                            userHealthRecordDto.getAllergies(),
                            userHealthRecordDto.getPastSurgeries(),
                            String.valueOf(userHealthRecordDto.getHasDiabetes()),
                            String.valueOf(userHealthRecordDto.getIsHypertensive()),
                            String.valueOf(userHealthRecordDto.getHasHeartDisease()),
                            String.valueOf(userHealthRecordDto.getHasKidneyDisease()),
                            String.valueOf(userHealthRecordDto.getHasLiverDisease()),
                            String.valueOf(userHealthRecordDto.getHasCancer()),
                            String.valueOf(userHealthRecordDto.getHasHiv()),
                            String.valueOf(userHealthRecordDto.getHasTb()),
                            userHealthRecordDto.getPhysicalDisability(),
                            userHealthRecordDto.getVaccineInfo(),
                            String.valueOf(userHealthRecordDto.getIsSmoker()),
                            String.valueOf(userHealthRecordDto.getIsAlcoholic()),
                            String.valueOf(userHealthRecordDto.getIsActive())
                    };
                    csvWriter.writeNext(data);
                }
                ResearcherEntity researcherEntity = researcher.get();
                researcherEntity.setIsTaken(true);
                analyticResearchRepository.save(researcherEntity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void giveAccessToResearcher(Long id) throws CustomException {
        try {
            if (analyticResearchRepository.findByIdAndIsValidIsFalse(id) == null) {
                log.error("Researcher with id: {} does not exist", id);
                throw new CustomException(new ResponseMessageDto("Researcher with id: " + id + " does not exist", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            Optional<ResearcherEntity> researcher = analyticResearchRepository.findById(id);
            if (researcher.isEmpty())
                throw new RuntimeException("Researcher is not found");
            ResearcherEntity researcherEntity = researcher.get();
            researcherEntity.setIsValid(true);
            analyticResearchRepository.save(researcherEntity);
        } catch (Exception e) {
            log.error("Error while giving access to researcher with id: {}", id);
            throw new CustomException(new ResponseMessageDto("Error while giving access to researcher with id: " + id, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
