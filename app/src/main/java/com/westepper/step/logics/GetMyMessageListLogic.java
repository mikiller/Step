package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.MyMsgList;

/**
 * Created by Mikiller on 2018/1/3.
 */

public class GetMyMessageListLogic extends BaseLogic<MyMsgList> {
    public GetMyMessageListLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<MyMsgList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/getMessage";
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
    public void onSuccess(MyMsgList response) {

    }

    @Override
    public void onFailed(String code, String msg, MyMsgList localData) {

    }
}
