package com.starmcc.mkv.to.mp4;


import com.starmcc.mkv.to.mp4.frame.FxManager;
import com.starmcc.mkv.to.mp4.frame.StageEnum;
import javafx.application.Application;
import javafx.stage.Stage;

public class StarmccMkvToMp4Application extends Application {
    /**
     * 启动应用程序 (入口)
     *
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FxManager.open(primaryStage, StageEnum.main);
    }
}
