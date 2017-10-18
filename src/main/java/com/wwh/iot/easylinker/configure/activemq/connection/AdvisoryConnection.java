package com.wwh.iot.easylinker.configure.activemq.connection;

import com.wwh.iot.easylinker.configure.activemq.listener.ActiveMQMessageListener;
import com.wwh.iot.easylinker.configure.activemq.listener.AdvisoryListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;

/**
 * 通知监听
 */
@Configuration
public class AdvisoryConnection {
    private ActiveMQConnectionFactory activeMQConnectionFactory;

    public AdvisoryConnection(ActiveMQConnectionFactory activeMQConnectionFactory) {
        this.activeMQConnectionFactory = activeMQConnectionFactory;

    }

    public MessageConsumer getMessageConsumer() throws Exception {
        Connection connection = activeMQConnectionFactory.createTopicConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(new ActiveMQTopic("ActiveMQ.Advisory.Connection.>"));
        consumer.setMessageListener(getAdvisoryListener());
        return consumer;
    }

    @Bean
    public AdvisoryListener getAdvisoryListener() {
        return new AdvisoryListener();
    }
}
