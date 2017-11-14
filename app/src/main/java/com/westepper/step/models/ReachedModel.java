package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/14.
 */

public class ReachedModel extends BaseModel {
    private String achievementId;

    public ReachedModel(String achievementId) {
        this.achievementId = achievementId;
    }

    public String getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(String achievementId) {
        this.achievementId = achievementId;
    }
}
