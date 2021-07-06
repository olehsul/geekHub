package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.exception.GenericGeekhubException;
import com.owu.geekhub.exception.UserNotFoundException;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.EmailService;
import com.owu.geekhub.service.UserActivationService;
import com.owu.geekhub.util.RandomVerificationNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@Slf4j
public class UserActivationServiceImpl implements UserActivationService {

    private final EmailService emailService;
    private final UserDao userDao;
    private final RandomVerificationNumber randomVerificationNumber;


    @Autowired
    public UserActivationServiceImpl(
            EmailService emailService,
            UserDao userDao,
            RandomVerificationNumber randomVerificationNumber) {
        this.emailService = emailService;
        this.userDao = userDao;
        this.randomVerificationNumber = randomVerificationNumber;
    }

    @Override
    public void sendTemporaryCode(User user) throws GenericGeekhubException {

        log.debug(user.toString());
        if (!userDao.existsByUsername(user.getUsername()))
            throw new UserNotFoundException("User " + user.getUsername() + "was not found.");
        int verificationNumber = randomVerificationNumber.getRandomVerificationNumber();
        try {
            emailService.prepareAndSend(
                    user.getUsername(),
                    "Geekhub temporary code",
                    Integer.toString(verificationNumber));
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new GenericGeekhubException("Error sending the email to " + user.getUsername());
        }
        user.setActivationKey(Integer.toString(verificationNumber));
        userDao.save(user);

    }

    @Override
    public boolean verifyCodeAndActivateUser(User user, String code) {
        if (user.getActivationKey().equals(code)) {
            user.setActivated(true);
            userDao.save(user);
            return true;
        }
        return false;
    }
}
