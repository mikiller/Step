package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class JoinResponse implements Serializable {
    private int joinCount;

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }
}
