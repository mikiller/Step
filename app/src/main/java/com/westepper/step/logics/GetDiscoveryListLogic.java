package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.DiscoveryList;

/**
 * Created by Mikiller on 2017/10/27.
 */

public class GetDiscoveryListLogic extends BaseLogic<DiscoveryList> {
    public GetDiscoveryListLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<DiscoveryList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/getdiscoverylist";
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
    public void onSuccess(DiscoveryList response) {

    }

    @Override
    public void onFailed(String code, String msg, DiscoveryList localData) {

    }
}
