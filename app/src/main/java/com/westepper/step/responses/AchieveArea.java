package com.westepper.step.responses;

import android.text.TextUtils;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/13.
 */

public class AchieveArea implements Serializable {

    private String achieveAreaId;
    private String achieveName;
    private String areaIds;

    public String getAchieveAreaId() {
        return achieveAreaId;
    }

    public void setAchieveAreaId(String achieveAreaId) {
        this.achieveAreaId = achieveAreaId;
    }

    public String getAchieveName() {
        return achieveName;
    }

    public void setAchieveName(String achieveName) {
        this.achieveName = achieveName;
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
