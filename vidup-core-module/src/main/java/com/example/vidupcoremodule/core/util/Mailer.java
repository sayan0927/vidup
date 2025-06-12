package com.example.vidupcoremodule.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.mail.SimpleMailMessage;

@Service
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String recipient, String subject, String body) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }


}
