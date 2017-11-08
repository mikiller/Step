package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.DiscoveryBaseInfo;

/**
 * Created by Mikiller on 2017/11/8.
 */

public class GetMyDiscoverBaseInfoLogic extends BaseLogic<DiscoveryBaseInfo> {
    public GetMyDiscoverBaseInfoLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<DiscoveryBaseInfo>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/discoverBaseInfo";
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
    public void onSuccess(DiscoveryBaseInfo response) {

    }

    @Override
    public void onFailed(String code, String msg, DiscoveryBaseInfo localData) {

    }
}
