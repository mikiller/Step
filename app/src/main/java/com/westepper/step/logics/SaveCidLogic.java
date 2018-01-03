package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;

/**
 * Created by Mikiller on 2018/1/3.
 */

public class SaveCidLogic extends BaseLogic {
    public SaveCidLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/saveCid";
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
    public void onSuccess(Object response) {
    }

    @Override
    public void onFailed(String code, String msg, Object localData) {

    }
}
