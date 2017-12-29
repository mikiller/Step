package com.westepper.step.responses;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/12/27.
 */

public class ReachedId implements Serializable {
    private String id;
    private int is_reached;

    public ReachedId(String id, int is_reached) {
        this.id = id;
        this.is_reached = is_reached;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIs_reached() {
        return is_reached;
    }

    public void setIs_reached(int is_reached) {
        this.is_reached = is_reached;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReachedId) {
            return id.equals(((ReachedId) obj).getId());
        }else
          return id.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
