package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/12/19.
 */

public class MyAchieve extends BaseInfo {
    protected int achievementId;
    protected String achievementName;

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public String getAchievementName() {
        return "完成" + achievementName;
    }

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }
}
