package com.owu.geekhub.service.impl;

import com.owu.geekhub.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender sender;

    @Autowired
    public EmailServiceImpl(@Qualifier("geekMailSender") JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void prepareAndSend(String recipient, String subject, String body) {

        MimeMessagePreparator message = newMessage -> {
            newMessage.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(recipient)
            );
            newMessage.setFrom("geekhub.owu@gmail.com");
            newMessage.setSubject(subject);
            newMessage.setText(body);
        };

        this.sender.send(message);
    }


}
