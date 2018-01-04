package com.westepper.step.responses;

import com.westepper.step.base.BaseModel;

import java.io.Serializable;

/**
 * Created by Mikiller on 2018/1/4.
 */

public class SimpleUser implements Serializable {
    private String id;
    private String nickName;
    private String headImg;
    private long created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getCreated_at() {
        return created_at * 1000l;
    }

    public void setCreated_at(long create_at) {
        this.created_at = create_at;
    }
}
