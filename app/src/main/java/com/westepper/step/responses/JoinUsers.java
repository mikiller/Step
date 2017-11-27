package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/27.
 */

public class JoinUsers implements Serializable {
    List<UserInfo> JoinUser;

    public List<UserInfo> getJoinUser() {
        return JoinUser;
    }

    public void setJoinUser(List<UserInfo> joinUser) {
        JoinUser = joinUser;
    }
}
