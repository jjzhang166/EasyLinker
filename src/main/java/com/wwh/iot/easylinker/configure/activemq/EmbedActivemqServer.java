package com.wwh.iot.easylinker.configure.activemq;

import com.wwh.iot.easylinker.configure.activemq.amqplugin.DeviceAuthPlugin;
import com.wwh.iot.easylinker.configure.activemq.amqplugin.PluginInstaller;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwhai on 2017/10/15.
 * 嵌入式ActiveMQ服务器
 */

public class EmbedActivemqServer extends BrokerService {
    private static Logger logger = LoggerFactory.getLogger(DeviceAuthPlugin.class);
    public static final String OPEN_WIRE_URL="tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String MQTT_URL="mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String STOMP_URL="stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String AMQP_URL="amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600";
    public static final String KAHADB_PATH="./data/easylinker-activemq-data";

    public EmbedActivemqServer() {
        try {
            this.setBrokerName("easylinker-server");
            this.setPlugins(new BrokerPlugin[]{new PluginInstaller()});
            TransportConnector openwireConnector = new TransportConnector();
            TransportConnector mqttConnector = new TransportConnector();
            openwireConnector.setName("openwire");
            openwireConnector.setUri(new URI(OPEN_WIRE_URL));
            mqttConnector.setName("mqtt");
            mqttConnector.setUri(new URI(MQTT_URL));
            List<TransportConnector>transportConnectors=new ArrayList<>();
            transportConnectors.add(openwireConnector);
            transportConnectors.add(mqttConnector);
            this.setTransportConnectors(transportConnectors);
            this.setDataDirectory(KAHADB_PATH);
            this.setUseShutdownHook(true);
            this.setPersistenceAdapter(new MemoryPersistenceAdapter());
        } catch (Exception e) {
            logger.error("EmbedActivemqServer start failed!");
            e.printStackTrace();
        }

    }

    @Override
    public void start() throws Exception {
        logger.info("Embed Activemq started!");
        super.start();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Embed Activemq stop!");
        super.stop();
    }

}
