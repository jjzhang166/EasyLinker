package com.wwh.iot.easylinker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动入口
 */
@SpringBootApplication
@EnableWebSecurity
@EnableScheduling
@EnableWebMvc
@EnableSwagger2
@ServletComponentScan
public class EasylinkerApplication implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(EasylinkerApplication.class);


    @Override

    public void run(String... strings) throws Exception {


    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EasylinkerApplication.class, args);

    }

}
