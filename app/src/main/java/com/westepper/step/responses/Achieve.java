package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/13.
 */

public class Achieve implements Parcelable {

    private String achieveKind;
    private List<AchieveArea> achieveAreaList = new ArrayList<>();

    public Achieve(String achieveKind) {
        this.achieveKind = achieveKind;
    }

    protected Achieve(Parcel in) {
        achieveKind = in.readString();
        achieveAreaList = in.createTypedArrayList(AchieveArea.CREATOR);
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

    public void setAchieveArea(AchieveArea achieveArea){
        this.achieveAreaList.add(achieveArea);
    }

    public String getAchieveKind() {
        return achieveKind;
    }

    public void setAchieveKind(String achieveKind) {
        this.achieveKind = achieveKind;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(achieveKind);
        dest.writeTypedList(achieveAreaList);
    }
}
