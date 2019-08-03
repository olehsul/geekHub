package com.owu.geekhub.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    ResponseEntity attemptLoginAndGetResponse(String username, String password);

    boolean matchVerificationCode(String username, int parseInt);

    boolean changePassword(String username, int code, String newPassword);
}
