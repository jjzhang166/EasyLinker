package com.wwh.iot.easylinker.configure;

import com.wwh.iot.easylinker.configure.activemq.ActiveMQExceptionListener;
import com.wwh.iot.easylinker.configure.activemq.ActiveMQMessageListener;
import com.wwh.iot.easylinker.configure.activemq.ActiveMqTransportListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.TransportListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wwhai on 2017/7/31.
 */
@Configuration
public class ActiveMQConfig {
    /**
     * ActiveMQ的工厂
     *
     * @return
     */
    @Value("${spring.activemq.broker-url}")
    String BROKER_URL;
    @Value("${spring.activemq.user}")
    String DEFAULT_USER;
    @Value("${spring.activemq.password}")
    String DEFAULT_PASSWORD;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() throws Exception {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                null,
                null,
                BROKER_URL);
        activeMQConnectionFactory.setTransportListener(addActiveMqTransportListener());
        activeMQConnectionFactory.setWatchTopicAdvisories(true);
        //activeMQConnectionFactory.setExceptionListener(addActiveMQExceptionListener());

        return activeMQConnectionFactory;
    }


    @Bean
    public TransportListener addActiveMqTransportListener() {
        return new ActiveMqTransportListener();

    }

    @Bean
    public ActiveMQMessageListener addActiveMQMessageListener() {

        return new ActiveMQMessageListener();
    }

    @Bean
    public ActiveMQExceptionListener addActiveMQExceptionListener() {

        return new ActiveMQExceptionListener();
    }
}
