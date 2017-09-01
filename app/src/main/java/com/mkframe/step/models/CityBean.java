package com.mkframe.step.models;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityBean extends BaseIndexPinyinBean {

    private int cityID;
    private String cityName;//城市名字
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的

    public CityBean() {
    }

    public CityBean(String city) {
        this.cityName = city;
    }

    public CityBean setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public CityBean setTop(boolean top) {
        isTop = top;
        return this;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    @Override
    public String getTarget() {
        return cityName;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
