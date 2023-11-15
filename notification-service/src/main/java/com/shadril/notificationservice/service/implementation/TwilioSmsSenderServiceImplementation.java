package com.shadril.notificationservice.service.implementation;

import com.shadril.notificationservice.config.TwilioConfiguration;
import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.service.TwilioSmsSenderService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioSmsSenderServiceImplementation implements TwilioSmsSenderService {
    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwilioSmsSenderServiceImplementation(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
        Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
    }
    @Override
    public void sendSms(String phoneNumberTo, String message)
            throws CustomException {
        try {
            log.info("inside sendSms method of TwilioSmsSenderServiceImplementation");
            Message.creator(new PhoneNumber(phoneNumberTo), new PhoneNumber(twilioConfiguration.getFromNumber()), message).create();
            log.info("sms sent successfully to {}", phoneNumberTo);
        } catch (Exception e) {
            log.error("error sending sms to {}", phoneNumberTo);
            throw new CustomException(new ResponseMessageDto("Error sending sms", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
