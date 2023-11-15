package com.shadril.notificationservice.service;

import com.shadril.notificationservice.exception.CustomException;

public interface TwilioSmsSenderService {
    void sendSms(String phoneNumberTo, String message) throws CustomException;
}
