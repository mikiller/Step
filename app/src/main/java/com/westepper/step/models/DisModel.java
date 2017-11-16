package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/3.
 */

public class DisModel extends DisBase {
    private int page;
    private int type;

    public DisModel(String discoveryId, int discoveryKind) {
        super(discoveryId, discoveryKind);
    }

    public DisModel(int discoveryKind, int page) {
        super(discoveryKind);
        this.page = page;
    }

    public DisModel(int discoveryKind, int page, int type) {
        super(discoveryKind);
        this.page = page;
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
