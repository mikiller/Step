package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/11/16.
 */

public class DiscoveryDetailInfo implements Serializable {
    private int goodNum;
    private int isGood;
    private int joinCount;
    private int isJoin;

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public boolean isGood() {
        return isGood == 1;
    }

    public void setGood(int good) {
        isGood = good;
    }

    public boolean isJoin() {
        return isJoin == 1;
    }

    public void setJoin(int join) {
        isJoin = join;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }
}
