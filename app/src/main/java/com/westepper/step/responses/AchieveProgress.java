package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/24.
 */

public class AchieveProgress implements Serializable {
    private int type;
    private String name;
    private int percent;
    private int iconId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
