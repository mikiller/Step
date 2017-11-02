package com.westepper.step.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.uilib.customdialog.CustomDialog;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.activities.MainActivity;
import com.westepper.step.adapters.DiscoveryAdapter;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.AcheiveSettingLayout;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.customViews.SearchView;
import com.westepper.step.logics.CommitLogic;
import com.westepper.step.logics.GetDiscoveryListLogic;
import com.westepper.step.models.CommitModel;
import com.westepper.step.models.DiscoveryListModel;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.City;
import com.westepper.step.responses.DiscoveryList;
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

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class
MapFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
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
    @BindView(R.id.commitInput)
    CommitEditView commitInput;

    DiscoveryAdapter adapter;
    MapUtils mapUtils;
    private boolean isTrack = true;
    float searchHeight, headTransY, vpTransY, optTransY;
    int scope = Constants.FRIEND, disKind = Constants.MOOD, gender = 0;

    @Override
    protected void setLayoutRes() {
        layoutRes = R.layout.fragment_map;
    }

    @Override
    protected void initView() {
        mapView.onCreate(saveBundle);


        initMapUtil();
        initAcheiveSetting();


        btn_acheivement.setOnClickListener(this);
        rdg_scope.setOnCheckedChangeListener(this);
        rdg_kind.setOnCheckedChangeListener(this);
        btn_new.setOnClickListener(this);
        btn_selection.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);

        adapter = new DiscoveryAdapter(null, getActivity());
        adapter.setCommitInput(commitInput);
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
        getDiscoveryList();
    }

    private void initMapUtil() {
        mapUtils = MapUtils.getInstance();
        mapUtils.init(getActivity().getApplicationContext(), mapView.getMap());
        for (City city : MainActivity.mapData.getCityList()) {
            if (!city.getCityName().equals("上海"))
                continue;
            for (Area area : city.getAreaList()) {
                mapUtils.addArea(area, Graphics.MAP);
            }
        }
        for(Area area : MainActivity.mapData.getAchievementAreaList()){
            mapUtils.addAchieveArea(area);
        }
    }


    private void initAcheiveSetting() {
        layout_achSetting.setAchievementList(MainActivity.mapData.getAchievementList());
        layout_achSetting.setAchieveSettingListener(new AcheiveSettingLayout.onAchieveSettingListener() {
            @Override
            public void onAchieveSelected(String[] areaId, String achieveKind) {
                if (achieveKind.equals("探索地图")) {
                    mapUtils.setAreaType(Graphics.MAP);
                } else {
                    mapUtils.setAreaType(Graphics.ACHEIVE, areaId);
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
            if(adapter.getCount() > 0)
                mapUtils.addMarker(adapter.getItem(vp_discoveryList.getCurrentItem()).getUserPos().getLatlng());
            mapUtils.setIsNeedArea(false);
        }

    }

    private void getDiscoveryList() {
        if (!isTrack) {
            if(vp_discoveryList.getTranslationY() == 0)
                AnimUtils.startObjectAnim(vp_discoveryList, "translationY", 0, vpTransY, 400);
            mapUtils.removeMarker();
        }
        GetDiscoveryListLogic logic = new GetDiscoveryListLogic(getActivity(), new DiscoveryListModel(scope == Constants.FRIEND ? 0 : gender, disKind, scope, mapUtils.getMapLocation().getLatitude(), mapUtils.getMapLocation().getLongitude()));
        logic.setCallback(new BaseLogic.LogicCallback<DiscoveryList>() {
            @Override
            public void onSuccess(DiscoveryList response) {
                adapter.setScope(scope);
                adapter.setDataList(response.getDiscoveryList());
                vp_discoveryList.setCurrentItem(0);
                if (!isTrack) {
                    AnimUtils.startObjectAnim(vp_discoveryList, "translationY", vpTransY, 0, 500);
                    if(response.getDiscoveryList().size() > 0)
                    mapUtils.addMarker(response.getDiscoveryList().get(0).getUserPos().getLatlng());
                }
            }

            @Override
            public void onFailed(String code, String msg, DiscoveryList localData) {
                if("0".equals(code)){
                    Toast.makeText(getActivity(), "未搜到您想要的结果，".concat(msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
        logic.sendRequest();
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
        if (type == Constants.SHOW_ACHIEVE_AREA) {
            layout_achSetting.checkAchItem("城市探索", data.getStringExtra(Constants.ACH_KIND));
        } else
            setIsTrack(type == R.id.rdb_track);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_acheivement:
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
                getDiscoveryList();
            }
        });
        dlg.show();
    }

    private void refreshDiscoveryList() {
        AnimUtils.startRotateAnim(btn_refresh, 360, 0, 1000);
        getDiscoveryList();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rdb_friend:
                btn_selection.setVisibility(View.GONE);
                scope = Constants.FRIEND;
                break;
            case R.id.rdb_near:
                btn_selection.setVisibility(View.VISIBLE);
                scope = Constants.NEARBY;
                break;
            case R.id.rdb_mood:
                disKind = Constants.MOOD;
                break;
            case R.id.rdb_join:
                disKind = Constants.OUTGO;
                break;
        }
        getDiscoveryList();
    }
}
