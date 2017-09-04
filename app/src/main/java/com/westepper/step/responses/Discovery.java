package com.westepper.step.responses;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/9/4.
 */

public class Discovery implements Serializable {
    private static final long serialVersionUID = -4853212901442372136L;

    private String discoveryId;
    private int discoveryKind;
    private String nickName;
    private int gender;
    private String headUrl;
    private String info;
    private LatLng latlng;
    private List<String> imgList;
    private long pushTime;
    private long endTime;
    private int joinCount;

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

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getPushTime() {
        return pushTime;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }
}
