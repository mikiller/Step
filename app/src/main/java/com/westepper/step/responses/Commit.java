package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/9/27.
 */

public class Commit implements Serializable {
    private String headerUrl;
    private String nickName;
    private String commit;
    private long commitTime;

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public long getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(long commitTime) {
        this.commitTime = commitTime;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
