package com.wwh.iot.easylinker.configure.activemq.listener;

import com.wwh.iot.easylinker.configure.activemq.amqplugin.BrokerJdbcTemplate;
import com.wwh.iot.easylinker.configure.activemq.amqplugin.DeviceAuthPlugin;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.RemoveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class AdvisoryListener extends ActiveMQMessageListener {
    private static Logger logger = LoggerFactory.getLogger(DeviceAuthPlugin.class);
    BrokerJdbcTemplate brokerJdbcTemplate = new BrokerJdbcTemplate();
    @Autowired
    DeviceRepository deviceRepository;
    private boolean isLocalDevice = false;

    @Override
    public void onMessage(Message connectionMessage) {
        try {
            logger.info(connectionMessage.getJMSType().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        ActiveMQMessage activeMQMessage = (ActiveMQMessage) connectionMessage;
        logger.info(connectionMessage.toString());
        DataStructure dataStructure = activeMQMessage.getDataStructure();

        if (dataStructure instanceof ConnectionInfo) {
            String connectionId = ((ConnectionInfo) dataStructure).getConnectionId().toString();
            String username = (((ConnectionInfo) dataStructure).getUserName());
            System.out.println("username  " + username);
            if (((ConnectionInfo) dataStructure).getClientIp().startsWith("tcp://127")) {
                isLocalDevice = true;
            }
            if (username != null) {
                Device device = deviceRepository.findOne(username);
                if (device != null) {
                    device.setConnectionId(connectionId);
                    device.setIsOnline(true);
                    deviceRepository.save(device);
                    deviceRepository.flush();
                    logger.info("Device [" + username + "] connected with connectionId:" + connectionId);
                }

            }

        } else if (dataStructure instanceof RemoveInfo && (((RemoveInfo) dataStructure).getObjectId().toString() != null)) {

            String objectId = ((RemoveInfo) dataStructure).getObjectId().toString();
            if (!isLocalDevice) {
                if (objectId != null) {
                    Device device = deviceRepository.findByConnectionId(objectId);
                    if (device != null) {
                        device.setIsOnline(false);
                        deviceRepository.save(device);
                        deviceRepository.flush();
                        logger.info("Device disconnected with connection-id:" + objectId);
                    } else {
                        logger.warn("Device not exist!");
                    }

                } else {
                    logger.warn("Connection id is null!");
                }
            }
        }
    }
}
