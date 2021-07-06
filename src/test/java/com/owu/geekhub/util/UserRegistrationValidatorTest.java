package com.owu.geekhub.util;

import com.owu.geekhub.configs.TestConfig;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.exception.UserInvalidException;
import com.owu.geekhub.models.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = TestConfig.class,
        loader = AnnotationConfigContextLoader.class
)
class UserRegistrationValidatorTest {

    @Autowired
    private UserRegistrationValidator userRegistrationValidator;

    // Mocked dependencies
    @Autowired
    private UserDao userDao;

    //Valid user to test
    private static final User user_valid = mock(User.class);
    private static final String username = "test_user@gmail.com";
    private static final String password = "Valid777!";
    private static final String firstName = "Dexter";
    private static final String lastName = "Morgan";
    private static final Date dob_valid = Date.valueOf("2000-02-12");

    //Invalid user to test
    private static final User user_invalid = mock(User.class);
    private static final String password_invalid = "Valid777!";
    private static final String lastName_Invalid = "@Morgan";
    private static final Date dob_invalid = Date.valueOf("2022-02-12");




    @BeforeAll
    static void setup() {
        when(user_valid.getUsername()).thenReturn(username);
        when(user_valid.getPassword()).thenReturn(password);
        when(user_valid.getFirstName()).thenReturn(firstName);
        when(user_valid.getLastName()).thenReturn(lastName);
        when(user_valid.getBirthDate()).thenReturn(dob_valid);

        when(user_invalid.getUsername()).thenReturn(username);
        when(user_invalid.getPassword()).thenReturn(password_invalid);
        when(user_invalid.getFirstName()).thenReturn(firstName);
        when(user_invalid.getLastName()).thenReturn(lastName_Invalid);
        when(user_invalid.getBirthDate()).thenReturn(dob_invalid);
    }

    @Test
    void validatedUserAccepted() throws UserInvalidException {
        when(userDao.existsByUsername(username)).thenReturn(false);
        userRegistrationValidator.validate(user_valid);
    }

    @Test
    void validateUserNotAccepted1() {
        when(userDao.existsByUsername(username)).thenReturn(false);
        assertThrows(UserInvalidException.class, () -> userRegistrationValidator.validate(user_invalid));
    }
}