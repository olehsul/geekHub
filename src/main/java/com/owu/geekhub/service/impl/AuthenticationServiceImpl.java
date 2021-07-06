package com.owu.geekhub.service.impl;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.jwtmessage.response.JwtResponse;
import com.owu.geekhub.models.User;
import com.owu.geekhub.security.jwt.JwtProvider;
import com.owu.geekhub.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserDao userDao;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;

    public AuthenticationServiceImpl(UserDao userDao, AuthenticationManager authenticationManager,
                                     JwtProvider jwtProvider, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.encoder = encoder;
    }

    @Override
    public ResponseEntity<JwtResponse> attemptLoginAndGetResponse(String username, String password) {
        if (!userDao.existsByUsername(username)) {
            String message = String.format("User [%s] does not exist!", username);
            logger.warn(message);
            return ResponseEntity.notFound().build();
        }
        if (!userDao.findByUsername(username).isActivated()) {
            String message = String.format("User [%s] is not activated!", username);
            logger.warn(message);
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .build();
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            logger.warn(String.format("Wrong passwords for [%s]", username),
                    e.getCause());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @Override
    public boolean changePassword(String username, String code, String newPassword) {
        User user = userDao.findByUsername(username);
        if (user.getActivationKey().equals(code)) {
            user.setPassword(encoder.encode(newPassword));
            userDao.save(user);
            return true;
        } else {
            return false;
        }
    }
}
