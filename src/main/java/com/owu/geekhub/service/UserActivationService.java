package com.owu.geekhub.service;

import com.owu.geekhub.exception.GenericGeekhubException;
import com.owu.geekhub.exception.UserNotFoundException;
import com.owu.geekhub.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserActivationService {

    void sendTemporaryCode(User user) throws GenericGeekhubException;
    boolean verifyCodeAndActivateUser(User user, String code);

}
