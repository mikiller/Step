package com.westepper.step.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/8/29.
 */

public class MapData implements Serializable {
    private static final long serialVersionUID = 1141207758735809623L;
    private List<City> cityList = new ArrayList<>();
    private List<Achieve> achievementList = new ArrayList<>();

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public void setCity(City city){
        cityList.add(city);
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
}
