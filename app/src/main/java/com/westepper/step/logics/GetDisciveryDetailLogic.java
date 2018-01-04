package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.Discovery;

/**
 * Created by Mikiller on 2017/11/16.
 */

public class GetDisciveryDetailLogic extends BaseLogic<Discovery> {
//    DisDetailRcvAdapter rcvAdapter;
    public GetDisciveryDetailLogic(Context context, BaseModel model) {
        super(context, model);
    }

//    public GetDisciveryDetailLogic setRcvAdapter(DisDetailRcvAdapter rcvAdapter) {
//        this.rcvAdapter = rcvAdapter;
//        return this;
//    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<Discovery>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/discoveryDetail";
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
    public void onSuccess(Discovery response) {
    }

    @Override
    public void onFailed(String code, String msg, Discovery localData) {

    }
}
