package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/8/29.
 */

public class MapData implements Parcelable {
    private String version;
    private List<City> cityList = new ArrayList<>();
    private List<Area> achievementAreaList = new ArrayList<>();
    private List<Achieve> achievementList = new ArrayList<>();

    public MapData() {
    }


    protected MapData(Parcel in) {
        version = in.readString();
        cityList = in.createTypedArrayList(City.CREATOR);
        achievementAreaList = in.createTypedArrayList(Area.CREATOR);
        achievementList = in.createTypedArrayList(Achieve.CREATOR);
    }

    public static final Creator<MapData> CREATOR = new Creator<MapData>() {
        @Override
        public MapData createFromParcel(Parcel in) {
            return new MapData(in);
        }

        @Override
        public MapData[] newArray(int size) {
            return new MapData[size];
        }
    };

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public void setCity(City city){
        cityList.add(city);
    }

    public List<Area> getAchievementAreaList() {
        return achievementAreaList;
    }

    public void setAchievementAreaList(List<Area> achievementAreaList) {
        this.achievementAreaList = achievementAreaList;
    }

    public List<Achieve> getAchievementList() {
        return achievementList;
    }

    public void setAchievementList(List<Achieve> achievementList) {
        this.achievementList = achievementList;
    }

    public void setAchieve(Achieve area){
        achievementList.add(area);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(version);
        dest.writeTypedList(cityList);
        dest.writeTypedList(achievementAreaList);
        dest.writeTypedList(achievementList);
    }


}
