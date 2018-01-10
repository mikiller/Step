package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.models.JoinModel;

/**
 * Created by Mikiller on 2018/1/10.
 */

public class BindGroupChatLogic extends BaseLogic<JoinModel> {
    public BindGroupChatLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<JoinModel>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/groupChat";
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
    public void onSuccess(JoinModel response) {

    }

    @Override
    public void onFailed(String code, String msg, JoinModel localData) {

    }
}
