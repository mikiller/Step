package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class CommitModel extends DeleteCommitModel {
    private String commitUserId;
    private String msg;
    private long time;

    public CommitModel(String discoveryId, int discoveryKind) {
        super(discoveryId, discoveryKind);
    }

    public CommitModel(String commitUserId, String discoveryId, int discoveryKind, String msg) {
        super(discoveryId, discoveryKind);
        this.commitUserId = commitUserId;
        this.msg = msg;
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

    public long getTime() {
        return time * 1000l;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
