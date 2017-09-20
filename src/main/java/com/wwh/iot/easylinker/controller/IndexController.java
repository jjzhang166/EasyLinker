package com.wwh.iot.easylinker.controller;

import com.alibaba.fastjson.JSONObject;
import com.wwh.iot.easylinker.entity.AppUser;
import com.wwh.iot.easylinker.repository.AppUserRepository;
import com.wwh.iot.easylinker.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by wwhai on 2017/7/27.
 */

/**
 * 首页控制器
 */
@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @RequestMapping("/")
    public String defaultPage() {
        return "index";
    }

    @RequestMapping("/index")
    public String index(Model model) {

        return "index";
    }

    @RequestMapping("/signupPage")
    public String signupPage() {
        return "admin/signupPage";
    }

    @RequestMapping("/loginPage")
    public String loginPage() {
        return "loginpage";
    }

    @RequestMapping("/document")
    public String document() {
        return "document";
    }

    @RequestMapping("/loginFailed")
    public String loginFailed() {
        return "loginfailed";
    }

    @RequestMapping("/ifUserExist")
    @ResponseBody
    public JSONObject ifUserExist(@RequestParam String username){
        AppUser appUser=  appUserRepository.findTop1ByUsernameOrEmailOrPhone(username,username,username);
        System.out.println(appUser);
        if (appUser!=null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", "用户已存在!");
            jsonObject.put("state",1);
            return jsonObject;
        }else {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("state",0);
            return jsonObject;
        }
    }
}
