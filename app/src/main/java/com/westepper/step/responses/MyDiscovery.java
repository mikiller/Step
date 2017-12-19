package com.westepper.step.responses;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class MyDiscovery extends BaseInfo {
    protected int areaId;
    protected String areaName;
    protected String city_name;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return "点亮" + areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCity_name() {
        return "发现" + city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
