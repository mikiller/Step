package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class MyAchievements implements Serializable {
    private int credit;
    private int L1, L2, L3, L4;
    private List<AchieveProgress> percentList;

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getL1() {
        return L1;
    }

    public void setL1(int l1) {
        L1 = l1;
    }

    public int getL2() {
        return L2;
    }

    public void setL2(int l2) {
        L2 = l2;
    }

    public int getL3() {
        return L3;
    }

    public void setL3(int l3) {
        L3 = l3;
    }

    public int getL4() {
        return L4;
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
}
