package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.JoinResponse;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class JoinLogic extends BaseLogic<JoinResponse> {


    public JoinLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<JoinResponse>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/joinoutgoing";
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
    public void onSuccess(JoinResponse response) {

    }

    @Override
    public void onFailed(String code, String msg, JoinResponse localData) {

    }
}
