package com.owu.geekhub.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            //get method
            registry.addViewController("/login").setViewName("login");
            registry.addViewController("/register").setViewName("register");
        }
}
