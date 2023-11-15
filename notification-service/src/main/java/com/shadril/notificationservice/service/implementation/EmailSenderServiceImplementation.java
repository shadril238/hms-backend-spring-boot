package com.shadril.notificationservice.service.implementation;

import com.shadril.notificationservice.dto.ResponseMessageDto;
import com.shadril.notificationservice.exception.CustomException;
import com.shadril.notificationservice.service.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderServiceImplementation implements EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String emailTo, String subject, String messageBody)
            throws CustomException {
        try {
            log.info("inside sendEmail method of EmailSenderServiceImplementation");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(messageBody);
            log.info("sending email to {}", emailTo);
            javaMailSender.send(message);
            log.info("email sent successfully to {}", emailTo);
        } catch (Exception e) {
            log.error("error sending email to {}", emailTo);
            throw new CustomException(new ResponseMessageDto("Error sending email", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}
