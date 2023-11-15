package com.shadril.notificationservice.service.implementation;

import com.shadril.notificationservice.config.TwilioConfiguration;
import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.service.TwilioSmsSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSenderImplementation implements TwilioSmsSender {
    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwilioSmsSenderImplementation(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
        Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
    }
    @Override
    public void sendSms(String phoneNumberTo, String message)
            throws CustomException {
        try {
            Message.creator(new PhoneNumber(phoneNumberTo), new PhoneNumber(twilioConfiguration.getFromNumber()), message).create();
        } catch (Exception e) {
            throw new CustomException(new ResponseMessageDto("Error sending sms", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
