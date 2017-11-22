package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/27.
 */

public class NewDiscoveryModel extends BaseModel {
    private int scope;
    private int discoveryKind;
    private String info;
    private List<String> imgList = new ArrayList<>();
    private String poiTitle;
    private double latitude;
    private double longitude;
    private long pushTime;
    private long endTime;
    private int totalCount;
    private String teamId;

    public NewDiscoveryModel(int discoveryKind, String info, long pushTime) {
        this.discoveryKind = discoveryKind;
        this.info = info;
        this.pushTime = pushTime/1000;
    }

    public NewDiscoveryModel(int scope, int discoveryKind, String info, long pushTime, long endTime, int totalCount) {
        this(discoveryKind, info, pushTime);
        this.scope = scope;
        this.endTime = endTime;
        this.totalCount = totalCount;
    }

    public int getDiscoveryKind() {
        return discoveryKind;
    }

    public void setDiscoveryKind(int discoveryKind) {
        this.discoveryKind = discoveryKind;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public void addImg(String img){
        imgList.add(img);
    }

    public void addImgs(List<String> imgs){
        imgList.addAll(imgs);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public String getPoiTitle() {
        return poiTitle;
    }

    public void setPoiTitle(String poiTitle) {
        this.poiTitle = poiTitle;
    }

    public long getPushTime() {
        return pushTime;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
