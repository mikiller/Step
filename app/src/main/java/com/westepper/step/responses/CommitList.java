package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class CommitList implements Serializable {
    private int commitNum;
    private List<Commit> commitList;

    public List<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<Commit> commitList) {
        this.commitList = commitList;
    }

    public int getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(int commitNum) {
        this.commitNum = commitNum;
    }
}
