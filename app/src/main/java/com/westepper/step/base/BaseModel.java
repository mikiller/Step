package com.westepper.step.base;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/5/24.
 */

public class BaseModel implements Serializable {
    private static final long serialVersionUID = -7989127778033177550L;
    protected String userId;
    protected String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
