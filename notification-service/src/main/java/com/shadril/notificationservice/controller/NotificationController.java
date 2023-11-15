package com.shadril.notificationservice.controller;

import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.service.EmailSenderService;
import com.shadril.notificationservice.service.TwilioSmsSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {
    @Autowired
    private TwilioSmsSenderService twilioSmsSenderService;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/sms")
    public ResponseEntity<ResponseMessageDto> sendSms(@RequestParam("phoneNumberTo") String phoneNumberTo,
                                                      @RequestParam("message") String message) throws CustomException {
        log.info("inside sendSms method of NotificationController");
        twilioSmsSenderService.sendSms(phoneNumberTo, message);
        return new ResponseEntity<>(new ResponseMessageDto("SMS notification sent successfully", HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/email")
    public ResponseEntity<ResponseMessageDto> sendEmail(@RequestParam("emailTo") String emailTo,
                                                        @RequestParam("subject") String subject,
                                                        @RequestParam("messageBody") String messageBody) throws CustomException {
        log.info("inside sendEmail method of NotificationController");
        emailSenderService.sendEmail(emailTo, subject, messageBody);
        return new ResponseEntity<>(new ResponseMessageDto("Email notification sent successfully", HttpStatus.OK), HttpStatus.OK);
    }
}
