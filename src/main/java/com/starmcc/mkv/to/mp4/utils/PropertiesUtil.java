package com.starmcc.mkv.to.mp4.utils;

import com.starmcc.mkv.to.mp4.StarmccMkvToMp4pplication;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final String PROPERTIES_NAME = "application.properties";

    public static String readData(String node) {
        Properties properties = new Properties();
        try (InputStream input = StarmccMkvToMp4pplication.class.getClassLoader().getResourceAsStream(PROPERTIES_NAME)) {
            properties.load(input);
            return properties.getProperty(node);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
