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
import com.westepper.step.adapters.DiscoveryAdapter;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.Constants;
import com.westepper.step.customViews.AcheiveSettingLayout;
import com.westepper.step.customViews.SearchView;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.City;
import com.westepper.step.responses.MapData;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.Graphics;
import com.westepper.step.responses.UserPos;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.utils.FileUtils;
import com.westepper.step.utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MapFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.btn_acheivement)
    ImageButton btn_acheivement;
    @BindView(R.id.layout_achSetting)
    AcheiveSettingLayout layout_achSetting;
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
    MapData mapData;
    MapUtils mapUtils;
    //    BroadcastReceiver geoReceiver;
    String path;
    private boolean isTrack = true;
    float searchHeight, headTransY, vpTransY, optTransY;
    int gender = 0;

    private void createTestData() {
        MapData mapData = new MapData();
        City city1 = new City();
        city1.setCityName("上海");
        Area area1 = new Area("1");
        area1.setAreaType(Area.POLYGON);
        List<LatLng> coords = new ArrayList<>();
        coords.add(new LatLng(31.230957, 121.462133));
        coords.add(new LatLng(31.230911, 121.464043));
        coords.add(new LatLng(31.227425, 121.465052));
        coords.add(new LatLng(31.226847, 121.463078));
        coords.add(new LatLng(31.229856, 121.462005));
        area1.setBorderList(coords);
        city1.setArea(area1);

        Area area2 = new Area("2");
        area2.setAreaType(Area.POLYGON);
        List<LatLng> coords2 = new ArrayList<>();
        coords2.add(new LatLng(31.230122, 121.459419));
        coords2.add(new LatLng(31.230957, 121.462133));
        coords2.add(new LatLng(31.229856, 121.462005));
        coords2.add(new LatLng(31.228085, 121.462638));
        coords2.add(new LatLng(31.226681, 121.459885));
        area2.setBorderList(coords2);
        city1.setArea(area2);

        Area area3 = new Area("3");
        area3.setAreaType(Area.POLYGON);
        List<LatLng> coords3 = new ArrayList<>();
        coords3.add(new LatLng(31.230911, 121.464043));
        coords3.add(new LatLng(31.230819, 121.465792));
        coords3.add(new LatLng(31.227489, 121.466908));
        coords3.add(new LatLng(31.227425, 121.465052));
        area3.setBorderList(coords3);
        city1.setArea(area3);

        Area area4 = new Area("4");
        area4.setAreaType(Area.POLYGON);
        List<LatLng> coords4 = new ArrayList<>();
        coords4.add(new LatLng(31.226847, 121.463078));
        coords4.add(new LatLng(31.227425, 121.465052));
        coords4.add(new LatLng(31.227489, 121.466908));
        coords4.add(new LatLng(31.225617, 121.467541));
        coords4.add(new LatLng(31.225241, 121.463249));
        area4.setBorderList(coords4);
        city1.setArea(area4);

        Area area5 = new Area("5");
        area5.setAreaType(Area.CIRCLE);
        Area.CirlclArea ca = new Area.CirlclArea(new LatLng(31.230779, 121.472071), 800);
        area5.setCircle(ca);
        city1.setArea(area5);

//        Area area6 = new Area("6");
//        area6.setAreaType(Area.CIRCLE);
//        Area.CirlclArea ca1 = new Area.CirlclArea(new LatLng(31.230779, 121.472071), 800);
//        area6.setCircle(ca1);
//        city1.setArea(area6);

        mapData.setCity(city1);

        Achieve achieve = new Achieve();
        achieve.setAchieveKind("城市探索");
        AchieveArea achieveArea = new AchieveArea();
        achieveArea.setAchieveName("静安区");
        achieveArea.setAchieveAreaId("13");
        achieveArea.setAreaIds("3,4");
        achieve.setAchieveArea(achieveArea);
        AchieveArea achieveArea1 = new AchieveArea();
        achieveArea1.setAchieveName("黄浦区");
        achieveArea1.setAchieveAreaId("14");
        achieveArea1.setAreaIds("1,2,5");
        achieve.setAchieveArea(achieveArea1);
        mapData.setAchieve(achieve);

        Achieve achieve1 = new Achieve();
        achieve1.setAchieveKind("发现世界");
        AchieveArea achieveArea2 = new AchieveArea();
        achieveArea2.setAchieveName("人民广场");
        achieveArea2.setAchieveAreaId("16");
        achieveArea2.setAreaIds("5");
        achieve1.setAchieveArea(achieveArea2);
        AchieveArea achieveArea3 = new AchieveArea();
        achieveArea3.setAchieveName("上海电视台");
        achieveArea3.setAchieveAreaId("17");
        achieveArea3.setAreaIds("1,2");
        achieve1.setAchieveArea(achieveArea3);
        mapData.setAchieve(achieve1);

        Achieve achieve2 = new Achieve();
        achieve2.setAchieveKind("行者无疆");
        AchieveArea achieveArea4 = new AchieveArea();
        achieveArea4.setAchieveName("南京西路");
        achieveArea4.setAchieveAreaId("20");
        achieveArea4.setAreaIds("1,4");
        achieve2.setAchieveArea(achieveArea4);
        AchieveArea achieveArea5 = new AchieveArea();
        achieveArea5.setAchieveName("威海路");
        achieveArea5.setAchieveAreaId("21");
        achieveArea5.setAreaIds("2,3");
        achieve2.setAchieveArea(achieveArea5);
        mapData.setAchieve(achieve2);

        String areaStr = new Gson().toJson(mapData);
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

//        geoReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(!isTrack)
//                    return;
//                Bundle bundle = intent.getExtras();
//                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
//                String id = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
//                Log.e(TAG, "id: " + id + ", status: " + status);
//                if (status == 1) {
//                    mapUtils.setAreaChecked(id);
//                }
//            }
//        };

        mapData = FileUtils.getDataFromLocal(path, MapData.class);
        if (mapData == null) {
            createTestData();
            mapData = FileUtils.getDataFromLocal(path, MapData.class);
        }
        initMapUtil();
        initAcheiveSetting();


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
                mapUtils.addMarker(disc.getUserPos().getLatlng());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getDiscoveryList(1, 1, gender);
    }

    private void initMapUtil() {
        mapUtils = MapUtils.getInstance();
        mapUtils.init(getActivity().getApplicationContext(), mapView.getMap());
        for (City city : mapData.getCityList()) {
            if (!city.getCityName().equals("上海"))
                continue;
            for (Area area : city.getAreaList()) {
                mapUtils.addArea(area, Graphics.MAP);
            }
        }
    }


    private void initAcheiveSetting() {
        layout_achSetting.setAchievementList(mapData.getAchievementList());
        layout_achSetting.setAchieveSettingListener(new AcheiveSettingLayout.onAchieveSettingListener() {
            @Override
            public void onAchieveSelected(String[] areaId, String achieveKind) {
                if (achieveKind.equals("探索地图")) {
                    mapUtils.setAreaType(Graphics.MAP);
                } else {
                    mapUtils.setAreaType(Graphics.ACHEIVE);
                }
                mapUtils.setShowAreaIds(areaId);
            }
        });
    }

    public void setIsTrack(boolean isTrack) {
        if (this.isTrack == isTrack)
            return;
        else
            this.isTrack = isTrack;

        if (ll_search == null || rl_head == null || vp_discoveryList == null) {
            return;
        }

        if (searchHeight <= 0) {
            searchHeight = ll_search.getMeasuredHeight();
            headTransY = rl_head.getTranslationY();
            vpTransY = vp_discoveryList.getTranslationY();
            optTransY = (int) ll_discovery_opt.getTranslationY();
        }
        if (isTrack) {
            AnimUtils.startObjectAnim(ll_search, "translationY", -searchHeight, 0, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", 0, headTransY, 300);
            AnimUtils.startObjectAnim(ll_discovery_opt, "translationY", 0, optTransY, 400);
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", 0, vpTransY, 800);
            mapUtils.removeMarker();
            mapUtils.setIsNeedArea(true);
        } else {
            AnimUtils.startObjectAnim(ll_search, "translationY", 0, -searchHeight, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", headTransY, 0, 300);
            AnimUtils.startObjectAnim(ll_discovery_opt, "translationY", optTransY, 0, 400);
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", vpTransY, 0, 500);
            mapUtils.addMarker(adapter.getItem(vp_discoveryList.getCurrentItem()).getUserPos().getLatlng());
            mapUtils.setIsNeedArea(false);
        }

    }

    private void getDiscoveryList(final int scope, final int kind, final int gender) {
        if (!isTrack) {
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", 0, vpTransY, 800);
            mapUtils.removeMarker();
        }
        //test create discovery list
        vp_discoveryList.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Discovery> discoveryList = new ArrayList<>();
                Discovery dis = new Discovery();
                dis.setNickName("小飞");
                dis.setGender(1);
                dis.setInfo("小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排小飞送鸡排");
                UserPos userPos = new UserPos(new LatLng(31.2304, 121.462489), "上海市，上海电视台");
                dis.setUserPos(userPos);
                dis.setDiscoveryKind(kind);
                if (kind == Constants.OUTGO) {
                    dis.setJoinCount(3);
                    dis.setTotalCount(5);
                }
                if (gender != 2)
                    discoveryList.add(dis);
                dis = new Discovery();
                dis.setNickName("鸡排侠");
                dis.setGender(2);
                dis.setInfo("鸡排不用飞哥送");
                UserPos userPos1 = new UserPos(new LatLng(31.229189, 121.468207), "上海市，和平影院");
                dis.setUserPos(userPos1);
                dis.setDiscoveryKind(kind);
                if (kind == Constants.OUTGO) {
                    dis.setJoinCount(2);
                    dis.setTotalCount(6);
                }
                if (gender != 1)
                    discoveryList.add(dis);
                adapter.setScope(scope);
                adapter.setDataList(discoveryList);
                vp_discoveryList.setCurrentItem(0);
                if (!isTrack) {
                    AnimUtils.startObjectAnim(vp_discoveryList, "translationY", vpTransY, 0, 500);
                    mapUtils.addMarker(discoveryList.get(vp_discoveryList.getCurrentItem()).getUserPos().getLatlng());
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

    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mapView.onDestroy();
//        mapUtils.destory();
        mapUtils.setShowAreaIds(null);
        super.onDestroyView();
    }

    @Override
    public void fragmentCallback(int type, Intent data) {
        setIsTrack(type == R.id.rdb_track);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_acheivement:
//                layout_achSetting.setVisibility(View.VISIBLE);
                layout_achSetting.show();
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

    private void showNewDisDlg() {
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

    private void showGenderDlg() {
        final CustomDialog dlg = new CustomDialog(getActivity());
        dlg.setLayoutRes(R.layout.layout_gender_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
            @Override
            public void onBtnClick(int id) {
                dlg.dismiss();
            }
        }, R.id.rdb_man, R.id.rdb_woman, R.id.rdb_people);
        final int[] selections = new int[]{R.id.rdb_people, R.id.rdb_man, R.id.rdb_woman};
        RadioGroup rdg = (RadioGroup) dlg.getCustomView().findViewById(R.id.rdg_gender);
        rdg.check(selections[gender]);
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < selections.length; i++) {
                    if (checkedId == selections[i]) {
                        gender = i;
                        break;
                    }

                }
                getDiscoveryList(getDisScope(), getDisKind(), gender);
            }
        });
        dlg.show();
    }

    private void refreshDiscoveryList() {
        AnimUtils.startRotateAnim(btn_refresh, 360, 0, 1000);
        getDiscoveryList(getDisScope(), getDisKind(), gender);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rdb_friend:
                btn_selection.setVisibility(View.GONE);
                getDiscoveryList(Constants.FRIEND, getDisKind(), gender);
                break;
            case R.id.rdb_near:
                btn_selection.setVisibility(View.VISIBLE);
                getDiscoveryList(Constants.NEARBY, getDisKind(), gender);
                break;
            case R.id.rdb_mood:
                getDiscoveryList(getDisScope(), Constants.MOOD, gender);
                break;
            case R.id.rdb_join:
                getDiscoveryList(getDisScope(), Constants.OUTGO, gender);
                break;
        }
    }

    private int getDisKind() {
        return rdg_kind.getCheckedRadioButtonId() == R.id.rdb_mood ? Constants.MOOD : Constants.OUTGO;
    }

    private int getDisScope() {
        return rdg_scope.getCheckedRadioButtonId() == R.id.rdb_friend ? Constants.FRIEND : Constants.NEARBY;
    }
}
