package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.DiscoveryList;

/**
 * Created by Mikiller on 2017/11/3.
 */

public class GetMyDiscoveryLogic extends BaseLogic<DiscoveryList> {
    public GetMyDiscoveryLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<DiscoveryList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/getMyDiscovery";
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
    public void onSuccess(DiscoveryList response) {

    }

    @Override
    public void onFailed(String code, String msg, DiscoveryList localData) {

    }
}
