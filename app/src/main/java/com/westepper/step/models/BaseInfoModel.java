package com.westepper.step.models;

import com.westepper.step.base.BaseModel;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class BaseInfoModel extends BaseModel {
    private int type;
    private int categoryId;

    public BaseInfoModel(int type) {
        this.type = type;
    }

    public BaseInfoModel(int type, int categoryId) {
        this.type = type;
        this.categoryId = categoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
