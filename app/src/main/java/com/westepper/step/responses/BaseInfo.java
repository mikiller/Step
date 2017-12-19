package com.westepper.step.responses;

import com.westepper.step.utils.MXTimeUtils;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class BaseInfo implements Serializable {
    protected long created_at;
    protected long reached_at;

    public String getFormatCreated_at() {
        return MXTimeUtils.getFormatTime("yy/MM/dd", created_at * 1000l);
    }

    public String getFormatReached_at(){
        return MXTimeUtils.getFormatTime("yy/MM/dd", reached_at * 1000);
    }

    public long getCreated_at(){
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getReached_at() {
        return reached_at;
    }

    public void setReached_at(long reached_at) {
        this.reached_at = reached_at;
    }
}
