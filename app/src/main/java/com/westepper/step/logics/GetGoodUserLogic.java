package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.GoodUserList;

/**
 * Created by Mikiller on 2018/1/4.
 */

public class GetGoodUserLogic extends BaseLogic<GoodUserList> {
    public GetGoodUserLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<GoodUserList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/getLikeList";
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
    public void onSuccess(GoodUserList response) {

    }

    @Override
    public void onFailed(String code, String msg, GoodUserList localData) {

    }
}
