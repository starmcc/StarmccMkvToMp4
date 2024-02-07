package com.starmcc.mkv.to.mp4.frame;

import java.net.URL;

public enum StageEnum {
    main("QmMkvToMp4 - " + StarmccConstant.VERSION_NAME),
    ;

    private String title;
    private Integer width;
    private Integer height;


    StageEnum(String title) {
        this.title = title;
    }


    StageEnum(String title, Integer width, Integer height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public URL buildPath() {
        return getClass().getResource(this.name() + ".fxml");
    }
}
