package com.owu.geekhub.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.io.File;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/emailRecovery")
                .setViewName("authentication/recovery-email");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/" + uploadPath + "/usersPicture/");
//                .addResourceLocations("file:\\\\\\" + System.getProperty("user.dir") + appDir + uploadPath + File.separator + "usersPicture");
    }
}
