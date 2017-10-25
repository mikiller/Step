package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/8/29.
 */

public class Area implements Parcelable{

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

    protected Area(Parcel in) {
        areaId = in.readString();
        areaType = in.readInt();
        borderList = in.createTypedArrayList(LatLng.CREATOR);
        circle = in.readParcelable(CirlclArea.class.getClassLoader());
        reached = in.readByte() != 0;
    }

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };

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

    public void createGraphics(AMap aMap, int graphicType){
        if(areaType == Area.POLYGON){
            graphics = new Graphics(aMap, reached, borderList);
        }else if(areaType == Area.CIRCLE){
            graphics = new Graphics(aMap, reached, circle.getLatlng(), circle.getRadius());
        }
        setGraphicsType(graphicType);
    }

    public void hide(){
        graphics.hide(reached);
    }

    public void show(){
        graphics.show(reached);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaId);
        dest.writeInt(areaType);
        dest.writeTypedList(borderList);
        dest.writeParcelable(circle, flags);
        dest.writeByte((byte) (reached ? 1 : 0));
    }

    public static class CirlclArea implements Parcelable {
        private LatLng latlng;
        private int radius;

        public CirlclArea(LatLng latng, int radius) {
            this.latlng = latng;
            this.radius = radius;
        }

        protected CirlclArea(Parcel in) {
            latlng = in.readParcelable(LatLng.class.getClassLoader());
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

        public LatLng getLatlng() {
            return latlng;
        }

        public void setLatlng(LatLng latlng) {
            this.latlng = latlng;
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
            dest.writeParcelable(latlng, flags);
            dest.writeInt(radius);
        }
    }
}
