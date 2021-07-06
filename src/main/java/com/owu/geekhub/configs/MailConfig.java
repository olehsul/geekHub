package com.owu.geekhub.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;
import java.util.Properties;

@Configuration
//@PropertySource("classpath:application.properties")
public class MailConfig implements WebMvcConfigurer {


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


    @Bean(name = "geekMailSender")
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
