package com.mkframe.step.models;

import com.mkframe.step.base.BaseModel;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class SignModel extends BaseModel {
    private static final long serialVersionUID = 2988074485401161262L;

    private String uuid;
    private String nickName;
    private String gender;
    private String city;
    private String headImg;

    public SignModel(String nickName, String gender, String city) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
