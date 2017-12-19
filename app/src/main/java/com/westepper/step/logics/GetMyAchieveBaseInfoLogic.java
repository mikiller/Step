package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.AchievementBaseInfo;

/**
 * Created by Mikiller on 2017/12/19.
 */

public class GetMyAchieveBaseInfoLogic extends BaseLogic<AchievementBaseInfo> {
    public GetMyAchieveBaseInfoLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<AchievementBaseInfo>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/achievementBaseInfo";
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
    public void onSuccess(AchievementBaseInfo response) {

    }

    @Override
    public void onFailed(String code, String msg, AchievementBaseInfo localData) {

    }
}
