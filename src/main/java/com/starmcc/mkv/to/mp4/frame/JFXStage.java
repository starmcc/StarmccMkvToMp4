package com.starmcc.mkv.to.mp4.frame;

import javafx.stage.Stage;

import java.util.HashMap;

public class JFXStage extends HashMap<String, Stage> {
    public void put(StageEnum page, Stage stage) {
        this.put(page.name(), stage);
    }


    public Stage get(StageEnum page) {
        return this.get(page.name());
    }

    public void remove(StageEnum page) {
        this.remove(page.name());
    }

}
