package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/12/19.
 */

public class AchievementBaseInfo implements Serializable {
    private List<MyAchieve> reachedList;
    private List<MyAchieve> reachedAchievementList;

    public List<MyAchieve> getReachedAchievementList() {
        return reachedAchievementList;
    }

    public void setReachedAchievementList(List<MyAchieve> reachedAchievementList) {
        this.reachedAchievementList = reachedAchievementList;
    }

    public List<MyAchieve> getReachedList() {
        return reachedList;
    }

    public void setReachedList(List<MyAchieve> reachedList) {
        this.reachedList = reachedList;
    }
}
