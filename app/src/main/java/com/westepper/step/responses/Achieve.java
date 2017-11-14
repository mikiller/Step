package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/10/13.
 */

public class Achieve implements Parcelable {
    private String achieveCategory_id;
    private String achieveKind;
    private List<AchieveArea> achieveAreaList = new ArrayList<>();
    private Map<String, AchieveArea> achieveAreaMap;

    public Achieve(String id, String achieveKind) {
        this.achieveCategory_id = id;
        this.achieveKind = achieveKind;
    }

    protected Achieve(Parcel in) {
        achieveCategory_id = in.readString();
        achieveKind = in.readString();
        achieveAreaList = in.createTypedArrayList(AchieveArea.CREATOR);
        achieveAreaMap = in.readHashMap(HashMap.class.getClassLoader());
    }

    public static final Creator<Achieve> CREATOR = new Creator<Achieve>() {
        @Override
        public Achieve createFromParcel(Parcel in) {
            return new Achieve(in);
        }

        @Override
        public Achieve[] newArray(int size) {
            return new Achieve[size];
        }
    };

    public List<AchieveArea> getAchieveAreaList() {
        return achieveAreaList;
    }

    public void setAchieveAreaList(List<AchieveArea> achieveAreaList) {
        this.achieveAreaList = achieveAreaList;
    }

    public String getAchieveCategory_id() {
        return achieveCategory_id;
    }

    public void setAchieveCategory_id(String achieveCategory_id) {
        this.achieveCategory_id = achieveCategory_id;
    }

    public void setAchieveArea(AchieveArea achieveArea) {
        this.achieveAreaList.add(achieveArea);
    }

    public String getAchieveKind() {
        return achieveKind;
    }

    public void setAchieveKind(String achieveKind) {
        this.achieveKind = achieveKind;
    }

    public void setAchieveAreaMap() {
        achieveAreaMap = new HashMap<>();
        for (AchieveArea achArea : achieveAreaList) {
            achieveAreaMap.put(achArea.getAchieveAreaId(), achArea);
        }

    }

    public AchieveArea getAchieveName(String id) {
        return achieveAreaMap.get(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(achieveCategory_id);
        dest.writeString(achieveKind);
        dest.writeTypedList(achieveAreaList);
        dest.writeMap(achieveAreaMap);
    }
}
