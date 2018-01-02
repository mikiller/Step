package com.westepper.step.logics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.R;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.ReachedList;
import com.westepper.step.utils.MapUtils;

/**
 * Created by Mikiller on 2017/11/14.
 */

public class GeoFanceLogic extends BaseLogic<ReachedList> {
    public GeoFanceLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<ReachedList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/putexploredinfo";
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
    public void onSuccess(ReachedList response) {
        Log.e(TAG, "explord success");
        MapUtils.getInstance().saveLocalReachedList(null);
        Intent intent = new Intent(context.getString(R.string.geo_fence_receiver));
        intent.putExtra(Constants.REACHED_LIST, response);
        context.sendBroadcast(intent);


    }

    @Override
    public void onFailed(String code, String msg, ReachedList localData) {
        Log.e(TAG, msg);
    }
}
