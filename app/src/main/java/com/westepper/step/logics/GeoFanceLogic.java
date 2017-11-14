package com.westepper.step.logics;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.activities.MainActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.responses.AchieveArea;
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
        showCongraDlg("点亮L1区域", R.mipmap.ic_dis_l1);

        if(!TextUtils.isEmpty(response.getReachedL2Id())){
            showCongraDlg(response.getReachedL2Id(), R.mipmap.ic_dis_l2);
        }
        if(!TextUtils.isEmpty(response.getReachedL3Id())){
            showCongraDlg(response.getReachedL3Id(), R.mipmap.ic_dis_l3);
        }
        if(response.getReachedAchievementIds() != null && response.getReachedAchievementIds().size() > 0){
            for(String id : response.getReachedAchievementIds()) {
                AchieveArea ach = MainActivity.mapData.getAchieveName(id);
                if(ach != null){
                    int imgId = -1;
                    switch (ach.getCredit_level()){
                        case "1":
                            imgId = R.mipmap.ic_ach_l1;
                            break;
                        case "10":
                            imgId = R.mipmap.ic_dis_l2;
                            break;
                        case "100":
                            imgId = R.mipmap.ic_ach_l3;
                            break;
                        case "1000":
                            imgId = R.mipmap.ic_ach_l4;
                            break;
                    }
                    showCongraDlg("达成"+ach.getAchieveAreaName()+"成就", imgId);
                }
            }
        }

    }

    @Override
    public void onFailed(String code, String msg, ReachedList localData) {

    }

    private void showCongraDlg(String txt, int imgId){
        CustomDialog dlg = new CustomDialog(context).setLayoutRes(R.layout.layout_congratulation);
        dlg.setCancelable(true);

        TextView tv = (TextView) dlg.getCustomView().findViewById(R.id.tv_con_txt);
        tv.setText(txt);
        ImageView iv = (ImageView) dlg.getCustomView().findViewById(R.id.iv_con_icon);
        iv.setImageResource(imgId);
        dlg.show();
    }
}
