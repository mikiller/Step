package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.MyCreditAndPercents;

/**
 * Created by Mikiller on 2017/11/7.
 */

public class GetMyAchievementsLogic extends BaseLogic<MyCreditAndPercents> {
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
        responseType = new TypeToken<BaseResponse<MyCreditAndPercents>>(){}.getType();
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
    public void onSuccess(MyCreditAndPercents response) {
        response.setType(type);
    }

    @Override
    public void onFailed(String code, String msg, MyCreditAndPercents localData) {

    }
}
