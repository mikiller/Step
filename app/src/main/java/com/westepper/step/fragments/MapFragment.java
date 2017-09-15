package com.westepper.step.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.amap.api.fence.GeoFence;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.uilib.customdialog.CustomDialog;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.activities.NewDiscoveryActivity;
import com.westepper.step.adapters.DiscoveryAdapter;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.Constants;
import com.westepper.step.customViews.SearchView;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.AreaList;
import com.westepper.step.responses.Discovery;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.utils.FileUtils;
import com.westepper.step.utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MapFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.btn_acheivement)
    ImageButton btn_acheivement;
    @BindView(R.id.rl_head)
    RelativeLayout rl_head;
    @BindView(R.id.rdg_scope)
    RadioGroup rdg_scope;
    @BindView(R.id.btn_new)
    ImageButton btn_new;
    @BindView(R.id.ll_discovery_opt)
    LinearLayout ll_discovery_opt;
    @BindView(R.id.btn_selection)
    ImageButton btn_selection;
    @BindView(R.id.btn_refresh)
    ImageButton btn_refresh;
    @BindView(R.id.rdg_kind)
    RadioGroup rdg_kind;
    @BindView(R.id.vp_discoveryList)
    ViewPager vp_discoveryList;

    DiscoveryAdapter adapter;

    MapUtils mapUtils;
    BroadcastReceiver geoReceiver;
    String path;
    private boolean isTrack = true;
    float searchHeight, headTransY, vpTransY, optTransY;

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
        initMapUtil();

        geoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(!isTrack)
                    return;
                Bundle bundle = intent.getExtras();
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                String id = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                if (status == 1) {
                    mapUtils.setAreaChecked(id);
                }
            }
        };

        btn_acheivement.setOnClickListener(this);
        rdg_scope.setOnCheckedChangeListener(this);
        rdg_kind.setOnCheckedChangeListener(this);
        btn_new.setOnClickListener(this);
        btn_selection.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);

        adapter = new DiscoveryAdapter(null, getActivity());
        vp_discoveryList.setAdapter(adapter);
        vp_discoveryList.setOffscreenPageLimit(3);
        vp_discoveryList.setPageMargin(DisplayUtil.dip2px(getActivity(), 15));
        vp_discoveryList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Discovery disc = adapter.getItem(position);
                mapUtils.addMarker(disc.getLatlng());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getDiscoveryList(1, 1);
    }

    private void initMapUtil(){
        mapUtils = MapUtils.getInstance(getActivity(), mapView.getMap());
        mapUtils.initLocationStyle(20000);
        mapUtils.initLocationClient();

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
            Log.e(TAG, "add area");
            mapUtils.createGeoFenceClient(area.getId(), area.getBorderCoords());
        }
    }

    public void setIsTrack(boolean isTrack){
        if(this.isTrack == isTrack)
            return;
        else
            this.isTrack = isTrack;

        if(ll_search == null || rl_head  == null || vp_discoveryList == null) {
            return;
        }

        if(searchHeight <= 0) {
            searchHeight = ll_search.getMeasuredHeight();
            headTransY = rl_head.getTranslationY();
            vpTransY = vp_discoveryList.getTranslationY();
            optTransY = (int) ll_discovery_opt.getTranslationY();
            Log.e(TAG, "ty: " + vpTransY);
        }
        if(isTrack){
            AnimUtils.startObjectAnim(ll_search, "translationY", -searchHeight, 0, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", 0, headTransY, 300);
            AnimUtils.startObjectAnim(ll_discovery_opt, "translationY", 0, optTransY, 400);
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", 0, vpTransY, 800);
            mapUtils.removeMarker();
            mapUtils.setIsNeedArea(true);
        }else{
            AnimUtils.startObjectAnim(ll_search, "translationY", 0, -searchHeight, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", headTransY, 0, 300);
            AnimUtils.startObjectAnim(ll_discovery_opt, "translationY", optTransY, 0, 400);
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", vpTransY, 0, 500);
            mapUtils.addMarker(adapter.getItem(vp_discoveryList.getCurrentItem()).getLatlng());
            mapUtils.setIsNeedArea(false);
        }

    }

    private void getDiscoveryList(final int scope, final int kind){
        if(!isTrack) {
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", 0, vpTransY, 800);
            mapUtils.removeMarker();
        }
        //test
        vp_discoveryList.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Discovery> discoveryList = new ArrayList<>();
                Discovery dis = new Discovery();
                dis.setNickName("小飞");
                dis.setGender(1);
                dis.setInfo("小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排");
                dis.setLatlng(new LatLng(31.2304, 121.462489));
                dis.setDiscoveryKind(kind);
                discoveryList.add(dis);
                dis = new Discovery();
                dis.setNickName("鸡排侠");
                dis.setGender(2);
                dis.setInfo("鸡排不用飞哥送");
                dis.setLatlng(new LatLng(31.229189, 121.468207));
                dis.setDiscoveryKind(kind);
                discoveryList.add(dis);
                adapter.setScope(scope);
                adapter.setDataList(discoveryList);
                vp_discoveryList.setCurrentItem(0);
                if(!isTrack) {
                    AnimUtils.startObjectAnim(vp_discoveryList, "translationY", vpTransY, 0, 500);
                    mapUtils.addMarker(discoveryList.get(vp_discoveryList.getCurrentItem()).getLatlng());
                }
            }
        }, 1000);

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
    }

    @Override
    public void onPause() {
        mapView.onPause();
        mapUtils.unregistGeoListener(geoReceiver);
        super.onPause();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_acheivement:
                break;
            case R.id.btn_new:
                showNewDisDlg();
                break;
            case R.id.btn_selection:
                showGenderDlg();
                break;
            case R.id.btn_refresh:
                refreshDiscoveryList();
                break;
        }
    }

    private void showNewDisDlg(){
        final CustomDialog dlg = new CustomDialog(getActivity());
        dlg.setLayoutRes(R.layout.layout_newdis_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
            @Override
            public void onBtnClick(int id) {
                Map<String, Object> args = new HashMap<>();
                args.put(Constants.DIS_KIND, id == R.id.btn_mood ? Constants.MOOD : Constants.OUTGO);
                args.put(Constants.ISMULTIPLE, true);
//                ActivityManager.startActivity(getActivity(), NewDiscoveryActivity.class, args);
                ActivityManager.startActivity(getActivity(), GalleryActivity.class, args);
                dlg.dismiss();
            }
        }, R.id.btn_mood, R.id.btn_outgo).show();
    }

    private void showGenderDlg(){
        final CustomDialog dlg = new CustomDialog(getActivity());
        dlg.setLayoutRes(R.layout.layout_gender_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
            @Override
            public void onBtnClick(int id) {
                dlg.dismiss();
            }
        }, R.id.rdb_man, R.id.rdb_woman, R.id.rdb_people).show();
    }

    private void refreshDiscoveryList(){
        AnimUtils.startRotateAnim(btn_refresh, 360, 0, 1000);
        getDiscoveryList(getDisScope(), getDisKind());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rdb_friend:
                btn_selection.setVisibility(View.GONE);
                getDiscoveryList(Constants.FRIEND, getDisKind());
                break;
            case R.id.rdb_near:
                btn_selection.setVisibility(View.VISIBLE);
                getDiscoveryList(Constants.NEARBY, getDisKind());
                break;
            case R.id.rdb_mood:
                getDiscoveryList(getDisScope(), Constants.MOOD);
                break;
            case R.id.rdb_join:
                getDiscoveryList(getDisScope(), Constants.OUTGO);
                break;
        }
    }

    private int getDisKind(){
        return rdg_kind.getCheckedRadioButtonId() == R.id.rdb_mood ? Constants.MOOD : Constants.OUTGO;
    }

    private int getDisScope(){
        return rdg_scope.getCheckedRadioButtonId() == R.id.rdb_friend ? Constants.FRIEND : Constants.NEARBY;
    }
}
