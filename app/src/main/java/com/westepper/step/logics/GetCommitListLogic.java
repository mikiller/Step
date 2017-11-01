package com.westepper.step.logics;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.adapters.DisDetailRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.CommitList;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class GetCommitListLogic extends BaseLogic<CommitList> {
    DisDetailRcvAdapter rcvAdapter;

    public GetCommitListLogic(Context context, BaseModel model) {
        super(context, model);
    }

    public GetCommitListLogic setAdapter(DisDetailRcvAdapter adapter){
        rcvAdapter = adapter;
        return this;
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<CommitList>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/getdiscoverycommit";
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
        rcvAdapter.setCommits(response.getCommitList());
    }

    @Override
    public void onFailed(String code, String msg, CommitList localData) {
        if("0".equals(code)){
            Toast.makeText(context, msg.concat(", 赶紧抢个沙发吧！"), Toast.LENGTH_SHORT).show();
        }
    }
}
