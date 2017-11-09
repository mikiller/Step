package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class SignModel extends BaseModel {
    private static final long serialVersionUID = 2988074485401161262L;

    protected String uuid;
    protected String nickName;
    protected int gender;
    protected String city;
    protected String headImg;


    public SignModel() {
    }

    public SignModel(String uuid) {
        this.uuid = uuid;
    }

    public SignModel(String nickName, int gender, String city) {
        this.nickName = nickName;
        this.gender = gender;
        this.city = city;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
