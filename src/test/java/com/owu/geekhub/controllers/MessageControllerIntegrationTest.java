package com.owu.geekhub.controllers;

import com.owu.geekhub.configs.IntegrationConfig;
import com.owu.geekhub.configs.TestConfig;
import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.Gender;
import com.owu.geekhub.models.Role;
import com.owu.geekhub.models.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@WebMvcTest(MessageController.class)
//@ContextConfiguration(
//        classes = IntegrationConfig.class,
//        loader = AnnotationConfigContextLoader.class
//)
class MessageControllerIntegrationTest {







    @BeforeAll
    static void setup(@Autowired UserDao userDao) {
        createTestUsers(userDao);
    }

    @AfterAll
    static void rollback(@Autowired UserDao userDao) {
        deleteTestUsers(userDao);
    }


    @Test
    @Disabled
    void getConversations() {
    }

    @Test
    void getMessages() {
    }

    @Test
    void privateMessage() {
    }

    @Test
    @Disabled
    void saveMessageAsRead() {
    }

    @Test
    @Disabled
    void getUnreadMessages() {
    }

    @Test
    @Disabled
    void createConversationIfNotExistsAndGet() {
    }


    private static void createTestUsers(UserDao userDao) {
        userDao.save(
                completeUser(createTestUser(
                        100000101L, "Will", "Smith", "will.smith@geekmail.com", "testpass"
                ))
        );
        userDao.save(
                completeUser(createTestUser(
                        100000102L,"Jim", "Parsons", "jimP@geekmail.com", "testpass"
                ))
        );
        userDao.save(
                completeUser(createTestUser(
                        100000103L,"Cris", "Pratt", "cris.pratt@geekmail.com", "testpass"
                ))
        );


    }

    private static void deleteTestUsers(UserDao userDao) {
        userDao.deleteUserById(100000101L);
        userDao.deleteUserById(100000102L);
        userDao.deleteUserById(100000103L);
    }

    private static User createTestUser(long id, String name, String lastName, String username, String password) {
        return User.builder()
                .id(id)
                .firstName(name)
                .lastName(lastName)
                .username(username)
                .password(password)
                .gender(Gender.MALE)
                .birthDate(Date.valueOf("2000-07-04"))
                .build();
    }


    private static User completeUser(User user) {
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setRole(Role.ROLE_USER);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setActivated(true);
        user.setProfileImage("general.png");
        user.setActivationKey("00000");
        return user;
    }


}