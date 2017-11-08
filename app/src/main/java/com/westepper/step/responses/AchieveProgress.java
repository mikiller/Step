package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/24.
 */

public class AchieveProgress implements Serializable {

    private int categoryId;
    private String categoryName;
    private int percent;
//    private int iconId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }



//    public int getIconId() {
//        return iconId;
//    }
//
//    public void setIconId(int iconId) {
//        this.iconId = iconId;
//    }
}
