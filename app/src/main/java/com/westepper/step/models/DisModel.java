package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/3.
 */

public class DisModel extends BaseModel {
    private String discoveryId;
    private int discoveryKind;
    private int page;
    private int type;

    public DisModel(String discoveryId, int discoveryKind) {
        this.discoveryId = discoveryId;
        this.discoveryKind = discoveryKind;
    }

    public DisModel(int discoveryKind, int page) {
        this.discoveryKind = discoveryKind;
        this.page = page;
    }

    public DisModel(int discoveryKind, int page, int type) {
        this.discoveryKind = discoveryKind;
        this.page = page;
        this.type = type;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
