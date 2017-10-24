package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/24.
 */

public class MapDataModel extends BaseModel {
    private String versionCode;

    public MapDataModel(String versionCode) {
        super();
        this.versionCode = versionCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
