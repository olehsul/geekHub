package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.jwtmessage.request.LoginForm;
import com.owu.geekhub.jwtmessage.request.SignUpForm;
import com.owu.geekhub.jwtmessage.response.ResponseMessage;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.AuthenticationService;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.validation.RegistrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthRestController {
    private static final Logger logger = LoggerFactory.getLogger(ApiAuthRestController.class);

    private final UserDao userDao;
    private final UserService userService;
    private final MailService mailService;
    private final RegistrationValidator registrationValidator;
    private final AuthenticationService authenticationService;

    public ApiAuthRestController(UserDao userDao, RegistrationValidator registrationValidator,
                                 MailService mailService, UserService userService,
                                 AuthenticationService authenticationService) {
        this.userDao = userDao;
        this.registrationValidator = registrationValidator;
        this.mailService = mailService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        return authenticationService.attemptLoginAndGetResponse(loginRequest.getUsername(),
                loginRequest.getPassword());
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignUpForm signUpRequest)
            throws ParseException, MessagingException {
        if (!registrationValidator.isDateValid(signUpRequest.getDate())) {
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
    public ResponseEntity getVerificationCode(@RequestParam String username, @RequestParam String code) {
        if (code.matches("[0-9]+")
                && authenticationService.matchVerificationCode(username, Integer.parseInt(code))) {
            return ResponseEntity.ok()
                    .body(new ResponseMessage("Code matches"));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Code does not matches"));

        }
    }

    @PostMapping("/send-code-to-email")
    public ResponseEntity sendCodeToEmail(@RequestBody String username) throws MessagingException {
        if (!userDao.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage("User with such email does not exist!"));
        }
        mailService.sendRecoveryCode(username);
        return ResponseEntity.ok(new ResponseMessage("Code sent"));
    }

    @PostMapping("/set-new-password")
    public ResponseEntity setNewPassword(@RequestParam String username,
                                         @RequestParam String code,
                                         @RequestParam String newPassword) {
        if (authenticationService.changePassword(username, Integer.parseInt(code), newPassword)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Password was not changed"));
        }
    }
}