package com.wwh.iot.easylinker.configure.activemq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.entity.data.TypeValueData;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import com.wwh.iot.easylinker.repository.TypeValueDataRepository;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by wwhai on 2017/7/31.
 */
@Component("TypeValueConsumer")
public class TypeValueConsumer {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    TypeValueDataRepository typeValueDataRepository;
    String username;
    String textMessage;
    String valueName;//计量单位 比如 牛顿 千克
    JSONObject receivedMessageJson;

    @JmsListener(destination = "device.typevalue.>")
    public void receiveMessage(ActiveMQMessage message) throws Exception {
        System.out.println(message);
        if (message instanceof ActiveMQTextMessage) {
            receivedMessageJson = JSONObject.parseObject(((ActiveMQTextMessage) message).getText().toString());
            username = receivedMessageJson.get("username").toString();
            textMessage = receivedMessageJson.get("message").toString();
            valueName = receivedMessageJson.get("name").toString();

        } else if (message instanceof ActiveMQBytesMessage) {
            receivedMessageJson = JSONObject.parseObject(new String(message.getMessage().getContent().getData(), "utf-8"));
            username = receivedMessageJson.get("username").toString();
            textMessage = receivedMessageJson.get("message").toString();
            valueName = receivedMessageJson.get("name").toString();
        }
        if (username != null && textMessage != null) {
            Device device = deviceRepository.findOne(username);
            if (device != null) {
                TypeValueData typeValueData = new TypeValueData();
                typeValueData.setDevice(device);
                typeValueData.setValue(textMessage);
                typeValueData.setName(valueName);
                typeValueDataRepository.save(typeValueData);
            } else {
                    System.out.println("Local Device or no such device !");
            }

        } else {
            System.out.println("Null value!");
        }
    }
}
