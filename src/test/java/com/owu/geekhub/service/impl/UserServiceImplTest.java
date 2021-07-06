package com.owu.geekhub.service.impl;

import com.owu.geekhub.configs.TestConfig;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import com.owu.geekhub.service.UserActivationService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.util.RandomUserIdentity;
import com.owu.geekhub.util.RandomVerificationNumber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.mail.MessagingException;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = TestConfig.class,
        loader = AnnotationConfigContextLoader.class
)
@TestPropertySource(locations = {"classpath:application.properties"})
class UserServiceImplTest {

    @Autowired
    @Qualifier("userServiceMock")
    private UserService userService;

    @Autowired
    @Qualifier("passwordEncoderMock")
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("randomUserIdentityMock")
    private RandomUserIdentity randomUserIdentity;

    @Autowired
    @Qualifier("randomVerificationNumberMock")
    private RandomVerificationNumber randomVerificationNumber;

    @Autowired
    @Qualifier("userDaoMock")
    private UserDao userDao;

    @Autowired
    @Qualifier("userActivationServiceMock")
    private UserActivationService userActivationService;


    private static final User user_valid = mock(User.class);
    private static final String username = "test_user@gmail.com";
    private static final String password = "Valid777!";
    private static final String firstName = "Dexter";
    private static final String lastName = "Morgan";
    private static final Date dob_valid = Date.valueOf("2000-02-12");





    @BeforeAll
    static void setup() {
        when(user_valid.getUsername()).thenReturn(username);
        when(user_valid.getPassword()).thenReturn(password);
        when(user_valid.getFirstName()).thenReturn(firstName);
        when(user_valid.getLastName()).thenReturn(lastName);
        when(user_valid.getBirthDate()).thenReturn(dob_valid);
    }


    @Test
    void saveUser() throws MessagingException {

//        when(userDao.save(any())).thenReturn(doNothing());
        when(passwordEncoder.encode(user_valid.getPassword())).thenReturn("thisisencodedpassword");
        when(userDao.findByUsername(user_valid.getUsername())).thenReturn(user_valid);
        when(randomVerificationNumber.getRandomVerificationNumber()).thenReturn(12345);
        when(userDao.existsByUsername(user_valid.getUsername())).thenReturn(true);
        ResponseEntity<?> save = userService.save(user_valid);
        assertEquals(save.getStatusCode(), HttpStatus.OK);
    }


}