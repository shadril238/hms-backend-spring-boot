package com.shadril.notificationservice.controller;

import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.service.TwilioSmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class SmsController {
    @Autowired
    private TwilioSmsSender twilioSmsSender;

    @PostMapping("/sms")
    public ResponseEntity<ResponseMessageDto> sendSms(@RequestParam("phoneNumberTo") String phoneNumberTo,
                                                      @RequestParam("message") String message) throws CustomException {
        twilioSmsSender.sendSms(phoneNumberTo, message);
        return new ResponseEntity<>(new ResponseMessageDto("SMS notification sent successfully", HttpStatus.OK), HttpStatus.OK);
    }
}
