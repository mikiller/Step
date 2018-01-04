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
    private String discoveryUserId;
    private String nickName;
    private int gender;
    private String headUrl;
    private String info;
    private UserPos userPos;
    private List<ImgDetail> imgList;
    private long goodNum;
    private int isGood;
    private long commitNum;
    private int isJoin;
    private long pushTime;
    private long endTime;
    private int totalCount;
    private int joinCount;
    private String teamId;



    public Discovery(){}

    protected Discovery(Parcel in) {
        discoveryId = in.readString();
        discoveryKind = in.readInt();
        discoveryUserId = in.readString();
        nickName = in.readString();
        gender = in.readInt();
        headUrl = in.readString();
        info = in.readString();
        userPos = in.readParcelable(UserPos.class.getClassLoader());
        imgList = in.createTypedArrayList(ImgDetail.CREATOR);
        isGood = in.readInt();
        goodNum = in.readLong();
        commitNum = in.readLong();
        isJoin = in.readInt();
        pushTime = in.readLong();
        endTime = in.readLong();
        totalCount = in.readInt();
        joinCount = in.readInt();
        teamId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(discoveryId);
        dest.writeInt(discoveryKind);
        dest.writeString(discoveryUserId);
        dest.writeString(nickName);
        dest.writeInt(gender);
        dest.writeString(headUrl);
        dest.writeString(info);
        dest.writeParcelable(userPos, flags);
        dest.writeTypedList(imgList);
        dest.writeInt(isGood);
        dest.writeLong(goodNum);
        dest.writeLong(commitNum);
        dest.writeInt(isJoin);
        dest.writeLong(pushTime);
        dest.writeLong(endTime);
        dest.writeInt(totalCount);
        dest.writeInt(joinCount);
        dest.writeString(teamId);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getDiscoveryUserId() {
        return discoveryUserId;
    }

    public void setDiscoveryUserId(String discoveryUserId) {
        this.discoveryUserId = discoveryUserId;
    }

    public long getEndTime() {
        return endTime * 1000l;
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

    public List<ImgDetail> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgDetail> imgList) {
        this.imgList = imgList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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

    public boolean isJoin() {
        return isJoin == 1;
    }

    public void setJoin(int join) {
        isJoin = join;
    }

    public long getPushTime() {
        return pushTime * 1000l;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }

    public long getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(long commitNum) {
        this.commitNum = commitNum;
    }

    public boolean getIsGood() {
        return isGood == 1;
    }

    public void setIsGood(boolean isGood) {
        this.isGood = isGood ? 1 : 0;
    }

    public long getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(long goodNum) {
        this.goodNum = goodNum;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setUserInfo(UserInfo userInfo){
        setNickName(userInfo.getNickName());
        setGender(userInfo.getGender());
        setHeadUrl(userInfo.getHeadImg());
        setDiscoveryUserId(userInfo.getUserId());
    }

}
