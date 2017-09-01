package com.mkframe.step.responses;

import android.graphics.Color;

import com.amap.api.maps.AMap;
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

    private String id;
    private List<LatLng> borderCoords = new ArrayList<>();
    private Polygon polygon;
    private boolean hasChecked = false;

    public Area(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<LatLng> getBorderCoords() {
        return borderCoords;
    }

    public void setBorderCoords(List<LatLng> borderCoords) {
        this.borderCoords = borderCoords;
    }

    public void setBorderCoord(double lat, double lon){
        borderCoords.add(new LatLng(lat, lon));
    }

    public boolean isHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

    public void createPolygon(AMap aMap){
        PolygonOptions opt = new PolygonOptions().addAll(borderCoords);
        if(hasChecked){
            opt.strokeWidth(0f)
                    .fillColor(Color.parseColor("#6600A8FF"));
        }else{
            opt.strokeWidth(4)
                    .strokeColor(Color.parseColor("#D2D2D2"))
                    .fillColor(Color.TRANSPARENT);
        }
        polygon = aMap.addPolygon(opt);
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void hide(){
        if(hasChecked){
            polygon.setFillColor(Color.TRANSPARENT);
        }else{
            polygon.setStrokeWidth(0);
        }
    }

    public void show(){
        if(hasChecked){
            polygon.setFillColor(Color.parseColor("#6600A8FF"));
        }else{
            polygon.setStrokeWidth(4);
        }
    }
}
