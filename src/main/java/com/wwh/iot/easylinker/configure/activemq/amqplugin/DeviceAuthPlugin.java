package com.wwh.iot.easylinker.configure.activemq.amqplugin;

import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wwhai on 2017/10/15.
 */
public class DeviceAuthPlugin extends BrokerFilter {
    @Autowired
    DeviceRepository deviceRepository;

    private static Logger log = LoggerFactory.getLogger(DeviceAuthPlugin.class);

    public DeviceAuthPlugin(Broker next) {
        super(next);
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        System.out.println("-------------------------------------------------------");
        System.out.println("来自[ " + info.getClientIp() + "] 的连接");
        System.out.println("username:" + info.getUserName());
        System.out.println("-------------------------------------------------------");
        String username = context.getUserName();
        String ip = info.getClientIp();
        if (username == null) {
            if (ip.startsWith("tcp://127.0.0.1")) {
                super.addConnection(context, info);
                log.info("Local connection connected!");
            } else {
                throw new SecurityException("Auth filed device's ID is null!");
            }
        } else {
            if (username.equals("easylinker")) {
                super.addConnection(context, info);
                log.info("Easylinker connection connected!");

            } else {
                Device device = deviceRepository.findOne(username);
                System.out.println(device.getType());
                if (device != null) {
                    super.addConnection(context, info);
                } else {
                    throw new SecurityException("Auth filed, device not exist!");
                }
            }

        }
    }
}
