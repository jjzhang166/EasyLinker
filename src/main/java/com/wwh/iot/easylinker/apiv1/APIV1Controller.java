package com.wwh.iot.easylinker.apiv1;

import com.alibaba.fastjson.JSONObject;
import com.wwh.iot.easylinker.constants.DeviceType;
import com.wwh.iot.easylinker.constants.SystemMessage;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.entity.data.TypeMediaData;
import com.wwh.iot.easylinker.entity.data.TypeValueData;
import com.wwh.iot.easylinker.repository.AppUserRepository;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import com.wwh.iot.easylinker.repository.TypeMediaDataRepository;
import com.wwh.iot.easylinker.repository.TypeValueDataRepository;
import com.wwh.iot.easylinker.utils.FilePathHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by wwhai on 2017/8/28.
 */
@RequestMapping("/apiv1")
@RestController
public class APIV1Controller {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    TypeValueDataRepository typeValueDataRepository;
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    TypeMediaDataRepository typeMediaDataRepository;

    MessageSender messageSender = new MessageSender();


    @RequestMapping("/test")
    public JSONObject test() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "test ok!");
        jsonObject.put("state", 1);
        return jsonObject;
    }


    @RequestMapping("/sendMessageToDevice")
    public JSONObject sendMessageToDevice(@RequestParam(defaultValue = "null") String deviceId, @RequestParam(defaultValue = "hello") String message) {
        System.out.println(deviceId);

        return messageSender.pushMessage(deviceId, message);

    }

    @RequestMapping("/sendMessageToServer")
    public JSONObject sendMessageToServer(@RequestParam String deviceId, @RequestParam(defaultValue = "hello") String message) {
        Device device = deviceRepository.findOne(deviceId);
        JSONObject resultJson = new JSONObject();
        if (device != null) {
            DeviceType type = device.getType();
            switch (type) {
                case TYPE_MEDIA:
                    TypeMediaData typeMediaData = new TypeMediaData();
                    typeMediaData.setDevice(device);
                    typeMediaData.setValue(message);
                    typeMediaData.setName(device.getName());
                    typeMediaDataRepository.save(typeMediaData);
                    break;

                case TYPE_VALUE:
                    TypeValueData typeValueData = new TypeValueData();
                    typeValueData.setValue(message);
                    typeValueData.setDevice(device);
                    typeValueData.setName(device.getName());
                    typeValueDataRepository.save(typeValueData);

                default:
                    break;
            }
            resultJson.put("state", 1);
            resultJson.put("message", SystemMessage.OPERATE_SUCCESS.toString());
        } else {
            resultJson.put("state", 0);
            resultJson.put("message", SystemMessage.OPERATE_FAILED.toString());
        }

        return resultJson;

    }


    @RequestMapping("/uploadMediaMessage")
    public JSONObject uploadMediaMessage(@RequestParam String deviceId, @RequestParam MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "文件为空!");
            jsonObject.put("state", 0);
            return jsonObject;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilePath = "/uploadfiles";
        String newFileName = UUID.randomUUID() + suffixName;
        File newFile = new File(newFilePath + newFileName);
        FilePathHelper.dirExists(newFile);
        if (!newFile.exists()) {
            newFile.createNewFile();
        }

        Device device = deviceRepository.findOne(deviceId);
        if (device == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "设备不存在!");
            jsonObject.put("state", 0);
            return jsonObject;
        }
        TypeMediaData typeMediaData = new TypeMediaData();
        typeMediaData.setValue(newFileName);
        typeMediaData.setDevice(device);
        typeMediaData.setName(originalFilename);
        typeMediaDataRepository.save(typeMediaData);
        try {

            multipartFile.transferTo(newFile);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "Success!");
            System.out.println(newFile.getCanonicalPath());
            jsonObject.put("state", 1);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "Failed!");
            jsonObject.put("state", 0);
            return jsonObject;
        }
    }

    @RequestMapping("/getValueDatas")
    public JSONObject getValueDatas(@RequestParam(defaultValue = "") String deviceId, int page) {
        JSONObject jsonObject=new JSONObject();
        Page<TypeValueData> dataPage = null;
        Device device = deviceRepository.findOne(deviceId);
        if (device != null) {
            dataPage = typeValueDataRepository.findAllByDeviceOrderByCreateTimeDesc(device, new PageRequest(10, page));

            jsonObject.put("state",1);
            jsonObject.put("message",SystemMessage.OPERATE_SUCCESS);
            jsonObject.put("data",dataPage);
        }else {
            jsonObject.put("state",0);
            jsonObject.put("message",SystemMessage.OPERATE_FAILED);
        }
        return jsonObject;


    }

}
