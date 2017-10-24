package com.westepper.step.responses;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/13.
 */

public class AchieveArea implements Serializable {

    private String achieveAreaId;
    private String achieveAreaName;
    private String achievementCenter;
    private String title;
    private String desc;
    private String credit_level;
    private String credit_coefficient;
    private String category_id;
    private String areaIds;

    public String getAchieveAreaId() {
        return achieveAreaId;
    }

    public void setAchieveAreaId(String achieveAreaId) {
        this.achieveAreaId = achieveAreaId;
    }

    public String getAchieveAreaName() {
        return achieveAreaName;
    }

    public void setAchieveAreaName(String achieveAreaName) {
        this.achieveAreaName = achieveAreaName;
    }

    public String getAchievementCenter() {
        return achievementCenter;
    }

    public void setAchievementCenter(String achievementCenter) {
        this.achievementCenter = achievementCenter;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCredit_coefficient() {
        return credit_coefficient;
    }

    public void setCredit_coefficient(String credit_coefficient) {
        this.credit_coefficient = credit_coefficient;
    }

    public String getCredit_level() {
        return credit_level;
    }

    public void setCredit_level(String credit_level) {
        this.credit_level = credit_level;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAreaIds() {
        return TextUtils.isEmpty(areaIds) ? new String[]{} : areaIds.split(",");
    }

    public String getAreaId(int pos){
        return getAreaIds().length > pos ? getAreaIds()[pos] : "";
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }
}
