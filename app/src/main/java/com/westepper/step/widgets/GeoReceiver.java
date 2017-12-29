package com.westepper.step.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.amap.api.fence.GeoFence;
import com.google.gson.Gson;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.GeoFanceLogic;
import com.westepper.step.models.GeoModel;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.DisArea;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MapUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mikiller on 2017/10/19.
 */

public class GeoReceiver extends BroadcastReceiver {

    private GeoHandler handler = new GeoHandler();
    private Context context;
    private MapUtils mapUtils = MapUtils.getInstance();
    public GeoReceiver() {
        MapUtils.getInstance().setLocalReachedList(new Gson().fromJson(MXPreferenceUtils.getInstance().getString(Constants.REACHED_ID + SuperActivity.userInfo.getUserId()), List.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        if(!isTrack)
//            return;
        this.context = context;
        Bundle bundle = intent.getExtras();
        int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
        String id = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
//        if (status == 1 && !MapUtils.getInstance().localReachedIds.contains(id)) {
        //id = "954";
        if (status == 1 && !MapUtils.getInstance().reachedList.contains(id)){
            mapUtils.reachedList.addReachedId(id);
            Area area= mapUtils.getArea(id);
            if(area==null) {
                mapUtils.setAreaChecked(id, true);
                mapUtils.localReachedIds.add(id);
                mapUtils.saveLocalReachedList();
            }else {
                boolean showL2 = true,showL3 = true;
                String pid1 = area.getP_id(),pid2;
                DisArea disArea = mapUtils.getDistrictArea(pid1), disArea1;
                for(String l1 : disArea.getAreaIds()){
                    if (!mapUtils.reachedList.contains(l1)){
                        showL2 = false;
                        break;
                    }
                }
                if (showL2){
                    pid2 = disArea.getP_id();
                    disArea1 = mapUtils.getDistrictArea(pid2);
                    for (String l2 : disArea1.getAreaIds()){
                        if (!mapUtils.reachedList.contains(l2)){
                            showL3 = false;
                            break;
                        }
                    }
                }else {

                }
            }
//            intent = new Intent(context.getString(R.string.geo_fence_receiver));
//            intent.putExtra(Constants.REACHED_LIST, MapUtils.getInstance().reachedList);
//            context.sendBroadcast(intent);
            //startGeoFenceLogic(context, id);
        }
        startGeoTask();
    }



    private void startGeoTask(){

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startGeoFenceLogic(mapUtils.localReachedIds);
            }
        },2000,2000);
    }

    private void startGeoFenceLogic(List<String> ids){
        if (ids == null || ids.size() == 0)
            return;
        String areaIds = ids.get(0);
        ids.remove(0);
        for (String id : ids)
           areaIds = areaIds.concat(",").concat(id);
        Message msg = new Message();
        msg.obj = areaIds;
        handler.sendMessage(msg);

    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            GeoFanceLogic logic = new GeoFanceLogic(context, new GeoModel((String) msg.obj));
            //logic.sendRequest();
        }
    }
}
