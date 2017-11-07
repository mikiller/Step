package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class JoinModel extends BaseModel {
    private String discoveryId;
    private String teamId;

    public JoinModel(String discoveryId, String teamId) {
        this.discoveryId = discoveryId;
        this.teamId = teamId;
    }

    public String getDiscoveryId() {
        return discoveryId;
    }

    public void setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
