package com.starmcc.mkv.to.mp4.entity;



import com.alibaba.fastjson2.annotation.JSONField;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


public class UpdateModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 343098565547813845L;
    private String errRequestMsg;
    @JSONField(name="tag_name")
    private String tagName;
    private String body;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
