package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.models.DisModel;
import com.westepper.step.responses.GoodCount;
import com.westepper.step.utils.MXPreferenceUtils;

/**
 * Created by Mikiller on 2017/11/3.
 */

public class GoodLogic extends BaseLogic<GoodCount> {
    public GoodLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<GoodCount>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/likes";
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
    public void onSuccess(GoodCount response) {
        String key = ((DisModel)model).getDiscoveryId() + model.getUserId();
        MXPreferenceUtils.getInstance().setBoolean(key, true);
        MXPreferenceUtils.getInstance().setInt(((DisModel)model).getDiscoveryId() + "goodNum", response.getCount());

    }

    @Override
    public void onFailed(String code, String msg, GoodCount localData) {

    }
}
