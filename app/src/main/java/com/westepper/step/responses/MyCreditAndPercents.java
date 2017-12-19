package com.westepper.step.responses;

import com.westepper.step.base.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class MyCreditAndPercents implements Serializable {
    private int type;
    private int credit;
    private int L1, L2, L3, L4;
    private int discoverCitys, L1Count, L2Count, L3Count;
    private List<AchieveProgress> percentList;
    private List<CityProgress> discoverCityList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getL1() {
        return type == Constants.ACH_BADGE ? L1 : discoverCitys;
    }

    public void setL1(int l1) {
            L1 = l1;
    }

    public int getL2() {
        return type == Constants.ACH_BADGE ? L2 : L1Count;
    }

    public void setL2(int l2) {
        L2 = l2;
    }

    public int getL3() {
        return type == Constants.ACH_BADGE ? L3 : L2Count;
    }

    public void setL3(int l3) {
        L3 = l3;
    }

    public int getL4() {
        return type == Constants.ACH_BADGE ? L4 : L3Count;
    }

    public void setL4(int l4) {
        L4 = l4;
    }

    public List<AchieveProgress> getPercentList() {
        return percentList;
    }

    public void setPercentList(List<AchieveProgress> percentList) {
        this.percentList = percentList;
    }

    public void setDiscoverCityList(List<CityProgress> discoverCityList) {
        this.discoverCityList = discoverCityList;
    }

    public List<CityProgress> getDiscoverCityList() {
        return discoverCityList;
    }

    public void setDiscoverCitys(int discoverCitys) {
        this.discoverCitys = discoverCitys;
    }

    public void setL1Count(int l1Count) {
        L1Count = l1Count;
    }

    public void setL2Count(int l2Count) {
        L2Count = l2Count;
    }

    public void setL3Count(int l3Count) {
        L3Count = l3Count;
    }

    public void setL2percent(List<L2Progress> l2percent) {
        discoverCityList.clear();
        for(L2Progress lp : l2percent){
            CityProgress cp = new CityProgress();
            cp.setCityName(lp.getAreaName());
            cp.setReachedPercent(lp.getPercent());
            cp.setPoint(lp.getPoint());
            discoverCityList.add(cp);
        }
    }
}
