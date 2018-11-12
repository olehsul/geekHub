package com.owu.geekhub.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface MailService {
    void send(String email, String Message) throws MessagingException;
}
