package com.westepper.step.responses;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/8/29.
 */

public class Area implements Serializable{
    private static final long serialVersionUID = 424384235978049417L;

    public static final int CIRCLE = 2, POLYGON = 1;
    private String areaId;
    private int areaType;
    private List<LatLng> borderList = new ArrayList<>();
    private CirlclArea circle;
    private boolean reached = false;

    private Graphics graphics;

    public Area(String id) {
        this.areaId = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String id) {
        this.areaId = id;
    }

    public List<LatLng> getBorderList() {
        return borderList;
    }

    public int getAreaType() {
        return areaType;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public void setBorderList(List<LatLng> borderList) {
        this.borderList = borderList;
    }

    public void setBorderCoord(double lat, double lon){
        borderList.add(new LatLng(lat, lon));
    }

    public CirlclArea getCircle() {
        return circle;
    }

    public void setCircle(CirlclArea circle) {
        this.circle = circle;
    }

    public boolean isReached() {
        return reached;
    }

    public Area setReached(boolean reached) {
        this.reached = reached;
        graphics.setReached(reached);
        return this;
    }

    public void setGraphicsType(int type){
        graphics.setGraphicsType(type);
        graphics.setReached(reached);
    }

    public void createGraphics(AMap aMap){
        if(areaType == Area.POLYGON){
            graphics = new Graphics(aMap, reached, borderList);
        }else if(areaType == Area.CIRCLE){
            graphics = new Graphics(aMap, reached, circle.getLatng(), circle.getRadius());
        }
    }

    public void hide(){
        graphics.hide(reached);
    }

    public void show(){
        graphics.show(reached);
    }

    public static class CirlclArea implements Parcelable {
        private LatLng latng;
        private int radius;

        public CirlclArea(LatLng latng, int radius) {
            this.latng = latng;
            this.radius = radius;
        }

        protected CirlclArea(Parcel in) {
            latng = in.readParcelable(LatLng.class.getClassLoader());
            radius = in.readInt();
        }

        public static final Creator<CirlclArea> CREATOR = new Creator<CirlclArea>() {
            @Override
            public CirlclArea createFromParcel(Parcel in) {
                return new CirlclArea(in);
            }

            @Override
            public CirlclArea[] newArray(int size) {
                return new CirlclArea[size];
            }
        };

        public LatLng getLatng() {
            return latng;
        }

        public void setLatng(LatLng latng) {
            this.latng = latng;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(latng, flags);
            dest.writeInt(radius);
        }
    }
}
