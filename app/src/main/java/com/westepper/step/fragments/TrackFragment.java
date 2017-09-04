package com.westepper.step.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.fence.GeoFence;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.westepper.step.R;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.AreaList;
import com.westepper.step.utils.FileUtils;
import com.westepper.step.utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class TrackFragment extends BaseFragment {
    @BindView(R.id.map)
    TextureMapView mapView;

    MapUtils mapUtils;
    BroadcastReceiver geoReceiver;
    String path;

    @Override
    protected void setLayoutRes() {
        layoutRes = R.layout.fragment_track;
    }

    @Override
    protected void initView() {
        path = getActivity().getFilesDir() + File.separator + "area.data";
        mapView.onCreate(saveBundle);
        mapUtils = MapUtils.getInstance(getActivity(), mapView.getMap());
        mapUtils.initLocationStyle(20000);
        mapUtils.initLocationClient();
        geoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                String id = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                if (status == 1) {
                    mapUtils.setAreaChecked(id);
                }
            }
        };

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapUtils.registGeoListener(geoReceiver);

        AreaList areaList = FileUtils.getDataFromLocal(path, AreaList.class);
        if(areaList == null) {
            createTestData();
            areaList = FileUtils.getDataFromLocal(path, AreaList.class);
        }

        for(Area area : areaList.getAreaList()){
            if(area.isHasChecked())
                mapUtils.addCheckedArea(area);
            else
                mapUtils.addArea(area);
            mapUtils.createGeoFenceClient(area.getId(), area.getBorderCoords());
        }
    }

    private void createTestData() {
        AreaList areaList = new AreaList();
        Area area1 = new Area("1");
        List<LatLng> coords = new ArrayList<>();
        coords.add(new LatLng(31.230957, 121.462133));
        coords.add(new LatLng(31.230911, 121.464043));
        coords.add(new LatLng(31.227425, 121.465052));
        coords.add(new LatLng(31.226847, 121.463078));
        coords.add(new LatLng(31.229856, 121.462005));
        area1.setBorderCoords(coords);
        areaList.setArea(area1);

        Area area2 = new Area("2");
        List<LatLng> coords2 = new ArrayList<>();
        coords2.add(new LatLng(31.230122, 121.459419));
        coords2.add(new LatLng(31.230957, 121.462133));
        coords2.add(new LatLng(31.229856, 121.462005));
        coords2.add(new LatLng(31.228085, 121.462638));
        coords2.add(new LatLng(31.226681, 121.459885));
        area2.setBorderCoords(coords2);
        areaList.setArea(area2);

        Area area3 = new Area("3");
        List<LatLng> coords3 = new ArrayList<>();
        coords3.add(new LatLng(31.230911, 121.464043));
        coords3.add(new LatLng(31.230819, 121.465792));
        coords3.add(new LatLng(31.227489, 121.466908));
        coords3.add(new LatLng(31.227425, 121.465052));
        area3.setBorderCoords(coords3);
        areaList.setArea(area3);

        Area area4 = new Area("4");
        List<LatLng> coords4 = new ArrayList<>();
        coords4.add(new LatLng(31.226847, 121.463078));
        coords4.add(new LatLng(31.227425, 121.465052));
        coords4.add(new LatLng(31.227489, 121.466908));
        coords4.add(new LatLng(31.225617, 121.467541));
        coords4.add(new LatLng(31.225241, 121.463249));
        area4.setBorderCoords(coords4);
        areaList.setArea(area4);

        String areaStr = new Gson().toJson(areaList);
        FileUtils.saveToLocal(areaStr, path);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mapUtils.unregistGeoListener(geoReceiver);
    }

    @Override
    public void onDestroyView() {
        mapView.onDestroy();
        mapUtils.destory();
        super.onDestroyView();
    }

    @Override
    public void fragmentCallback(int type, Intent data) {

    }
}
