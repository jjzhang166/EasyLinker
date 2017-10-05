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
import java.lang.String;
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
    JSONObject receivedMessageJson;

    @JmsListener(destination = "device.typevalue.>")
    public void receiveMessage(ActiveMQMessage message) throws Exception{

        if (message instanceof ActiveMQTextMessage){
            receivedMessageJson=JSONObject.parseObject(((ActiveMQTextMessage) message).getText().toString());
            username=receivedMessageJson.get("username").toString();
            textMessage=receivedMessageJson.get("message").toString();

        }else if(message instanceof ActiveMQBytesMessage){
            receivedMessageJson=JSONObject.parseObject(new String(message.getMessage().getContent().getData(),"utf-8"));
            username=receivedMessageJson.get("username").toString();
            textMessage=receivedMessageJson.get("message").toString();
        }
        if (username!=null){
            Device device=deviceRepository.findOne(username);
            if (device!=null){
                TypeValueData typeValueData=new TypeValueData();
                typeValueData.setDevice(device);
                typeValueData.setValue(textMessage);
                typeValueData.setName(message.getUserID());
                typeValueDataRepository.save(typeValueData);
            }else {
                System.out.println("Device Not Exist!");
            }

        }

        System.out.println("From device value message:"+message);
    }
}
