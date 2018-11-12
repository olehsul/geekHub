package com.owu.geekhub.service.impl;

import com.owu.geekhub.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@PropertySource("classpath:application.properties")
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private Environment env;

    public void send(String email, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("email.username")));
            helper.setTo(email);
            helper.setText(message,true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }
}
