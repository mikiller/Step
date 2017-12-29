package com.westepper.step.logics;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.MapData;
import com.westepper.step.utils.FileUtils;

import java.util.List;

/**
 * Created by Mikiller on 2017/10/24.
 */

public class GetMapDataLogic extends BaseLogic<MapData> {
    public GetMapDataLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<MapData>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "data/getlatestdata";
    }

    @Override
    public void sendRequest() {
        super.sendAnsyRequest(OkHttpManager.RequestType.JSONPOST);
    }

    @Override
    public boolean isNeedDlg() {
        return true;
    }

    @Override
    public void onSuccess(MapData response) {
        if(response.getAchievementList().size() < 4){
            String[] kinds = new String[]{"2,我爱上海", "3,探索世界", "4,地标名胜", "5,限时成就"};
            for(int i = 0; i < kinds.length; i++) {
                String[] achName = kinds[i].split(",");
                if(i < response.getAchievementList().size()){
                    if (response.getAchievementList().get(i).getAchieveCategory_id().equals(achName[0])) {
                        continue;
                    }else{
                        response.getAchievementList().add(i, new Achieve(achName[0], achName[1]));
                    }
                }else{
                    response.getAchievementList().add(new Achieve(achName[0], achName[1]));
                }
            }
        }

    }

    @Override
    public void onFailed(String code, String msg, MapData localData) {

    }
}
