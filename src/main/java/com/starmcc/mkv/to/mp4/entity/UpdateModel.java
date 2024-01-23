package com.starmcc.mkv.to.mp4.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateModel implements Serializable {


    private static final long serialVersionUID = 343098565547813845L;
    private String errRequestMsg;
    private String tagName;
    private String body;


}
