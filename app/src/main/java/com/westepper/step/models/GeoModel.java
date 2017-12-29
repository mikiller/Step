package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

import java.util.List;

/**
 * Created by Mikiller on 2017/11/14.
 */

public class GeoModel extends BaseModel {
    private String areaId;

    public GeoModel(String areaIds) {
        areaId = areaIds;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
