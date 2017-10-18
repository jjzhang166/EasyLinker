package com.wwh.iot.easylinker.configure.activemq.amqplugin;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by wwhai on 2017/10/17.
 */
@Component
public class BrokerJdbcTemplate extends JdbcTemplate {
    /**
     * spring.datasource.driver-class-name=com.mysql.jdbc.Driver
     * spring.datasource.url=jdbc:mysql://localhost:3306/easylinker?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=true
     * spring.datasource.username=root
     * spring.datasource.password=root
     */
    private static Logger logger = LoggerFactory.getLogger(DeviceAuthPlugin.class);

    public BrokerJdbcTemplate() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/easylinker?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=true");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        setDataSource(dataSource);
        logger.info("Activemq数据源加载成功!");
    }

    public boolean judgeDeviceIsExist(String id) {
        return queryForList("select * from device where id =?", id).size() > 0 ? true : false;
    }
}
