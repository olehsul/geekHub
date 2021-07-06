package com.owu.geekhub.service.impl;

import com.owu.geekhub.configs.TestConfig;
import com.owu.geekhub.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = TestConfig.class,
        loader = AnnotationConfigContextLoader.class
)
@TestPropertySource(locations = {"classpath:application.properties"})
class EmailServiceImplTest {

    @Autowired
//    @Qualifier("EmailServiceTest")
    private EmailService emailService;

//    @Autowired
//    @Qualifier("geekMailSenderTest")
//    private JavaMailSender javaMailSender;


    @Test
    void testSendEmail() throws MessagingException {

        emailService.prepareAndSend("olehsulmail@gmail.com", "test_email", "This is a test!!!");

    }

}