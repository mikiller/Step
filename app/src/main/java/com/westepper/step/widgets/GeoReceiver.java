package com.westepper.step.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.MyApplication;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.GeoFanceLogic;
import com.westepper.step.models.GeoModel;
import com.westepper.step.models.ReachedModel;
import com.westepper.step.responses.ReachedList;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/19.
 */

public class GeoReceiver extends BroadcastReceiver {

    public GeoReceiver() {
        MapUtils.getInstance().setReachedList(new Gson().fromJson(MXPreferenceUtils.getInstance().getString(Constants.REACHED_ID + SuperActivity.userInfo.getUserId()), ReachedList.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        if(!isTrack)
//            return;
        Bundle bundle = intent.getExtras();
        int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
        String id = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
        if (status == 1 && !MapUtils.getInstance().reachedList.hasReachedId(id)) {
            MapUtils.getInstance().setAreaChecked(id);
            MapUtils.getInstance().reachedList.addReachedId(id);
            MapUtils.getInstance().saveReachedList();
            intent = new Intent(context.getString(R.string.geo_fence_receiver));
            intent.putExtra(Constants.REACHED_LIST, MapUtils.getInstance().reachedList);
            context.sendBroadcast(intent);
            startGeoFenceLogic(context, id);
        }
    }

    private void startGeoFenceLogic(Context context, String id){
        GeoFanceLogic logic = new GeoFanceLogic(context, new GeoModel(id));
        logic.sendRequest();
    }
}
