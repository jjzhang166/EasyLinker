package com.wwh.iot.easylinker.controller;

import com.alibaba.fastjson.JSONObject;
import com.wwh.iot.easylinker.apiv1.MessageSender;
import com.wwh.iot.easylinker.configure.activemq.ActiveMQMessageProducer;
import com.wwh.iot.easylinker.constants.DeviceType;
import com.wwh.iot.easylinker.constants.SystemMessage;
import com.wwh.iot.easylinker.entity.AppUser;
import com.wwh.iot.easylinker.entity.Device;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

    @RequestMapping("/index")
    public String index(Model model)throws Exception {
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
        systemInfo.put("deviceCount",deviceRepository.countByAppUser(appUser) );
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

        Page<Device> devicePage = deviceRepository.findByAppUser(user, new PageRequest(page, size, sort));
        System.out.println(devicePage.getNumberOfElements());
        model.put("devicePage", devicePage);
        return "admin/devices";
    }


    @RequestMapping("/addDevice")
    public String addDevice() {
        return "admin/addDevice";
    }

    @RequestMapping("/add")
    public String add(RedirectAttributes redirectAttributes, @RequestParam String name, @RequestParam DeviceType type, @RequestParam String describe) {
        AppUser user = (AppUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Device device = new Device();

        device.setAppUser(user);
        device.setName(name);
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

    @Transactional
    @RequestMapping("/deleteDevice")
    public String deleteDevice(@RequestParam String deviceId) {
        deviceRepository.delete(deviceId);
        deviceRepository.flush();
        return "admin/devices";
    }



    @RequestMapping("/deviceDetail")
    public String deviceDetail(ModelMap modelMap, @RequestParam String deviceId) {
        modelMap.put("device", deviceRepository.findOne(deviceId));
        return "admin/deviceDetail";
    }


    @RequestMapping("/pushMessage")
    @ResponseBody
    public JSONObject pushMessage(@RequestParam String deviceId,  @RequestParam(defaultValue = "default") String message) {
        return activeMQMessageProducer.pushMessage(deviceId, message);
    }


}
