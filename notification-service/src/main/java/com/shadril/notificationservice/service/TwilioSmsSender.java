package com.shadril.notificationservice.service;

import com.shadril.notificationservice.exception.CustomException;

public interface TwilioSmsSender {
    void sendSms(String phoneNumberTo, String message) throws CustomException;
}
