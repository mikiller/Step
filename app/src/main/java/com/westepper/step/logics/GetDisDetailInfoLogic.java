package com.westepper.step.logics;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.adapters.DisDetailRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.models.DisBase;
import com.westepper.step.responses.DiscoveryDetailInfo;
import com.westepper.step.utils.MXPreferenceUtils;

/**
 * Created by Mikiller on 2017/11/16.
 */

public class GetDisDetailInfoLogic extends BaseLogic<DiscoveryDetailInfo> {
    DisDetailRcvAdapter rcvAdapter;
    public GetDisDetailInfoLogic(Context context, BaseModel model) {
        super(context, model);
    }

    public GetDisDetailInfoLogic setRcvAdapter(DisDetailRcvAdapter rcvAdapter) {
        this.rcvAdapter = rcvAdapter;
        return this;
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<DiscoveryDetailInfo>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/discoveryDetail";
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
    public void onSuccess(DiscoveryDetailInfo response) {
        String key = ((DisBase)model).getDiscoveryId() + model.getUserId();
        MXPreferenceUtils.getInstance().setBoolean(key, response.isGood());
        MXPreferenceUtils.getInstance().setInt(((DisBase)model).getDiscoveryId() + "goodNum", response.getGoodNum());
        if(rcvAdapter != null)
            rcvAdapter.notifyItemChanged(0);
    }

    @Override
    public void onFailed(String code, String msg, DiscoveryDetailInfo localData) {

    }
}
