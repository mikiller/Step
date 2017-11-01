package com.westepper.step.logics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.adapters.DisDetailRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.responses.Commit;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class CommitLogic extends BaseLogic<Commit> {
    View inputView;
    DisDetailRcvAdapter rcvAdapter;

    public CommitLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<Commit>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "map/commit";
    }

    @Override
    public void sendRequest() {
        super.sendRequest(OkHttpManager.RequestType.JSONPOST);
    }

    @Override
    public boolean isNeedDlg() {
        return true;
    }

    public CommitLogic setCallbackObject(View view, DisDetailRcvAdapter adapter){
        inputView = view;
        rcvAdapter = adapter;
        return this;
    }

    @Override
    public void onSuccess(Commit response) {
        ((SuperActivity)context).hideInputMethod(inputView);
        rcvAdapter.addCommit(response);
    }

    @Override
    public void onFailed(String code, String msg, Commit localData) {
        Toast.makeText(context, "评论失败，请稍后再试", Toast.LENGTH_SHORT).show();
    }
}
