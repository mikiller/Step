package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/23.
 */

public class AchCity implements Serializable {

    int type;
    String title, date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
