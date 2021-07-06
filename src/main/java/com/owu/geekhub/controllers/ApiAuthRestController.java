package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.SendCodeRequest;
import com.owu.geekhub.models.User;
import com.owu.geekhub.jwtmessage.request.LoginForm;
import com.owu.geekhub.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.owu.geekhub.service.AuthenticationService;
import com.owu.geekhub.jwtmessage.request.SignUpForm;
import com.owu.geekhub.util.UserRegistrationValidator;
import com.owu.geekhub.jwtmessage.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthRestController {

    private final UserDao userDao;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserRegistrationValidator userRegistrationValidator;

    @Autowired
    public ApiAuthRestController(UserDao userDao,
                                 UserService userService,
                                 AuthenticationService authenticationService,
                                 UserRegistrationValidator userRegistrationValidator) {
        this.userDao = userDao;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userRegistrationValidator = userRegistrationValidator;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        return authenticationService.attemptLoginAndGetResponse(loginRequest.getUsername(),
                loginRequest.getPassword());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest)
            throws ParseException, MessagingException {
        if (!userRegistrationValidator.isDateValid(signUpRequest.getDate())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Invalid birth date!"));
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = dateFormat.parse(signUpRequest.getDate());
        java.sql.Date birthDate = new java.sql.Date(date.getTime());

        User user = User.builder()
                .firstName(signUpRequest.getFirstname())
                .lastName(signUpRequest.getLastname())
                .username(signUpRequest.getUsername())
                .password(signUpRequest.getPassword())
                .gender(signUpRequest.getGender())
                .birthDate(birthDate)
                .build();
        return userService.save(user);
    }

    @PostMapping("/get-verification-code")
    public ResponseEntity<?> getVerificationCode(@RequestBody SendCodeRequest sendCodeRequest) {
        log.debug(sendCodeRequest.toString());

        return userService.activateUser(sendCodeRequest.getUsername(), sendCodeRequest.getCode());
    }

    @PostMapping("/send-code-to-email")
    public ResponseEntity<?> sendCodeToEmail(@RequestBody String username) {
        if (!userDao.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage("User with such email does not exist!"));
        }
        return userService.sendNewActivationCode(username);
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<ResponseMessage> setNewPassword(@RequestParam String username,
                                         @RequestParam String code,
                                         @RequestParam String newPassword) {
        if (authenticationService.changePassword(username, code, newPassword)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Password was not changed"));
        }
    }
}