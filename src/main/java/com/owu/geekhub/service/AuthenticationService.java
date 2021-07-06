package com.owu.geekhub.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    ResponseEntity attemptLoginAndGetResponse(String username, String password);

    boolean changePassword(String username, String code, String newPassword);
}
