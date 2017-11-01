package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.UserInfo;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class GetUserInfoLogic extends BaseLogic<UserInfo> {
    public GetUserInfoLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<UserInfo>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/getUserInfo";
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
    public void onSuccess(UserInfo response) {

    }

    @Override
    public void onFailed(String code, String msg, UserInfo localData) {

    }
}
