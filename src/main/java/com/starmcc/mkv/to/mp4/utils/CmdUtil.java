package com.starmcc.mkv.to.mp4.utils;

import com.starmcc.mkv.to.mp4.frame.StarmccConstant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdUtil {
    public static int run(String cmd) throws Exception {
        if (!StarmccConstant.consoleWindowStatus) {
            return run(cmd, null);
        }
        String bfCmd = "cmd.exe /c start /wait \"\" ";
        System.out.println("cmd: " + bfCmd + cmd);
        Process process = Runtime.getRuntime().exec(bfCmd + cmd);
        // 等待进程执行完成
        return process.waitFor();
    }


    public static int run(String cmd, Consumer<String> consumer) throws Exception {
        // 创建 ProcessBuilder 对象
        System.out.println("cmd: " + cmd);
        ProcessBuilder processBuilder = new ProcessBuilder(cmd.split(" "));

        // 设置将输出流连接到 Java 控制台
        processBuilder.redirectErrorStream(true);

        // 启动进程
        Process process = processBuilder.start();
        // 读取进程输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (Objects.nonNull(consumer)) {
                consumer.accept(line);
            }
        }
        // 等待进程执行完成
        return process.waitFor();

    }


    public static List<String> buildVideoDataList(List<String> strs) {
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < strs.size(); i++) {
            String str = strs.get(i).replace("\n", "").trim();

            // 定义正则表达式
            String regex = "Stream #(\\d+:\\d+).*?: (.*?)$";
            // 创建 Matcher 对象
            Matcher matcher = Pattern.compile(regex).matcher(str);
            // 查找匹配
            if (matcher.find()) {
                // 获取匹配的结果
                strList.add("[" + matcher.group(1).trim() + "]" + matcher.group(2).trim());
            } else if (!strList.isEmpty()) {
                regex = "title\\s+:(.*)";
                matcher = Pattern.compile(regex).matcher(str);
                if (matcher.find()) {
                    int index = strList.size() - 1;
                    strList.set(index, strList.get(index) + " - " + matcher.group(1).trim());
                }
            }
        }
        return strList;
    }


    public static String buildSpe(String str, boolean front, boolean after) {
        return (front ? " " : "") + str + (after ? " " : "");
    }


    public static String buildMapNumber(String str) {
        String regex = "^\\[(\\d+:\\d+)\\]";
        Matcher matcher = Pattern.compile(regex).matcher(str);
        return matcher.find() ? matcher.group(1).trim() : "";
    }


    public static String buildQuotationMarks(String str) {
        return "\"" + str + "\"";
    }

    public static String getDesktopPath() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            return System.getProperty("user.home") + "\\Desktop\\";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Linux or Mac
            return System.getProperty("user.home") + "/Desktop/";
        }

        // Other systems
        return System.getProperty("user.home") + "\\";
    }
}
