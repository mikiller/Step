package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.MyAchievements;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class GetMyAchievementsLogic extends BaseLogic<MyAchievements> {
    public GetMyAchievementsLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<MyAchievements>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/achievementInfo";
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
    public void onSuccess(MyAchievements response) {

    }

    @Override
    public void onFailed(String code, String msg, MyAchievements localData) {

    }
}
