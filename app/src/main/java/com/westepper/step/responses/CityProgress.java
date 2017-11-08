package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class CityProgress implements Serializable {
    private String cityName;
    private double rechedPercent;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getRechedPercent() {
        return rechedPercent;
    }

    public void setRechedPercent(double rechedPercent) {
        this.rechedPercent = rechedPercent;
    }
}
