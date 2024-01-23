package com.starmcc.mkv.to.mp4.frame;

import com.starmcc.mkv.to.mp4.utils.PropertiesUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

public class StarmccConstant {

    public static final JFXStage STAGE_CACHE = new JFXStage();

    public static TextArea consoleTextArea = null;
    public static int selectType = 0;
    public static String inputVideoPath = "";
    public static String outputFolderPath = "";
    public static String defaultInput = "";
    public static ObservableList<? extends String> selectList = FXCollections.observableArrayList();
    public static String videoCode = "";
    public static String audioCode = "";
    public static String subCode = "";
    public static boolean consoleWindowStatus = true;

    public static final String UPDATE_URL = "https://api.github.com/repos/starmcc/StarmccMkvToMp4/releases/latest";
    public static final String GITHUB_URL = "https://api.github.com/starmcc/StarmccMkvToMp4";

    public static final String VERSION_NAME = "v" + PropertiesUtil.readData("app.version");


    public static final String FFMPEG_PATH = "./bin/ffmpeg.exe";
}
