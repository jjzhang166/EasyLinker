package com.wwh.iot.easylinker.controller;

import com.wwh.iot.easylinker.EasylinkerApplication;
import com.wwh.iot.easylinker.utils.FilePathHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by wwhai on 2017/10/6.
 */
@Controller
public class QRCodeController {
    @RequestMapping("/qrcode/{imagePath}")
    public void getQRCode(HttpServletResponse response, @PathVariable String imagePath) throws IOException {
        File file = new File("/qrcode/"+imagePath+".png");
        OutputStream outputStream = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[fileInputStream.available()];
        fileInputStream.read(data);
        fileInputStream.close();
        response.setContentType("image/png;charset=utf-8");
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();

    }

}
