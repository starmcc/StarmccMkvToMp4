package com.starmcc.mkv.to.mp4.service;

import com.starmcc.mkv.to.mp4.utils.CmdUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FfmpegService {


    private static FfmpegService ffmpegService = null;

    public static FfmpegService getInstance() {
        if (Objects.isNull(ffmpegService)) {
            ffmpegService = new FfmpegService();
        }
        return ffmpegService;
    }


    public List<String> queryVideoInfo(String ffmpegPath, String inputFile) throws Exception {
        List<String> list = new ArrayList<>();
        String cmd = buildFfmpegPath(ffmpegPath) + " -i " + CmdUtil.buildQuotationMarks(inputFile);
        CmdUtil.run(cmd, list::add);
        return CmdUtil.buildVideoDataList(list);
    }


    public int exportVideo(String ffmpegPath,
                           String outputFolder,
                           String inputVideoPath,
                           List<String> selectList,
                           int type,
                           List<String> args) {
        return exportData(ffmpegPath, outputFolder, inputVideoPath, selectList, type, args);
    }

    public int exportSrt(String ffmpegPath,
                         String outputFolder,
                         String inputVideoPath,
                         String select) {
        List<String> list = new ArrayList<>();
        list.add(select);
        return exportData(ffmpegPath, outputFolder, inputVideoPath, list, 4, new ArrayList<>());
    }


    private int exportData(String ffmpegPath,
                           String outputFolder,
                           String inputVideoPath,
                           List<String> selectList,
                           int type,
                           List<String> args) {
        StringBuffer sb = new StringBuffer();
        sb.append(buildFfmpegPath(ffmpegPath)).append(" -i ").append(CmdUtil.buildQuotationMarks(inputVideoPath));


        for (int i = 0; i < selectList.size(); i++) {
            String num = CmdUtil.buildMapNumber(selectList.get(i));
            if (!num.isEmpty()) {
                sb.append(" -map ").append(num);
            }
        }

        String fileName = String.valueOf(System.currentTimeMillis());
        fileName += type != 4 ? ".mp4" : ".srt";

        switch (type) {
            case 0 -> sb.append(" -c:v copy -c:a copy -c:s mov_text ");
            case 1 -> sb.append(" -c:v libx264 -c:a aac -c:s mov_text ");
            case 2 -> sb.append(" -c:v copy -c:a copy -c:s mov_text -c:a:0 ac3 ");
            case 3 -> {
                if (args.isEmpty() || args.size() < 3) {
                    return -999;
                }
                sb.append(" -c:v ").append(args.get(0).trim()).append(" ");
                sb.append(" -c:a ").append(args.get(1).trim()).append(" ");
                sb.append(" -c:s ").append(args.get(2).trim()).append(" ");
            }
            default -> sb.append(" ");
        }

        sb.append(CmdUtil.buildQuotationMarks(outputFolder + fileName));
        try {
            return CmdUtil.run(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private String buildFfmpegPath(String filePath) {
        Path path = Paths.get("./ffmpeg.exe");
        if (Files.exists(path)) {
            return CmdUtil.buildQuotationMarks(path.toString());
        }
        return CmdUtil.buildQuotationMarks(filePath);
    }

}
