package com.wwh.iot.easylinker.apiv1;

import com.alibaba.fastjson.JSONObject;
import com.wwh.iot.easylinker.entity.AppUser;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.entity.data.TypeMediaData;
import com.wwh.iot.easylinker.repository.AppUserRepository;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import com.wwh.iot.easylinker.repository.TypeMediaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    @RequestMapping("/sendMessage")
    public JSONObject sendMessage(@RequestParam String deviceId, @RequestParam(defaultValue = "hello") String message) {
        return messageSender.pushMessage(deviceId, message);

    }

    @RequestMapping("/uploadMediaMessage")
    public JSONObject uploadMediaMessage(@RequestParam String deviceId,  @RequestParam MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "文件为空!");
            jsonObject.put("state", 0);
            return jsonObject;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilePath = "/";
        String newFileName = UUID.randomUUID() + suffixName;
        File newFile = new File(newFilePath + newFileName);
        if (!newFile.exists()) {
            System.out.println("文件不存在");
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
        typeMediaData.setPath(newFileName);
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

}
