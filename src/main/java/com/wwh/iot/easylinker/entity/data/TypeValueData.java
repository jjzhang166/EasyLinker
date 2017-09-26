package com.wwh.iot.easylinker.entity.data;

import com.wwh.iot.easylinker.entity.BaseEntity;
import com.wwh.iot.easylinker.entity.Device;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by wwhai on 2017/7/31.
 *
 * 值类型持久化存档数据
 */
@Entity
public class TypeValueData extends BaseEntity{
    @ManyToOne
    private Device device;
    private String name;
    private String value;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
