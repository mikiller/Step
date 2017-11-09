package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/9.
 */

public class RankModel extends BaseModel {
    private int rankType;

    public RankModel(int rankType) {
        this.rankType = rankType;
    }

    public int getRankType() {
        return rankType;
    }

    public void setRankType(int rankType) {
        this.rankType = rankType;
    }
}
