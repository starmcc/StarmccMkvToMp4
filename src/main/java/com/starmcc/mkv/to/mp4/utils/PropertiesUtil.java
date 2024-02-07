package com.starmcc.mkv.to.mp4.utils;

import com.starmcc.mkv.to.mp4.StarmccMkvToMp4Application;
import com.starmcc.mkv.to.mp4.frame.StarmccConstant;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesUtil {
    private static final String CONFIG_NAME = "starmccConfig.properties";


    public static String actionGlobalConfig(StarmccConstant.ConfigKeyEnum configKeyEnum) {
        return actionGlobalConfig(configKeyEnum, null);
    }

    public static String actionGlobalConfig(StarmccConstant.ConfigKeyEnum configKeyEnum, String data) {

        Properties properties = new Properties();
        String filePath = System.getProperty("user.home") + "/" + CONFIG_NAME;

        if (Files.exists(Paths.get(filePath))) {
            try {
                properties.load(new FileReader(filePath));
            } catch (IOException e) {
                System.out.println("错误...配置文件不存在");
            }
        }
        if (data == null || data.isEmpty()) {
            String property = properties.getProperty(configKeyEnum.getKey());
            return property == null ? "" : property;
        }
        properties.setProperty(configKeyEnum.getKey(), data);
        try {
            OutputStream fos = new FileOutputStream(filePath);
            properties.store(fos, null);
        } catch (IOException e) {
            System.out.println("错误...保存配置失败");
            return "";
        }
        return data;

    }

}
