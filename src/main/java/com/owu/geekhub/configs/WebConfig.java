package com.owu.geekhub.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/verification").setViewName("verification");
//        if (!(SecurityContextHolder.getContext().getAuthentication()
//                instanceof AnonymousAuthenticationToken)) {
//            registry.addViewController("/auth").setViewName("index");
//        } else {
//            //get method
//            registry.addViewController("/auth").setViewName("auth");
//        }
    }
}
