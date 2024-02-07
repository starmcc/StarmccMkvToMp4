package com.starmcc.mkv.to.mp4.utils;

import com.alibaba.fastjson2.JSON;
import com.starmcc.mkv.to.mp4.entity.UpdateModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class RequestUtil {

    public static UpdateModel request(String apiUrl) {
        HttpURLConnection connection = null;
        try {
            // 创建 URL 对象
            URL url = new URL(apiUrl);
            // 打开连接
            connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            connection.setRequestMethod("GET");

            // 获取响应码
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应内容
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // 输出响应内容
                System.out.println("API 响应：" + content.toString());
                return JSON.parseObject(content.toString(), UpdateModel.class);
            } else {
                System.out.println("API 请求失败，响应码：" + responseCode);
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭连接
            if (Objects.nonNull(connection)) {
                connection.disconnect();
            }
        }
    }
}
