package com.wwh.iot.easylinker.configure.activemq;

import com.alibaba.fastjson.JSONObject;
import com.wwh.iot.easylinker.constants.SystemMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by wwhai on 2017/7/31.
 */

@Service("ActiveMQMessageProducer")
public class ActiveMQMessageProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @JmsListener(destination = ">")
    public void testReceived(ActiveMQMessage connectionMessage) {
        System.out.println("ActiveMQMessageProducer"+connectionMessage);

    }


//    @Scheduled(fixedDelay = 3000)//每3s执行1次
//    public void testSend() {
//        ActiveMQTopic activeMQTopic = new ActiveMQTopic("test");
//        this.jmsTemplate.convertAndSend(activeMQTopic, "test message");
//    }

    public JSONObject pushMessage(String deviceId, String message) {
        this.jmsTemplate.convertAndSend(new ActiveMQTopic("device." + deviceId), message);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", 1);
        jsonObject.put("message", SystemMessage.OPERATE_SUCCESS.toString());
        return jsonObject;
    }
}
