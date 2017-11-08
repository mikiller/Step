package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.MyAchievements;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class GetMyAchievementsLogic extends BaseLogic<MyAchievements> {
    int type = Constants.ACH_CITY;
    public GetMyAchievementsLogic(Context context, BaseModel model) {
        super(context, model);
    }

    public void setType(int type) {
        this.type = type;
        setUrl();
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<MyAchievements>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = type == Constants.ACH_BADGE ? "user/achievementInfo" : "user/discoverInfo";
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
        response.setType(type);
    }

    @Override
    public void onFailed(String code, String msg, MyAchievements localData) {

    }
}
