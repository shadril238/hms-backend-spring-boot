package com.shadril.notificationservice.service.implementation;

import com.shadril.notificationservice.dto.NotificationPreferenceDto;
import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.entity.NotificationPreferenceEntity;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.repository.NotificationPreferenceRepository;
import com.shadril.notificationservice.service.NotificationPreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationPreferenceServiceImplementation implements NotificationPreferenceService {
    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createNotificationPreference(NotificationPreferenceDto notificationPreferenceDto)
            throws CustomException {
        try{
            log.info("inside createNotificationPreference method of NotificationPreferenceServiceImplementation class");

            NotificationPreferenceEntity notificationPreferenceEntity = modelMapper.map(notificationPreferenceDto, NotificationPreferenceEntity.class);
            notificationPreferenceRepository.save(notificationPreferenceEntity);

            log.info("notification preference entity saved successfully for user id: {}", notificationPreferenceEntity.getUserId());
        } catch (Exception e){
            log.error("error occurred while saving notification preference entity for user id: {}", notificationPreferenceDto.getUserId());
            throw new CustomException(new ResponseMessageDto("Notification preference could not be saved", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NotificationPreferenceDto getNotificationPreferenceByPreferenceType(Long userId, String preferenceType) throws CustomException {
        try{
            log.info("inside getNotificationPreferenceByPreferenceType method of NotificationPreferenceServiceImplementation class");
            Optional<NotificationPreferenceEntity> entity = notificationPreferenceRepository.findByUserIdAndPrefType(userId, preferenceType);
            if(entity.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Notification preference not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            log.info("notification preference entity fetched successfully for user id: {}", userId);
            return modelMapper.map(entity.get(), NotificationPreferenceDto.class);
        } catch (Exception e){
            log.error("error occurred while fetching notification preference entity for user id: {}", userId);
            throw new CustomException(new ResponseMessageDto("Notification preference could not be fetched", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<NotificationPreferenceDto> getAllNotificationPreferencesByUserId(Long userId) throws CustomException {
        try{
            log.info("inside getAllNotificationPreferencesByUserId method of NotificationPreferenceServiceImplementation class");

            List<NotificationPreferenceEntity> entities = notificationPreferenceRepository.findAllByUserId(userId);
            if (entities.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("Notification preferences not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            log.info("notification preference entities fetched successfully for user id: {}", userId);
            return entities.stream()
                    .map(entity -> modelMapper.map(entity, NotificationPreferenceDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e){
            log.error("error occurred while fetching notification preference entities for user id: {}", userId);
            throw new CustomException(new ResponseMessageDto("Notification preferences could not be fetched", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateNotificationPreference(NotificationPreferenceDto notificationPreferenceDto) throws CustomException {
        //  will be implemented later
    }

    @Override
    public void deleteNotificationPreference(Long notificationPreferenceId) throws CustomException {
        //  will be implemented later
    }
}
