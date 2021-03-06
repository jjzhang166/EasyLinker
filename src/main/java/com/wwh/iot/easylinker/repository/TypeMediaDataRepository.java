package com.wwh.iot.easylinker.repository;

import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.entity.data.TypeMediaData;
import com.wwh.iot.easylinker.entity.data.TypeValueData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wwhai on 2017/8/1.
 */
public interface TypeMediaDataRepository extends JpaRepository<TypeMediaData,String> {
    public Page<TypeMediaData> findAllByDevice(Device device, Pageable pageable);

}
