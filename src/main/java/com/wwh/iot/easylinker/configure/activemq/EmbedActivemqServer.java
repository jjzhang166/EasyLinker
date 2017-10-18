package com.wwh.iot.easylinker.configure.activemq;

import com.wwh.iot.easylinker.configure.activemq.amqplugin.DeviceAuthPlugin;
import com.wwh.iot.easylinker.configure.activemq.amqplugin.PluginInstaller;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.broker.region.policy.ConstantPendingMessageLimitStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.store.kahadb.KahaDBStore;
import org.apache.activemq.usage.MemoryUsage;
import org.apache.activemq.usage.StoreUsage;
import org.apache.activemq.usage.SystemUsage;
import org.apache.activemq.usage.TempUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwhai on 2017/10/15.
 * 嵌入式ActiveMQ服务器
 */

public class EmbedActivemqServer extends BrokerService {
    private static Logger logger = LoggerFactory.getLogger(DeviceAuthPlugin.class);
    public static final String OPEN_WIRE_URL = "tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String MQTT_URL = "mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String STOMP_URL = "stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String AMQP_URL = "amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String KAHADB_PATH = "/data/easylinker-activemq-data";

    public EmbedActivemqServer() {
        try {
            /**
             * 设置一些常量
             */
            this.setBrokerName("easylinker-server");
            this.setAdvisorySupport(true);
            this.setPlugins(new BrokerPlugin[]{new PluginInstaller()});
            this.setUseJmx(false);
            this.setPersistent(true);
            this.setDataDirectory(KAHADB_PATH);
            this.setUseShutdownHook(true);
            this.setAllowTempAutoCreationOnSend(true);




            /**
             *增加连接器
             */
            TransportConnector openwireConnector = new TransportConnector();
            TransportConnector mqttConnector = new TransportConnector();
            openwireConnector.setName("openwire");
            openwireConnector.setUri(new URI(OPEN_WIRE_URL));
            mqttConnector.setName("mqtt");
            mqttConnector.setUri(new URI(MQTT_URL));

            List<TransportConnector> transportConnectors = new ArrayList<>();
            transportConnectors.add(openwireConnector);
            transportConnectors.add(mqttConnector);
            this.setTransportConnectors(transportConnectors);

            /**
             * 消息策略
             */
            List<PolicyEntry> policyEntries = new ArrayList<>();
            PolicyEntry topicPolicyEntry = new PolicyEntry();
            topicPolicyEntry.setTopic(">");
            topicPolicyEntry.setAdvisoryForConsumed(true);
            topicPolicyEntry.setAdvisoryForDelivery(true);
            ConstantPendingMessageLimitStrategy pendingMessageLimitStrategy = new ConstantPendingMessageLimitStrategy();
            pendingMessageLimitStrategy.setLimit(1000);
            topicPolicyEntry.setPendingMessageLimitStrategy(pendingMessageLimitStrategy);
            policyEntries.add(topicPolicyEntry);
            PolicyMap policyMap = new PolicyMap();
            policyMap.setPolicyEntries(policyEntries);
            this.setDestinationPolicy(policyMap);
            /**
             *内存配置
             */
            SystemUsage systemUsage = new SystemUsage();
            MemoryUsage memoryUsage = new MemoryUsage();
            memoryUsage.setPercentOfJvmHeap(70);
            systemUsage.setMemoryUsage(memoryUsage);
            StoreUsage storeUsage = new StoreUsage();
            storeUsage.setLimit(100);
            systemUsage.setStoreUsage(storeUsage);
            TempUsage tempUsage = new TempUsage();
            tempUsage.setLimit(50);
            systemUsage.setTempUsage(tempUsage);
            this.setSystemUsage(systemUsage);


            /**
             *通知管理
             */

            ActiveMQTopic topic = new ActiveMQTopic("ActiveMQ.Advisory.Connection");
            this.setDestinations(new ActiveMQDestination[]{topic});
            ManagementContext managementContext = new ManagementContext();
            managementContext.setCreateConnector(true);
            this.setManagementContext(managementContext);

            /**
             *KAHA数据库
             */

            KahaDBStore kahaDBStore = new KahaDBStore();
            kahaDBStore.setDirectory(new File(KAHADB_PATH + System.currentTimeMillis()));
            this.setPersistenceAdapter(kahaDBStore);


        } catch (Exception e) {
            logger.error("EmbedActivemqServer start failed!");
            e.printStackTrace();
        }

    }

    @Override
    public void start() throws Exception {
        logger.info("EmbedActivemqServer   started!");
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("EmbedActivemqServer   stop!");
        super.stop();
    }


}
