package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/13.
 */

public class AchieveArea implements Parcelable {

    private String achieveAreaId;
    private String achieveAreaName;
    private String achievementCenter;
    private String title;
    private String desc;
    private String credit_level;
    private String credit_coefficient;
    private String category_id;
    private String areaIds;

    public AchieveArea() {
    }

    protected AchieveArea(Parcel in) {
        achieveAreaId = in.readString();
        achieveAreaName = in.readString();
        achievementCenter = in.readString();
        title = in.readString();
        desc = in.readString();
        credit_level = in.readString();
        credit_coefficient = in.readString();
        category_id = in.readString();
        areaIds = in.readString();
    }

    public static final Creator<AchieveArea> CREATOR = new Creator<AchieveArea>() {
        @Override
        public AchieveArea createFromParcel(Parcel in) {
            return new AchieveArea(in);
        }

        @Override
        public AchieveArea[] newArray(int size) {
            return new AchieveArea[size];
        }
    };

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

    public int getScore(){
        return Integer.parseInt(credit_level) * Integer.parseInt(credit_coefficient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(achieveAreaId);
        dest.writeString(achieveAreaName);
        dest.writeString(achievementCenter);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(credit_level);
        dest.writeString(credit_coefficient);
        dest.writeString(category_id);
        dest.writeString(areaIds);
    }
}
