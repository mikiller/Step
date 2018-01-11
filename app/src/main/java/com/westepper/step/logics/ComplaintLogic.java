package com.westepper.step.logics;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.activities.ReportAdviceActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.SuperActivity;

/**
 * Created by Mikiller on 2018/1/11.
 */

public class ComplaintLogic extends BaseLogic {
    private int kind;
    public ComplaintLogic(Context context, BaseModel model, int kind) {
        super(context, model);
        this.kind = kind;
        setUrl();
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = kind == ReportAdviceActivity.REPORT ? "user/complaint" : "user/reportError";
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
    public void onSuccess(Object response) {
        Toast.makeText(context, kind == ReportAdviceActivity.REPORT ? "您的举报已提交" : "提交成功", Toast.LENGTH_SHORT).show();
        ((SuperActivity)context).back();
    }

    @Override
    public void onFailed(String code, String msg, Object localData) {

    }
}
