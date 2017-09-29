package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/9/27.
 */

public class Commit implements Serializable {
    private String discoveryId;
    private String commitId;
    private String headUrl;
    private String nickName;
    private String msg;
    private long time;

    public String getDiscoveryId() {
        return discoveryId;
    }

    public void setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String id) {
        this.commitId = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
