package com.westepper.step.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/13.
 */

public class Achieve implements Serializable {

    private String achieveKind;
    private List<AchieveArea> achieveAreaList = new ArrayList<>();

    public List<AchieveArea> getAchieveAreaList() {
        return achieveAreaList;
    }

    public void setAchieveAreaList(List<AchieveArea> achieveAreaList) {
        this.achieveAreaList = achieveAreaList;
    }

    public void setAchieveArea(AchieveArea achieveArea){
        this.achieveAreaList.add(achieveArea);
    }

    public String getAchieveKind() {
        return achieveKind;
    }

    public void setAchieveKind(String achieveKind) {
        this.achieveKind = achieveKind;
    }
}
