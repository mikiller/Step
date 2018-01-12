package com.westepper.step.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;

import com.amap.api.fence.GeoFence;
import com.google.gson.Gson;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.GeoFanceLogic;
import com.westepper.step.models.GeoModel;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.DisArea;
import com.westepper.step.responses.ReachedId;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MapUtils;

import java.util.ArrayList;
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
    private static Timer timer;
    private static List<String> hasShowedIds;

    public GeoReceiver() {
        mapUtils.setLocalReachedList(new Gson().fromJson(MXPreferenceUtils.getInstance().getString(Constants.REACHED_ID + SuperActivity.userInfo.getUserId()), List.class));
        if (mapUtils.reachedList == null) {
            mapUtils.getReachedList();
        }
        if (hasShowedIds == null){
            String json = MXPreferenceUtils.getInstance().getString(Constants.SHOWED_ID);
            hasShowedIds = TextUtils.isEmpty(json) ? new ArrayList<>() : new Gson().fromJson(json, List.class);
        }
        startGeoTask();
    }

    private void startGeoTask() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startGeoFenceLogic(mapUtils.localReachedIds);
                }
            }, 30000, 30000);
        }
    }

    private void startGeoFenceLogic(List<String> ids) {
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

    //String[] tmp = new String[]{"1474","1476","1479","1483","1488","1489","1490","1491","1492","1493","1494","1495","1496","1497","1498","1499","1500","1501","1502","1503","1504","1505","1506","1507","1508","1509","1510","1511","1512","1513","1514","1515","1516","1517","1518","1519","1520","1521","1522","1523","1524","1525","1526","1527","1528","1530","1531","1532","1533","1534","1535","1536","1537","1538","1539","1540","1541","1542","1543","1544","1545","1546","1547","1548"};
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.e("geo receiver", "start a new receiver: " + this.toString() + ", context:" + context.toString() + "name: " + context.getClass().getName());
        Bundle bundle = intent.getExtras();
        int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
        String id = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
//        for(String id : tmp) {

        if ((status == 1) && isNeedShow(id)) {
            checkedArea(id, 1, null);
            Area area = mapUtils.getArea(id);
            if (area != null) {
                searchDisArea(area.getP_id(), 1);
            }
        }
//        }

        //test
//        mapUtils.saveLocalReachedList(null);
    }

    private boolean isNeedShow(String id){
        return !MapUtils.getInstance().reachedList.contains(id, 1) || !hasShowedIds.contains(id);
    }

    private void searchDisArea(String pid, int level) {
        DisArea disArea = mapUtils.getDistrictArea(pid);
        if (disArea != null) {
            List<ReachedId> reachedIds = new ArrayList<>();
            if (searchAreaId(disArea.getAreaIds() == null ? disArea.getDistrictIds() : disArea.getAreaIds(), reachedIds, level)) {
                checkedArea(pid, level + 1, disArea.getAreaIds());
                mapUtils.reachedList.updateReachedId(reachedIds, level);
                searchDisArea(disArea.getP_id(), level + 1);
            }
        }
    }

    private void sendReachedBoardcast(String id, int level) {
        Intent intent = new Intent(context.getString(R.string.geo_fence_receiver));
        intent.putExtra(Constants.REACHED_ID, id);
        intent.putExtra(Constants.REACHED_LEVEL, level);
        context.sendBroadcast(intent);
    }

    private void checkedArea(String id, int level, List<String> disIds) {
        hasShowedIds.add(id);
        MXPreferenceUtils.getInstance().setString(Constants.SHOWED_ID, new Gson().toJson(hasShowedIds));
        if (!mapUtils.reachedList.contains(id, level)) {
            mapUtils.reachedList.addReachedId(id, level);
            if (level == 1) {
                mapUtils.setAreaChecked(id, true);
                mapUtils.saveLocalReachedList(id);
            } else {
                mapUtils.setDisAreaChecked(id, true);
                mapUtils.showDisArea(id);
                for (String l1 : disIds) {
                    if (level == 2)
                        mapUtils.setAreaChecked(l1, false);
                    else
                        mapUtils.setDisAreaChecked(l1, false);
                }
            }
        }
        sendReachedBoardcast(id, level);
    }

    private boolean searchAreaId(List<String> rIds, List<ReachedId> reachedIds, int level) {
        boolean needShow = true;
        for (String id : rIds) {
            if (!mapUtils.reachedList.contains(id, level)) {
                needShow = false;
                break;
            }
            reachedIds.add(new ReachedId(id, 0));
        }
        return needShow;
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("geo receiver", (String) msg.obj);
            GeoFanceLogic logic = new GeoFanceLogic(context, new GeoModel((String) msg.obj));
            logic.sendRequest();
        }
    }
}
