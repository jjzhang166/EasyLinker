package com.wwh.iot.easylinker.configure;

import com.wwh.iot.easylinker.configure.activemq.EmbedActivemqServer;
import com.wwh.iot.easylinker.configure.activemq.amqplugin.DeviceAuthPlugin;
import com.wwh.iot.easylinker.configure.activemq.connection.AdvisoryConnection;
import com.wwh.iot.easylinker.configure.activemq.listener.ActiveMQExceptionListener;
import com.wwh.iot.easylinker.configure.activemq.listener.ActiveMQMessageListener;
import com.wwh.iot.easylinker.configure.activemq.listener.ActiveMqTransportListener;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.transport.TransportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.MessageConsumer;

/**
 * Created by wwhai on 2017/7/31.
 */
@Configuration
public class ActiveMQConfig {
    private static Logger logger = LoggerFactory.getLogger(DeviceAuthPlugin.class);

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
    @Autowired
    DeviceRepository deviceRepository;
    Device device = null;
    private boolean isLocalDevice = false;


    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() throws Exception {


        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        activeMQConnectionFactory.setWatchTopicAdvisories(true);
        activeMQConnectionFactory.setTransportListener(addActiveMqTransportListener());
        activeMQConnectionFactory.setExceptionListener(addActiveMQExceptionListener());


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

    @Bean

    public BrokerService addAMQServer() throws Exception {
        EmbedActivemqServer embedActivemqServer = new EmbedActivemqServer();
        embedActivemqServer.start();


        return embedActivemqServer;
    }

    @Bean
    public JmsTemplate addJmsTemplate() throws Exception {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        return jmsTemplate;
    }

    @Bean
    public MessageConsumer addMessageConsumer() throws Exception {
        AdvisoryConnection advisoryConnection = new AdvisoryConnection(activeMQConnectionFactory());
        return advisoryConnection.getMessageConsumer();
    }


}
