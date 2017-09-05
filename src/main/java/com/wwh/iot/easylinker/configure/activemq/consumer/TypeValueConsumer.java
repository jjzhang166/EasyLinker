package com.wwh.iot.easylinker.configure.activemq.consumer;

import org.apache.activemq.command.ActiveMQMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by wwhai on 2017/7/31.
 */
@Component("TypeValueConsumer")
public class TypeValueConsumer {
    @JmsListener(destination = "device.>")
    public void receiveMessage(ActiveMQMessage message) {
        System.out.println("from device message:"+message);
    }
}
