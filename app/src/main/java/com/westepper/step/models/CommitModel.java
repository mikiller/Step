package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class CommitModel extends BaseModel {
    private int discoveryKind;
    private String discoveryId;
    private String commitUserId;
    private String msg;

    public CommitModel(String discoveryId, int discoveryKind) {
        this.discoveryId = discoveryId;
        this.discoveryKind = discoveryKind;
    }

    public CommitModel(String commitUserId, String discoveryId, int discoveryKind, String msg) {
        this.commitUserId = commitUserId;
        this.discoveryId = discoveryId;
        this.discoveryKind = discoveryKind;
        this.msg = msg;
    }

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

    public String getCommitUserId() {
        return commitUserId;
    }

    public void setCommitUserId(String commitUserId) {
        this.commitUserId = commitUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
