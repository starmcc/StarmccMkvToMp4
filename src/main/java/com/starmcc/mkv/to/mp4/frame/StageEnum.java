package com.starmcc.mkv.to.mp4.frame;

public enum StageEnum {
    Main("QmMkvToMp4 - " + StarmccConstant.VERSION_NAME),
    ;

    private String title;
    private Integer width;
    private Integer height;
    private StageEnum parent;


    StageEnum(String title) {
        this.title = title;
    }

    StageEnum(String title, StageEnum stageEnum) {
        this.title = title;
        this.parent = stageEnum;
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

    public StageEnum getParent() {
        return parent;
    }

    public String buildPath() {
        return "/ui/" + this.name() + ".fxml";
    }
}
