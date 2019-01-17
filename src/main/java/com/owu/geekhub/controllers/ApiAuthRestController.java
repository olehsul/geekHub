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
import com.owu.geekhub.service.generators.RandomUserIdentity;
import com.owu.geekhub.service.validation.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

        System.out.println("INSIDE SIGNIN_____________________");

        System.out.println(loginRequest);


        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        System.out.println("INSIDE SIGNIN 2");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) throws ParseException, MessagingException {


        System.out.println("inside signUp");
        System.out.println(signUpRequest);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = java.sql.Date.valueOf(java.time.LocalDate.now());
        java.util.Date date = dateFormat.parse(signUpRequest.getDate());
        java.sql.Date birthDate = new java.sql.Date(date.getTime());

        if ((!registrationValidator.isDateValid(signUpRequest.getDate()))|| (date.after(currentDate))) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Date is invalid!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userDao.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userDao.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    @PostMapping("/get-verification-code")
    public ResponseEntity<?> getVerificationCode(@RequestParam("username") String username,
                                    @RequestParam("code") Integer code){
        System.out.println("inside get ver code--------------");
        System.out.println("==================" + username + " --- " + code);
        return new ResponseEntity<>(new ResponseMessage("Inside send code!!!"),
                HttpStatus.BAD_REQUEST);
    }
}
