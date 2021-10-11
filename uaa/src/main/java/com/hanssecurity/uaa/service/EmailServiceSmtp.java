package com.hanssecurity.uaa.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author hans
 */
@Slf4j
@Service
@Setter
@RequiredArgsConstructor
// hans.sms-provider.name = leancloud
@ConditionalOnProperty(prefix = "hans.email-provider", name="name", havingValue = "smtp")
public class EmailServiceSmtp implements SmsService{

    private final JavaMailSender emailSender;

    @Override
    public void send(String email, String msg) {
        val message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("hans@hans.com");
        message.setSubject("Login valid code");
        message.setText("code: " + msg);
        emailSender.send(message);
    }
}
