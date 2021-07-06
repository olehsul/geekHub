package com.owu.geekhub.configs;


import com.owu.geekhub.dao.UserDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Configuration
@TestPropertySource(
        locations = {"classpath:application.properties"})
@ExtendWith(SpringExtension.class)
public class IntegrationConfig {





}
