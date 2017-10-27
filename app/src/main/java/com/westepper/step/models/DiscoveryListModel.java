package com.westepper.step.models;

import com.amap.api.maps.model.LatLng;
import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/10/27.
 */

public class DiscoveryListModel extends BaseModel {
    private int gender;
    private int kind;
    private int scope;
    private double latitude;
    private double longitude;

    public DiscoveryListModel(int gender, int kind, int scope, double lat, double lon) {
        this.gender = gender;
        this.kind = kind;
        this.scope = scope;
        latitude = lat;
        longitude = lon;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }
}
