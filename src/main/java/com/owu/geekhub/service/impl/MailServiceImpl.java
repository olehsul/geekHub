package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.generators.RandomVerificationNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@PropertySource("classpath:application.properties")
@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private final JavaMailSender javaMailSender;

    private final RandomVerificationNumber randomVerificationNumber;

    private final UserDao userDao;

    private final Environment env;

    public MailServiceImpl(JavaMailSender javaMailSender, RandomVerificationNumber randomVerificationNumber,
                           UserDao userDao, Environment env) {
        this.javaMailSender = javaMailSender;
        this.randomVerificationNumber = randomVerificationNumber;
        this.userDao = userDao;
        this.env = env;
    }

    private void send(String email) throws MessagingException {
        User user = userDao.findByUsername(email);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        int verificationNumber = randomVerificationNumber.getRandomVerificationNumber();
        try {
            mimeMessage.setFrom(new InternetAddress(Objects.requireNonNull(env.getProperty("email.username"))));
            helper.setTo(email);
            helper.setText(Integer.toString(verificationNumber), true);
        } catch (MessagingException e) {
            logger.error("Mail sending failed: ", e.getCause());
        }
        user.setActivationKey(verificationNumber);
        javaMailSender.send(mimeMessage);
        userDao.save(user);
    }

    public void sendActivationKey(String email) throws MessagingException {
        send(email);
    }

    public void sendRecoveryCode(String email) throws MessagingException {
        send(email);
    }
}
