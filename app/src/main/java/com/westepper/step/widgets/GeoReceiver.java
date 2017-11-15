package com.westepper.step.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.westepper.step.logics.GeoFanceLogic;
import com.westepper.step.models.GeoModel;
import com.westepper.step.models.ReachedModel;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MapUtils;

/**
 * Created by Mikiller on 2017/10/19.
 */

public class GeoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if(!isTrack)
//            return;
        Bundle bundle = intent.getExtras();
        int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
        String id = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
        if (status == 1) {
            MapUtils.getInstance().setAreaChecked(id);
            startGeoFenceLogic(context, id);
        }
    }

    private void startGeoFenceLogic(Context context, String id){
        GeoFanceLogic logic = new GeoFanceLogic(context, new GeoModel(id));
        logic.sendRequest();
    }
}
