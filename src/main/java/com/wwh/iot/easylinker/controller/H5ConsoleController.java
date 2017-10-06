package com.wwh.iot.easylinker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wwhai on 2017/10/6.
 */
@Controller
@RequestMapping("/h5console")
public class H5ConsoleController {

    @RequestMapping("/index/{deviceId}")
    public String index(@PathVariable String deviceId){

        return "h5console/index";
    }
}
