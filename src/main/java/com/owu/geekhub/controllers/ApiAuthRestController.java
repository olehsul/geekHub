package com.owu.geekhub.controllers;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.jwtmessage.request.LoginForm;
import com.owu.geekhub.jwtmessage.request.SignUpForm;
import com.owu.geekhub.jwtmessage.response.JwtResponse;
import com.owu.geekhub.jwtmessage.response.ResponseMessage;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import com.owu.geekhub.models.http.PasswordResetResponse;
import com.owu.geekhub.security.jwt.JwtProvider;
import com.owu.geekhub.service.MailService;
import com.owu.geekhub.service.generators.RandomUserIdentity;
import com.owu.geekhub.service.validation.RegistrationValidator;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserDao userDao;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private RandomUserIdentity randomUserIdentity;

    @Autowired
    private RegistrationValidator registrationValidator;

    @Autowired
    private MailService mailService;



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {


        System.out.println(loginRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "foo");
        if (!userDao.existsByUsername(loginRequest.getUsername())) {
            System.out.println("User not found");
//            return ResponseEntity.ok().body(new JwtResponse(null, null, null));
            return new ResponseEntity<>(
                    new JwtResponse(null, null, null, HttpStatus.NOT_FOUND), headers, HttpStatus.NOT_FOUND);
        }
        if (!userDao.findByUsername(loginRequest.getUsername()).isActivated()) {
            System.out.println("User is not activated");
            return new ResponseEntity<>(
                    new JwtResponse(null, loginRequest.getUsername(), null, HttpStatus.LOCKED), headers, HttpStatus.LOCKED);
        }
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println("password is wrong");
            e.printStackTrace();
            return new ResponseEntity<>(
                    new JwtResponse(null, loginRequest.getUsername(), null, HttpStatus.UNAUTHORIZED), headers, HttpStatus.UNAUTHORIZED);
        }

        System.out.println("INSIDE SIGNIN 2");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new ResponseEntity<>(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) throws ParseException, MessagingException {


        System.out.println("inside signUp");
        System.out.println(signUpRequest);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = java.sql.Date.valueOf(java.time.LocalDate.now());
        java.util.Date date = dateFormat.parse(signUpRequest.getDate());
        java.sql.Date birthDate = new java.sql.Date(date.getTime());

        if ((!registrationValidator.isDateValid(signUpRequest.getDate())) || (date.after(currentDate))) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Date is invalid!", HttpStatus.BAD_REQUEST),
                    HttpStatus.OK);
        }

        if (userDao.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!", HttpStatus.IM_USED),
                    HttpStatus.OK);
        }

        User user = User.builder()
                .firstName(signUpRequest.getFirstname())
                .lastName(signUpRequest.getLastname())
                .username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword()))
                .gender(signUpRequest.getGender())
                .birthDate(birthDate)
                .build();

        randomUserIdentity.setRandomId(user);

//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        strRoles.forEach(role -> {
//            switch (role) {
//                case "admin":
//                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
//                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
//                    roles.add(adminRole);
//
//                    break;
//                case "pm":
//                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
//                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
//                    roles.add(pmRole);
//
//                    break;
//                default:
//                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
//                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
//                    roles.add(userRole);
//            }
//        });
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setRole(Role.ROLE_USER);
        userDao.save(user);
        mailService.sendActivationKey(user.getUsername());
        return new ResponseEntity<>(new ResponseMessage("User registered successfully!", HttpStatus.OK), HttpStatus.OK);
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
        Map<String, String> response = new HashMap<String, String>();
//        if (user == null) {
//            return new ResponseEntity<>(new ResponseMessage("user not found"),
//                    HttpStatus.OK);
//        }
        if (user == null) {
            return ResponseEntity.ok().body(response);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "foo");
        mailService.sendRecoveryCode(params.get("username"));
        System.out.println("Code sent");
        return new ResponseEntity<>(
                new ResponseMessage("Code sent"), headers, HttpStatus.OK);
//        response.put("ok", "code sent");
//        response.put("bad", "code not sent");
//        return ResponseEntity.ok().body(response);

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