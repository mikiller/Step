package com.westepper.step.logics;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.DiscoveredCities;
import com.westepper.step.utils.MXPreferenceUtils;

/**
 * Created by Mikiller on 2017/11/13.
 */

public class DiscoverCityLogic extends BaseLogic<DiscoveredCities> {
    public DiscoverCityLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<DiscoveredCities>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/discoverCity";
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
    public void onSuccess(DiscoveredCities response) {
        MXPreferenceUtils.getInstance().setString(model.getUserId() + "_discities", new Gson().toJson(response));
//        final CustomDialog dlg = new CustomDialog(context);
//        dlg.setLayoutRes(R.layout.layout_congratulation).setCancelable(true);
//        View view = dlg.getCustomView();
//        ((TextView) view.findViewById(R.id.tv_con_txt)).setText("发现" + response.);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dlg.dismiss();
//            }
//        });
//        dlg.show();
    }

    @Override
    public void onFailed(String code, String msg, DiscoveredCities localData) {

    }
}
