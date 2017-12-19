package com.westepper.step.logics;

import android.content.Context;

import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.responses.MyCreditAndPercents;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class GetMyCitiesLogic extends BaseLogic<MyCreditAndPercents> {
    public GetMyCitiesLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
    }

    @Override
    protected void setUrl() {

    }

    @Override
    public void sendRequest() {

    }

    @Override
    public boolean isNeedDlg() {
        return false;
    }

    @Override
    public void onSuccess(MyCreditAndPercents response) {

    }

    @Override
    public void onFailed(String code, String msg, MyCreditAndPercents localData) {

    }
}
