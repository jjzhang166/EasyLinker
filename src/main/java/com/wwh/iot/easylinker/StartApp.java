package com.wwh.iot.easylinker;

import com.wwh.iot.easylinker.configure.activemq.EmbedActivemqServer;
import org.springframework.boot.SpringApplication;

/**
 * Created by wwhai on 2017/8/30.
 */
public class StartApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EasylinkerApplication.class, args);
    }
}
