package com.westepper.step.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.amap.api.fence.GeoFence;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.adapters.DiscoveryAdapter;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.customViews.SearchView;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.AreaList;
import com.westepper.step.responses.Discovery;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.utils.FileUtils;
import com.westepper.step.utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MapFragment extends BaseFragment {
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.rl_head)
    RelativeLayout rl_head;
    @BindView(R.id.rdg_kind)
    RadioGroup rdg_kind;
    @BindView(R.id.vp_discoveryList)
    ViewPager vp_discoveryList;

    DiscoveryAdapter adapter;

    MapUtils mapUtils;
    BroadcastReceiver geoReceiver;
    String path;
    private boolean isTrack = true;

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
    protected void setLayoutRes() {
        layoutRes = R.layout.fragment_map;
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

        adapter = new DiscoveryAdapter(null, getActivity());
        vp_discoveryList.setAdapter(adapter);
        vp_discoveryList.setOffscreenPageLimit(3);
        vp_discoveryList.setPageMargin(DisplayUtil.dip2px(getActivity(), 15));
    }

    public void setIsTrack(boolean isTrack){
        if(this.isTrack == isTrack)
            return;
        else
            this.isTrack = isTrack;
        int searchHeight, headHeight, vpHeight, vpPos;
        if(ll_search == null || rl_head  == null || vp_discoveryList == null)
            return;
        searchHeight = ll_search.getMeasuredHeight();
        headHeight = rl_head.getMeasuredHeight();
        vpPos = mapView.getMeasuredHeight() - vp_discoveryList.getMeasuredHeight();
        Log.e(TAG, "window h: " + mapView.getMeasuredHeight() + ", vp: " + vp_discoveryList.getMeasuredHeight());
        if(isTrack){
            AnimUtils.startObjectAnim(ll_search, "translationY", -searchHeight, 0, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", 0, -headHeight, 300);
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", 0, vpPos, 300);
        }else{
            AnimUtils.startObjectAnim(ll_search, "translationY", 0, -searchHeight, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", -headHeight, 0, 300);
            getDiscoveryList();
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", vpPos, 0, 300);
        }

    }

    private void getDiscoveryList(){
        //test
        List<Discovery> discoveryList = new ArrayList<>();
        Discovery dis = new Discovery();
        dis.setNickName("小飞");
        dis.setGender(1);
        dis.setInfo("小飞送鸡排");
        discoveryList.add(dis);
        dis = new Discovery();
        dis.setNickName("鸡排侠");
        dis.setGender(2);
        dis.setInfo("鸡排不用飞哥送");
        discoveryList.add(dis);
        adapter.setDataList(discoveryList);
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
        setIsTrack(type == R.id.rdb_track);
        //mapUtils.
    }
}
