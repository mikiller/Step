package com.westepper.step.logics;

import android.app.Activity;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.activities.RegisterActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.utils.ActivityManager;

/**
 * Created by Mikiller on 2017/10/18.
 */

public class LoginLogic extends BaseLogic<BaseModel> {
    public LoginLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<BaseModel>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/login";
    }

    @Override
    public void sendRequest() {
        super.sendRequest(OkHttpManager.RequestType.JSONPOST);
    }

    @Override
    public boolean isNeedDlg() {
        return false;
    }

    @Override
    public void onSuccess(BaseModel response) {

    }

    @Override
    public void onFailed(String code, String msg, BaseModel localData) {
    }
}
