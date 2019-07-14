package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.jwtmessage.request.LoginForm;
import com.owu.geekhub.jwtmessage.request.SignUpForm;
import com.owu.geekhub.jwtmessage.response.JwtResponse;
import com.owu.geekhub.jwtmessage.response.ResponseMessage;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.security.jwt.JwtProvider;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.generators.RandomUserIdentity;
import com.owu.geekhub.service.validation.RegistrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class ApiAuthRestController {
    private static Logger logger = LoggerFactory.getLogger(ApiAuthRestController.class);
    private final
    AuthenticationManager authenticationManager;

    private final UserDao userDao;

    private final UserService userService;

    private final
    PasswordEncoder encoder;

    private final
    JwtProvider jwtProvider;

    private final RandomUserIdentity randomUserIdentity;

    private final RegistrationValidator registrationValidator;

    private final MailService mailService;

    public ApiAuthRestController(AuthenticationManager authenticationManager, UserDao userDao, PasswordEncoder encoder,
                                 JwtProvider jwtProvider, RandomUserIdentity randomUserIdentity,
                                 RegistrationValidator registrationValidator, MailService mailService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
        this.randomUserIdentity = randomUserIdentity;
        this.registrationValidator = registrationValidator;
        this.mailService = mailService;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        logger.info(loginRequest.toString());
        if (!userDao.existsByUsername(loginRequest.getUsername())) {
            logger.warn("User not found!");
            return new ResponseEntity<>(
                    new JwtResponse(null, null, null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!userDao.findByUsername(loginRequest.getUsername()).isActivated()) {
            logger.warn("User is not activated");
            return new ResponseEntity<>(
                    new JwtResponse(null, loginRequest.getUsername(), null, HttpStatus.LOCKED), HttpStatus.LOCKED);
        }
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            logger.warn("password is wrong");
            return new ResponseEntity<>(
                    new JwtResponse(null, loginRequest.getUsername(), null, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) throws ParseException, MessagingException {
        if (!registrationValidator.isDateValid(signUpRequest.getDate())) {
            return new ResponseEntity<>(new ResponseMessage("Invalid birth date!"),
                    HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> getVerificationCode(@RequestBody Map<String, String> params) {

        User user = userDao.findByUsername(params.get("username"));
        int code = Integer.parseInt(params.get("code"));
        if (user.getActivationKey() == code) {
            System.out.println("Code equals!!!!");
            user.setActivated(true);
            userDao.save(user);
            return new ResponseEntity<>(new ResponseMessage("Code matches", HttpStatus.OK),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Code does not matches", HttpStatus.BAD_REQUEST),
                    HttpStatus.OK);

        }
    }

    @PostMapping("/get-password-reset-code")
    public ResponseEntity<?> getPasswordResetCode(@RequestBody Map<String, String> params) throws MessagingException {
        System.out.println("inside pass reset");
        User user = userDao.findByUsername(params.get("username"));
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            return ResponseEntity.ok().body(response);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "foo");
        mailService.sendRecoveryCode(params.get("username"));
        System.out.println("Code sent");
        return new ResponseEntity<>(
                new ResponseMessage("Code sent"), headers, HttpStatus.OK);
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<?> setNewPassword(@RequestBody Map<String, String> params) {
        System.out.println(params.get("code"));
        System.out.println(params.get("newPassword"));
        System.out.println(params.get("username"));
        User user = userDao.findByUsername(params.get("username"));
        if (user.getActivationKey() == Integer.parseInt(params.get("code"))) {
            String password = encoder.encode(params.get("newPassword"));
            user.setPassword(password);
            userDao.save(user);
            return new ResponseEntity<>(
                    new ResponseMessage("Password was changed"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Password was not changed"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}