package com.westepper.step.models;

/**
 * Created by Mikiller on 2018/1/11.
 */

public class ComplaintModel extends DisBase {
    private int type;
    private String content;

    public ComplaintModel(String discoveryId, int discoveryKind, int type, String content) {
        super(discoveryId, discoveryKind);
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
