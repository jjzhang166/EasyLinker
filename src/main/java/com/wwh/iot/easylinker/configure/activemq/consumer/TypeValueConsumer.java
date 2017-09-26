package com.wwh.iot.easylinker.configure.activemq.consumer;

import com.wwh.iot.easylinker.entity.AppUser;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.entity.data.TypeValueData;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import com.wwh.iot.easylinker.repository.TypeValueDataRepository;
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

    @JmsListener(destination = "device.typevalue.>")
    public void receiveMessage(ActiveMQMessage message) throws Exception{
        ActiveMQTextMessage textMessage=(ActiveMQTextMessage)message;
        String username=textMessage.getConnection().getConnectionInfo().getUserName();
        if (username!=null){
            Device device=deviceRepository.findOne(username);
            if (device!=null){
                TypeValueData typeValueData=new TypeValueData();
                typeValueData.setDevice(device);
                typeValueData.setValue(((ActiveMQTextMessage) message).getText().toString());
                typeValueData.setName(message.getUserID());
                typeValueDataRepository.save(typeValueData);
            }else {
                System.out.println("Device Not Exist!");
            }

        }

        System.out.println(username);
        System.out.println("from device message:"+message);
    }
}
