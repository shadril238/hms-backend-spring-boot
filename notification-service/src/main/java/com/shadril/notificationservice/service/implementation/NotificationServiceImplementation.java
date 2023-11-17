package com.shadril.notificationservice.service.implementation;

import com.shadril.notificationservice.dto.NotificationDto;
import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.dto.UserDto;
import com.shadril.notificationservice.entity.NotificationEntity;
import com.shadril.notificationservice.entity.NotificationPreferenceEntity;
import com.shadril.notificationservice.enums.PreferenceType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        try {
            log.info("inside createNotification method of NotificationServiceImplementation class");

            UserDto user = securityServiceFeignClient.getUserById(notificationDto.getUserId()).getBody();
            if (user == null) {
                throw new CustomException(new ResponseMessageDto("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            NotificationEntity notificationEntity = modelMapper.map(notificationDto, NotificationEntity.class);
            notificationEntity.setIsRead(false);
            notificationEntity.setTimestamp(LocalDateTime.now());
            notificationRepository.save(notificationEntity);
            log.info("notification entity saved successfully for user id: {}", notificationEntity.getUserId());

            // Check if user has email notification preference enabled and send email
            Optional<NotificationPreferenceEntity> emailPreference = notificationPreferenceRepository
                    .findByUserIdAndPrefType(user.getId(), String.valueOf(PreferenceType.Email_Notification));

            if (emailPreference.isPresent() && emailPreference.get().isEnabled()) {
                String email = user.getEmail();
                String subject = "New Notification from HMS : " + notificationDto.getNotificationType();
                String body = notificationDto.getNotificationText();
                emailSenderService.sendEmail(email, subject, body);
                log.info("Email notification sent successfully to user id: {}", user.getId());
            }
        } catch (Exception e) {
            log.error("error occurred while saving notification entity for user id: {}", notificationDto.getUserId());
            throw new CustomException(new ResponseMessageDto("Notification could not be saved", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public List<NotificationDto> getAllNotificationsByUserId(Long userId) throws CustomException {
        try {
            log.info("Fetching all notifications for user ID: {}", userId);

            List<NotificationEntity> notificationEntities = notificationRepository.findAllByUserId(userId);
            if (notificationEntities.isEmpty()) {
                throw new CustomException(new ResponseMessageDto("No notifications found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            List<NotificationDto> notifications = notificationEntities.stream()
                    .map(entity -> modelMapper.map(entity, NotificationDto.class))
                    .toList();

            log.info("Successfully fetched {} notifications for user ID: {}", notifications.size(), userId);
            return notifications;
        } catch (Exception e) {
            log.error("Error occurred while fetching notifications for user ID: {}", userId, e);
            throw new CustomException(new ResponseMessageDto("Error fetching notifications", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public List<NotificationDto> getUnreadNotificationsByUserId(Long userId) throws CustomException {
        try {
            log.info("Fetching unread notifications for user ID: {}", userId);

            List<NotificationEntity> notificationEntities = notificationRepository.findUnreadByUserId(userId);
            if (notificationEntities.isEmpty()) {
                log.info("No unread notifications found for user ID: {}", userId);
                return new ArrayList<>(); // Return an empty list or handle as per your design
            }

            List<NotificationDto> notificationDtos = notificationEntities.stream()
                    .map(entity -> modelMapper.map(entity, NotificationDto.class))
                    .collect(Collectors.toList());

            log.info("Successfully fetched {} unread notifications for user ID: {}", notificationDtos.size(), userId);
            return notificationDtos;
        } catch (Exception e) {
            log.error("Error occurred while fetching unread notifications for user ID: {}", userId, e);
            throw new CustomException(new ResponseMessageDto("Error fetching unread notifications", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void markNotificationAsRead(Long notificationId) throws CustomException {
        try {
            log.info("Marking notification as read for notification ID: {}", notificationId);

            NotificationEntity notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new CustomException(new ResponseMessageDto("Notification not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));

            notification.setIsRead(true);
            notificationRepository.save(notification);

            log.info("Notification marked as read for notification ID: {}", notificationId);
        } catch (Exception e) {
            log.error("Error occurred while marking notification as read for notification ID: {}", notificationId, e);
            throw new CustomException(new ResponseMessageDto("Error marking notification as read", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void markAllNotificationsAsRead(Long userId) throws CustomException {
        try {
            log.info("Marking all notifications as read for user ID: {}", userId);

            List<NotificationEntity> notifications = notificationRepository.findAllByUserId(userId);
            notifications.forEach(notification -> notification.setIsRead(true));
            notificationRepository.saveAll(notifications);

            log.info("All notifications marked as read for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error occurred while marking all notifications as read for user ID: {}", userId, e);
            throw new CustomException(new ResponseMessageDto("Error marking all notifications as read", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void deleteNotification(Long notificationId) throws CustomException {
        try {
            log.info("Deleting notification for notification ID: {}", notificationId);

            if (!notificationRepository.existsById(notificationId)) {
                throw new CustomException(new ResponseMessageDto("Notification not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            notificationRepository.deleteById(notificationId);

            log.info("Notification deleted for notification ID: {}", notificationId);
        } catch (Exception e) {
            log.error("Error occurred while deleting notification for notification ID: {}", notificationId, e);
            throw new CustomException(new ResponseMessageDto("Error deleting notification", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public void updateNotification(NotificationDto notificationDto) throws CustomException {
        try {
            log.info("Updating notification for notification ID: {}", notificationDto.getNotificationId());

            NotificationEntity notification = notificationRepository.findById(notificationDto.getNotificationId())
                    .orElseThrow(() -> new CustomException(new ResponseMessageDto("Notification not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));

            modelMapper.map(notificationDto, notification);
            notificationRepository.save(notification);

            log.info("Notification updated for notification ID: {}", notificationDto.getNotificationId());
        } catch (Exception e) {
            log.error("Error occurred while updating notification for notification ID: {}", notificationDto.getNotificationId(), e);
            throw new CustomException(new ResponseMessageDto("Error updating notification", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
