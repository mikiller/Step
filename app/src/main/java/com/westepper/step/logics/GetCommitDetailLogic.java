package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.CommitList;

/**
 * Created by Mikiller on 2018/1/5.
 */

public class GetCommitDetailLogic extends BaseLogic<CommitList> {
    public GetCommitDetailLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<CommitList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/getalldiscoverycommit";
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
    public void onSuccess(CommitList response) {

    }

    @Override
    public void onFailed(String code, String msg, CommitList localData) {

    }
}
