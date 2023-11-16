package com.shadril.notificationservice.service.implementation;

import com.shadril.notificationservice.dto.NotificationDto;
import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.dto.UserDto;
import com.shadril.notificationservice.entity.NotificationEntity;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.networkmanager.SecurityServiceFeignClient;
import com.shadril.notificationservice.repository.NotificationPreferenceRepository;
import com.shadril.notificationservice.repository.NotificationRepository;
import com.shadril.notificationservice.service.EmailSenderService;
import com.shadril.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificationServiceImplementation implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SecurityServiceFeignClient securityServiceFeignClient;


    @Override
    public void createNotification(NotificationDto notificationDto)
            throws CustomException {
        try{
            log.info("inside createNotification method of NotificationServiceImplementation class");
            UserDto user = securityServiceFeignClient.getUserById(notificationDto.getUserId()).getBody();
            if(user == null) {
                throw new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
            NotificationEntity notificationEntity = modelMapper.map(notificationDto, NotificationEntity.class);
            notificationEntity.setIsRead(false);
            notificationEntity.setTimestamp(LocalDateTime.now());
            notificationRepository.save(notificationEntity);
            log.info("notification entity saved successfully for user id: {}", notificationEntity.getUserId());

            // Todo: check if user has email notification preference enabled
            // send email
            String email = user.getEmail();
            String subject = "New Notification from HMS : " + notificationDto.getNotificationType();
            String body = notificationDto.getNotificationText();
            emailSenderService.sendEmail(email, subject, body);
        } catch (Exception e){
            log.error("error occurred while saving notification entity for user id: {}", notificationDto.getUserId());
            throw new CustomException(new ResponseMessageDto("Notification could not be saved", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public List<NotificationDto> getAllNotificationsByUserId(Long userId) throws CustomException {
        return null;
    }

    @Override
    public List<NotificationDto> getUnreadNotificationsByUserId(Long userId) throws CustomException {
        return null;
    }

    @Override
    public void markNotificationAsRead(Long notificationId) throws CustomException {

    }

    @Override
    public void markAllNotificationsAsRead(Long userId) throws CustomException {

    }

    @Override
    public void deleteNotification(Long notificationId) throws CustomException {

    }

    @Override
    public void updateNotification(NotificationDto notificationDto) throws CustomException {

    }
}
