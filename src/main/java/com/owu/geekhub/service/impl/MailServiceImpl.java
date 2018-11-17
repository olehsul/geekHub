package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.generators.RandomVerificationNumber;
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
    private RandomVerificationNumber randomVerificationNumber;

    @Autowired
    private UserDao userDao;

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

     public void send(String email) throws MessagingException {
        User user = userDao.findByUsername(email);
//        if (user.isActivated()) {
//            System.out.println(" user " + user.getUsername() + " is already activated");
//            return;
//        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        int verificationNumber = randomVerificationNumber.getRandomVerifictionNumber();

        user.setActivationKey(verificationNumber);
        userService.update(user);

        try {
            mimeMessage.setFrom(new InternetAddress(env.getProperty("email.username")));
            helper.setTo(email);
            helper.setText(Integer.toString(verificationNumber),true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendActivationKey(String email) throws MessagingException {
        User user = userDao.findByUsername(email);
        if (user.isActivated()) {
            System.out.println(" user " + user.getUsername() + " is already activated");
            return;
        }
        send(email);
    }
    public void sendRecoveryCode(String email){
        User user = userDao.findByUsername(email);




    }
}
