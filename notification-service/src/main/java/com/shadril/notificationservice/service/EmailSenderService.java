package com.shadril.notificationservice.service;

import com.shadril.notificationservice.exception.CustomException;

public interface EmailSenderService {
    void sendEmail(String emailTo, String subject, String message) throws CustomException;
}
