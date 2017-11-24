package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/2.
 */

public class Privacy extends BaseModel {
    public static final int ONLY_ME = 0, ALL = 1, FRIEND = 2;
    private int needFriendVerifi;
    private int moodScope;
    private int outgoScope;

    public Privacy(int moodScope, int needFriendVerifi, int outgoScope) {
        this.moodScope = moodScope;
        this.needFriendVerifi = needFriendVerifi;
        this.outgoScope = outgoScope;
    }

    public Privacy(Privacy src) {
        super();
        setMoodScope(src.getMoodScope());
        setOutgoScope(src.getOutgoScope());
        setNeedFriendVerifi(src.getNeedFriendVerifi());
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
