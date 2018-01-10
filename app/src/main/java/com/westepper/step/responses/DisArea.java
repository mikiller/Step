package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/12/28.
 */

public class DisArea extends Graphy {

    private String id;
    private String name;
    private String p_id;
    private List<List<LatLng>> coords = new ArrayList<>();
    private LatLng center;
    private List<String> areaIds;
    private List<String> districtIds;


    protected DisArea(Parcel in) {
        id = in.readString();
        name = in.readString();
        p_id = in.readString();
        center = in.readParcelable(LatLng.class.getClassLoader());
        areaIds = in.createStringArrayList();
        districtIds = in.createStringArrayList();
    }

    public static final Creator<DisArea> CREATOR = new Creator<DisArea>() {
        @Override
        public DisArea createFromParcel(Parcel in) {
            return new DisArea(in);
        }

        @Override
        public DisArea[] newArray(int size) {
            return new DisArea[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public List<List<LatLng>> getCoords() {
        return coords;
    }

    public void setCoords(List<List<LatLng>> coords) {
        this.coords = coords;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public List<String> getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(List<String> areaIds) {
        this.areaIds = areaIds;
    }

    public List<String> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(List<String> districtIds) {
        this.districtIds = districtIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(p_id);
        dest.writeParcelable(center, flags);
        dest.writeStringList(areaIds);
        dest.writeStringList(districtIds);
    }


    @Override
    public void createGraphics(AMap aMap, int graphicType) {
        graphics = new Graphics(aMap, coords == null ? new ArrayList<List<LatLng>>() : coords);
        setGraphicsType(graphicType);
        setReached(false);
        hide();
    }
}
