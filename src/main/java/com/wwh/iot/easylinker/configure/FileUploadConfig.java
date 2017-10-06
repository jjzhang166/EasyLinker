package com.wwh.iot.easylinker.configure;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * Created by wwhai on 2017/10/6.
 */
@Configuration
public class FileUploadConfig {
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/temp/");
        return factory.createMultipartConfig();
    }

}
