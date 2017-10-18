package com.westepper.step.responses;

import com.westepper.step.models.SignModel;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/10/10.
 */

public class UserInfo extends SignModel {

    private String cover;
    private String sign;
    private int needFriendVerifi;
    private int moodScope;
    private int outgoScope;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getNeedFriendVerifi() {
        return needFriendVerifi;
    }

    public void setNeedFriendVerifi(int needFriendVerifi) {
        this.needFriendVerifi = needFriendVerifi;
    }

    public int getMoodScope() {
        return moodScope;
    }

    public void setMoodScope(int moodScope) {
        this.moodScope = moodScope;
    }

    public int getOutgoScope() {
        return outgoScope;
    }

    public void setOutgoScope(int outgoScope) {
        this.outgoScope = outgoScope;
    }
}
