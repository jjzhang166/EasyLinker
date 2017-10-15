package com.wwh.iot.easylinker.configure.activemq.amqplugin;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by wwhai on 2017/10/15.
 * 负责将插件安装进Activemq
 */
public class PluginInstaller implements BrokerPlugin {
    private static Log logger = LogFactory.getLog(PluginInstaller.class);

    @Override
    public Broker installPlugin(Broker broker) throws Exception {
        return new DeviceAuthPlugin(broker);
    }

    public PluginInstaller() {
        logger.info("PluginInstaller started!");

    }
}
