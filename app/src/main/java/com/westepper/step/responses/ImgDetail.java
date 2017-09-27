package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/9/26.
 */

public class ImgDetail implements Serializable {
    String url;
    int width, height;

    public ImgDetail(String url, int height, int width) {
        this.height = height;
        this.url = url;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
