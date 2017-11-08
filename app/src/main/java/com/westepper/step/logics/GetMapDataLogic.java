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
        super.sendRequest(OkHttpManager.RequestType.JSONPOST);
    }

    @Override
    public boolean isNeedDlg() {
        return false;
    }

    @Override
    public void onSuccess(MapData response) {
        if(response.getAchievementList().size() < 4){
            String[] kinds = new String[]{"1,城市探索", "2,我爱上海", "3,地标名胜", "4,限时成就"};
//            int i = 0;
//            List<Achieve> tmp = response.getAchievementList();
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
        String data = new Gson().toJson(response);
//        Log.e(TAG, data);
        FileUtils.saveToLocal(data, FileUtils.getFilePath(context, Constants.MAP_DATA));
    }

    @Override
    public void onFailed(String code, String msg, MapData localData) {

    }
}
