package com.westepper.step.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/13.
 */

public class City implements Serializable {

    private String cityName;
    private List<Area> areaList = new ArrayList<>();

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setArea(Area area){
        areaList.add(area);
    }
}
