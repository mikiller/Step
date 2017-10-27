package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.SuperActivity;

/**
 * Created by Mikiller on 2017/10/27.
 */

public class NewDiscoveryLogic extends BaseLogic {
    public NewDiscoveryLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/newDiscovery";
    }

    @Override
    public void sendRequest() {
        super.sendRequest(OkHttpManager.RequestType.JSONPOST);
    }

    @Override
    public boolean isNeedDlg() {
        return true;
    }

    @Override
    public void onSuccess(Object response) {
        ((SuperActivity)context).back();
    }

    @Override
    public void onFailed(String code, String msg, Object localData) {

    }
}
