package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.RankList;

/**
 * Created by Mikiller on 2017/11/9.
 */

public class RankListLogic extends BaseLogic<RankList> {
    public RankListLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<RankList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/getRankList";
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
    public void onSuccess(RankList response) {

    }

    @Override
    public void onFailed(String code, String msg, RankList localData) {

    }
}
