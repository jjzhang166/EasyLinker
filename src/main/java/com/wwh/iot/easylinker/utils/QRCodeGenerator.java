package com.wwh.iot.easylinker.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by wwhai on 2017/10/5.
 * 二维码生成器
 */
public class QRCodeGenerator {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    public static String GenerateQRCode(String content) throws Exception {
        String path = "/qrcode/" + UUID.randomUUID().toString() + ".png";
        File file = new File(path);
        dirExists(file);
        OutputStream outputStream = new FileOutputStream(file);
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix m = writer.encode(content, BarcodeFormat.QR_CODE, HEIGHT, WIDTH);
        MatrixToImageWriter.writeToStream(m, "png", outputStream);
        return path;
    }

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
}
