package com.owu.geekhub.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class MailConfig implements WebMvcConfigurer {

    private final Environment environment;

    public MailConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("email.host"));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("email.port"))));
        mailSender.setUsername(environment.getProperty("email.username"));
        mailSender.setPassword(environment.getProperty("email.password"));
        Properties properties = mailSender.getJavaMailProperties();
        properties.put(environment.getProperty("email.protocol"), environment.getProperty("email.protocol.val"));
        properties.put(environment.getProperty("email.auth"), environment.getProperty("email.auth.val"));
        properties.put(environment.getProperty("email.starttls"), environment.getProperty("email.starttls.val"));
        properties.put(environment.getProperty("email.mail.debug"), environment.getProperty("email.mail.debug.val"));
        return mailSender;
    }

}
