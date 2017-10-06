package com.wwh.iot.easylinker.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wwh.iot.easylinker.configure.activemq.ActiveMQMessageProducer;
import com.wwh.iot.easylinker.constants.DeviceType;
import com.wwh.iot.easylinker.constants.SystemMessage;
import com.wwh.iot.easylinker.entity.AppUser;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.entity.data.TypeMediaData;
import com.wwh.iot.easylinker.entity.data.TypeValueData;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import com.wwh.iot.easylinker.repository.TypeMediaDataRepository;
import com.wwh.iot.easylinker.repository.TypeValueDataRepository;
import com.wwh.iot.easylinker.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wwhai on 2017/7/30.
 */

/**
 * 管理员后台控制器
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    ActiveMQMessageProducer activeMQMessageProducer;

    @Autowired
    TypeValueDataRepository typeValueDataRepository;
    @Autowired
    TypeMediaDataRepository typeMediaDataRepository;

    @RequestMapping("/index")
    public String index(Model model) throws Exception {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> systemInfo = new HashMap<>();
        Properties props = System.getProperties();
        String osName = props.getProperty("os.name");
        String osArch = props.getProperty("os.arch");
        String osVersion = props.getProperty("os.version");
        String totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M";
        String freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M";
        String alreadyUse = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "M";
        systemInfo.put("osName", osName);
        systemInfo.put("osArch", osArch);
        systemInfo.put("osVersion", osVersion);
        systemInfo.put("totalMemory", totalMemory);
        systemInfo.put("freeMemory", freeMemory);
        systemInfo.put("alreadyUse", alreadyUse);
        systemInfo.put("deviceCount", deviceRepository.countByAppUser(appUser));
        systemInfo.put("onlineDevice", deviceRepository.getOnlineDeviceCount());
        model.addAttribute("systemInfo", systemInfo);
        return "admin/index";
    }

    @RequestMapping("/devices")
    public String devices(
            ModelMap model,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size
    ) {
        AppUser user = (AppUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Sort sort = new Sort(Sort.Direction.DESC, "id");

        Page<Device> devicePage = deviceRepository.findByAppUserOrderByCreateTimeDesc(user, new PageRequest(page, size, sort));
        System.out.println(devicePage.getNumberOfElements());
        model.put("devicePage", devicePage);
        return "admin/devices";
    }


    @RequestMapping("/addDevice")
    public String addDevice() {
        return "admin/addDevice";
    }

    @RequestMapping("/add")
    public String add(RedirectAttributes redirectAttributes, @RequestParam String name, @RequestParam DeviceType type, @RequestParam String describe, @RequestParam String deviceGroup) {
        AppUser user = (AppUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Device device = new Device();
        device.setDeviceGroup(deviceGroup);
        device.setAppUser(user);
        device.setName(name);
        device.setQrCode( QRCodeGenerator.GenerateQRCode(device.getId()));
        device.setDeviceDescribe(describe);
        device.setType(type);
        deviceRepository.save(device);
        redirectAttributes.addFlashAttribute("message", SystemMessage.OPERATE_SUCCESS.toString());
        return "redirect:/admin/addDevice";
    }

    @RequestMapping("/sysConfig")
    public String sysConfig() {
        return "admin/sysConfig";
    }

    @ResponseBody
    @RequestMapping("/deleteDevice")
    public JSONObject deleteDevice(@RequestParam String deviceId) {
        JSONObject jsonObject = new JSONObject();
        deviceRepository.delete(deviceId);
        jsonObject.put("state", 1);
        jsonObject.put("message", SystemMessage.OPERATE_SUCCESS.toString());
        return jsonObject;
    }


    @RequestMapping("/deviceDetail")
    public String deviceDetail(ModelMap modelMap, @RequestParam String deviceId) {
        modelMap.put("device", deviceRepository.findOne(deviceId));
        return "admin/deviceDetail";
    }

    @RequestMapping("/deviceData")
    @ResponseBody
    /**
     * 获取数据列表
     */
    public JSONObject deviceData(@RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(name = "deviceId") String deviceId,
                           @RequestParam(name = "deviceType") DeviceType deviceType,
                           @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        PageRequest pageRequest = new PageRequest(pageNumber - 1, size);
        Device device = deviceRepository.findOne(deviceId);
        JSONObject resultJson=new JSONObject();
        switch (deviceType.toString()) {
            case "TYPE_VALUE":
                resultJson.put("data",typeValueDataRepository.findAllByDeviceOrderByCreateTimeDesc(device, pageRequest));
                break;
            case "TYPE_MEDIA":
                resultJson.put("data",typeMediaDataRepository.findAllByDevice(device, pageRequest));
                break;
            default:
                break;
        }
        return resultJson;
    }


    @RequestMapping("/pushMessage")
    @ResponseBody
    public JSONObject pushMessage(@RequestParam String deviceId, @RequestParam(defaultValue = "default") String message) {
        return activeMQMessageProducer.pushMessage(deviceId, message);
    }


}
