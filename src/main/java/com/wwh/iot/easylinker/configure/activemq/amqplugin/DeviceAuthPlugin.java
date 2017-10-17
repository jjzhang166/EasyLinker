package com.wwh.iot.easylinker.configure.activemq.amqplugin;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.Topic;

/**
 * Created by wwhai on 2017/10/15.
 */
public class DeviceAuthPlugin extends BrokerFilter {

    private static Logger logger = LoggerFactory.getLogger(DeviceAuthPlugin.class);
    public static final String LOCAL_HOST = "tcp://127.0.0.1";
    BrokerJdbcTemplate brokerJdbcTemplate;

    public DeviceAuthPlugin(Broker next) {
        super(next);

        brokerJdbcTemplate = new BrokerJdbcTemplate();
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        String loginfo = "\n-------------------------------------------------------\n" +
                "|来自IP为[ " + info.getClientIp() + "]设备号为["+context.getUserName()+"]的连接|\n" +
                "-------------------------------------------------------\n";

        logger.info(loginfo);
        if (context.getUserName() == null) {
            if (info.getClientIp().startsWith(LOCAL_HOST)) {
                super.addConnection(context, info);
            } else {
                throw new SecurityException("ID为空而且不是本地设备，验证失败！");
            }

        } else if (context.getUserName() != null) {
            if (brokerJdbcTemplate.judgeDeviceIsExist(context.getUserName())) {
                super.addConnection(context, info);
            } else {
                throw new SecurityException("ID指向的设备不存在，验证失败！");
            }

        }

    }

    @Override
    public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
        super.send(producerExchange, messageSend);
        Topic topic = new ActiveMQTopic("ActiveMQ.Advisory.>");

        logger.info("send"+messageSend.getMessage());
    }


}
