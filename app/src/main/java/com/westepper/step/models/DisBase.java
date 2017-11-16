package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/16.
 */

public class DisBase extends BaseModel {
    protected String discoveryId;
    protected int discoveryKind;

    public DisBase(int discoveryKind) {
        this.discoveryKind = discoveryKind;
    }

    public DisBase(String discoveryId, int discoveryKind) {
        this.discoveryId = discoveryId;
        this.discoveryKind = discoveryKind;
    }

    public String getDiscoveryId() {
        return discoveryId;
    }

    public void setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
    }

    public int getDiscoveryKind() {
        return discoveryKind;
    }

    public void setDiscoveryKind(int discoveryKind) {
        this.discoveryKind = discoveryKind;
    }
}
