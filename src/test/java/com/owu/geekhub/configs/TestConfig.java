package com.owu.geekhub.configs;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.service.EmailService;
import com.owu.geekhub.service.UserActivationService;
import com.owu.geekhub.service.UserService;
import com.owu.geekhub.service.impl.EmailServiceImpl;
import com.owu.geekhub.service.impl.UserActivationServiceImpl;
import com.owu.geekhub.service.impl.UserServiceImpl;
import com.owu.geekhub.util.RandomUserIdentity;
import com.owu.geekhub.util.RandomVerificationNumber;
import com.owu.geekhub.util.UserRegistrationValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Properties;

import static org.mockito.Mockito.mock;

@Configuration
@TestPropertySource(
        locations = {"classpath:application.properties"},
        properties = { "spring.mail.username = geekhub.owu@gmail.com" })
@ExtendWith(SpringExtension.class)
public class TestConfig {


    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.protocol}")
    private String protocol;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.debug}")
    private String debug;
    @Value("${mail.auth}")
    private String smtp_auth;
    @Value("${mail.starttls}")
    private String smtp_starttls;


    @Bean(name = "userActivationServiceMock")
    public UserActivationService userActivationService() {
        return new UserActivationServiceImpl(
                emailService(),
                userDaoMock(),
                randomVerificationNumberMock());
    }


//    @Bean(name = "EmailServiceTest")
    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl(javaMailSender());
    }

    @Bean(name = "userDaoMock")
    public UserDao userDaoMock() {
        return mock(UserDao.class);
    }

    @Bean(name = "userRegistrationValidator")
    public UserRegistrationValidator userRegistrationValidator() {
        return new UserRegistrationValidator();
    }

    @Bean(name = "randomVerificationNumberMock")
    public RandomVerificationNumber randomVerificationNumberMock() {
        return mock(RandomVerificationNumber.class);
    }

    @Bean(name = "passwordEncoderMock")
    public PasswordEncoder passwordEncoderMock() {
        return mock(PasswordEncoder.class);
    }

    @Bean(name = "randomUserIdentityMock")
    public RandomUserIdentity randomUserIdentityMock() {
        return mock(RandomUserIdentity.class);
    }

    @Bean(name = "userServiceMock")
    public UserService userService() {
        return new UserServiceImpl(
                userDaoMock(),
                userActivationService(),
                passwordEncoderMock(),
                randomUserIdentityMock(),
                userRegistrationValidator());
    }
//    @Bean
//    public JavaMailSender javaMailSender() {
//        return new JavaMailSenderImpl();
//    }

    @Bean(name = "geekMailSenderTest")
    @Primary
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", smtp_auth);
        properties.put("mail.smtp.starttls.enable", smtp_starttls);
        properties.put("mail.debug", debug);

        return mailSender;
    }

}
