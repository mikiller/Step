package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.JoinUsers;
import com.westepper.step.responses.UserInfo;

/**
 * Created by Mikiller on 2017/11/27.
 */

public class GetJoinUsersLogic extends BaseLogic<JoinUsers> {
    public GetJoinUsersLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<JoinUsers>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/getJoinUser";
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
    public void onSuccess(JoinUsers response) {

    }

    @Override
    public void onFailed(String code, String msg, JoinUsers localData) {

    }
}
