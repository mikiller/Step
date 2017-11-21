package com.westepper.step.logics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.models.SignModel;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.MXPreferenceUtils;

/**
 * Created by Mikiller on 2017/11/2.
 */

public class UpdateUserInfoLogic extends BaseLogic<UserInfo> {
    public UpdateUserInfoLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<UserInfo>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/updateUserInfo";
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
    public void onSuccess(UserInfo response) {
        SuperActivity.userInfo = response;
        MXPreferenceUtils.getInstance().setString(response.getUserId(), new Gson().toJson(response));
    }

    @Override
    public void onFailed(String code, String msg, UserInfo localData) {
        Log.e(TAG, code.concat(", ").concat(msg));
    }
}
