package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class BaseInfo implements Serializable {
    protected long created_at;

    public long getCreated_at() {
        return created_at * 1000l;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
