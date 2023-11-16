package com.shadril.notificationservice.service.implementation;

import com.shadril.notificationservice.dto.NotificationDto;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.repository.NotificationPreferenceRepository;
import com.shadril.notificationservice.repository.NotificationRepository;
import com.shadril.notificationservice.service.EmailSenderService;
import com.shadril.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    public void createNotification(NotificationDto notificationDto)
            throws CustomException {

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
