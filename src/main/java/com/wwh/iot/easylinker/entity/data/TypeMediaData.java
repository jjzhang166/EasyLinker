package com.wwh.iot.easylinker.entity.data;

/**
 * Created by wwhai on 2017/7/31.
 */

import com.wwh.iot.easylinker.entity.AppUser;
import com.wwh.iot.easylinker.entity.BaseEntity;
import com.wwh.iot.easylinker.entity.Device;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 媒体类型持久化存档数据
 */
@Entity
public class TypeMediaData extends BaseEntity{
    private String path;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL,optional = true)
    private Device device;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public Device getDevice() {
        return device;
    }
}
