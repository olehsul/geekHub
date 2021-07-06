package com.owu.geekhub.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface EmailService {

    void prepareAndSend(String recipient, String subject, String body) throws MessagingException;

}
