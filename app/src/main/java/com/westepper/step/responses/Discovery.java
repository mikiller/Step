package com.westepper.step.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/9/4.
 */

public class Discovery implements Parcelable {

    private String discoveryId;
    private int discoveryKind;
    private String nickName;
    private int gender;
    private String headUrl;
    private String info;
    private UserPos userPos;
    private List<String> imgList;
    private long pushTime;
    private long endTime;
    private int joinCount;

    public Discovery(){}

    protected Discovery(Parcel in) {
        discoveryId = in.readString();
        discoveryKind = in.readInt();
        nickName = in.readString();
        gender = in.readInt();
        headUrl = in.readString();
        info = in.readString();
        userPos = in.readParcelable(LatLng.class.getClassLoader());
        imgList = in.createStringArrayList();
        pushTime = in.readLong();
        endTime = in.readLong();
        joinCount = in.readInt();
    }

    public static final Creator<Discovery> CREATOR = new Creator<Discovery>() {
        @Override
        public Discovery createFromParcel(Parcel in) {
            return new Discovery(in);
        }

        @Override
        public Discovery[] newArray(int size) {
            return new Discovery[size];
        }
    };

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

    public UserPos getUserPos() {
        return userPos;
    }

    public void setUserPos(UserPos userPos) {
        this.userPos = userPos;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(discoveryId);
        dest.writeInt(discoveryKind);
        dest.writeString(nickName);
        dest.writeInt(gender);
        dest.writeString(headUrl);
        dest.writeString(info);
        dest.writeParcelable(userPos, flags);
        dest.writeStringList(imgList);
        dest.writeLong(pushTime);
        dest.writeLong(endTime);
        dest.writeInt(joinCount);
    }
}
