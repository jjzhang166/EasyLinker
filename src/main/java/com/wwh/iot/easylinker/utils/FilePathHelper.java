package com.wwh.iot.easylinker.utils;

import com.wwh.iot.easylinker.EasylinkerApplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by wwhai on 2017/10/6.
 */
public class FilePathHelper {

    // 判断文件夹是否存在
    public static void dirExists(File file) {
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String getRootPath(){
        URL base = EasylinkerApplication.class.getResource("/");
        String path = new File(base.getPath()).getParent();
        return path;
    }

}
