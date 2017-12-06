package com.westepper.step.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.uilib.customdialog.CustomDialog;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.adapters.DiscoveryAdapter;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.AcheiveSettingLayout;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.customViews.SearchView;
import com.westepper.step.logics.DiscoverCityLogic;
import com.westepper.step.logics.GetDiscoveryListLogic;
import com.westepper.step.logics.GetReachedListLogic;
import com.westepper.step.models.DiscoverCityModel;
import com.westepper.step.models.DiscoveryListModel;
import com.westepper.step.models.ReachedModel;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.City;
import com.westepper.step.responses.DiscoveredCities;
import com.westepper.step.responses.DiscoveryList;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.Graphics;
import com.westepper.step.responses.ReachedList;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MXTimeUtils;
import com.westepper.step.utils.MapUtils;

import java.util.HashMap;
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
    @BindView(R.id.btn_loc)
    ImageButton btn_loc;
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
    float searchHeight, headTransY, vpTransY, optTransY, locTransY;
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
        getReachedList();
        search.setOnClickListener(this);
        btn_loc.setOnClickListener(this);
        btn_acheivement.setOnClickListener(this);
        rdg_scope.setOnCheckedChangeListener(this);
        rdg_kind.setOnCheckedChangeListener(this);
        btn_new.setOnClickListener(this);
        btn_selection.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);

        search.setSearchListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    ((SuperActivity)getActivity()).hideInputMethod(search);
                    mapUtils.searchMapAddress(getActivity(), v.getText().toString(), mapUtils.getMapLocation().getCity(), new GeocodeSearch.OnGeocodeSearchListener() {
                        @Override
                        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                        }

                        @Override
                        public void onGeocodeSearched(GeocodeResult geocodeResult, int rstCode) {
                            if(rstCode != AMapException.CODE_AMAP_SUCCESS)
                                return;
                            LatLonPoint point = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                            mapUtils.moveCamera(new LatLng(point.getLatitude(), point.getLongitude()));
                        }
                    });
                }
                return false;
            }
        });

        commitInput.setDY(DisplayUtil.dip2px(getActivity(), 50));
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
                final Discovery disc = adapter.getItem(position);
                mapUtils.addMarker(disc.getUserPos().getLatlng());

                adapter.cancelTask();
                if (!MXTimeUtils.isOutofLimit(disc.getPushTime(), MXTimeUtils.DAY)) {
                    adapter.setCurrentHolderListener(new DiscoveryAdapter.getCurrentHolderListener() {
                        @Override
                        public void getCurrentHolder(DiscoveryAdapter.DiscoveryHolder holder) {
                            holder.updateLeftTime(disc.isJoin(), disc.getDiscoveryUserId(), disc.getPushTime());
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        getReachedList();
    }

    private void initMapUtil() {
        mapUtils = MapUtils.getInstance();
        mapUtils.init(getActivity().getApplicationContext(), mapView.getMap());
        for (City city : mapUtils.mapData.getCityList()) {
            if (!city.getCityName().equals("上海"))
                continue;
            for (Area area : city.getAreaList()) {
                mapUtils.addArea(area, Graphics.MAP);
            }
        }
        for (Area area : mapUtils.mapData.getAchievementAreaList()) {
            mapUtils.addAchieveArea(area);
        }
        for (Achieve achieve : mapUtils.mapData.getAchievementList()) {
            for (AchieveArea achArea : achieve.getAchieveAreaList()) {
                switch (achArea.getCredit_level()) {
                    case "1":
                        achArea.setImgId(R.mipmap.ic_ach_l1);
                        break;
                    case "10":
                        achArea.setImgId(R.mipmap.ic_ach_l2);
                        break;
                    case "100":
                        achArea.setImgId(R.mipmap.ic_ach_l3);
                        break;
                    case "1000":
                        achArea.setImgId(R.mipmap.ic_ach_l4);
                        break;
                }
                mapUtils.addAchievement(achArea);
            }
        }
        mapUtils.setGetLocationListener(new MapUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(final String cityName) {
                DiscoverCityModel model = new DiscoverCityModel(cityName);
                final String tmp = MXPreferenceUtils.getInstance().getString(model.getUserId() + "_discities");
                DiscoveredCities disCity = new Gson().fromJson(tmp, DiscoveredCities.class);
                if (disCity != null) {
                    for (DiscoveredCities.DiscoverCity city : disCity.getDiscoverCitys()) {
                        if (cityName.equals(city.getCity_name()))
                            return;
                    }
                }
                DiscoverCityLogic logic = new DiscoverCityLogic(getActivity(), new DiscoverCityModel(cityName));
                logic.setCallback(new BaseLogic.LogicCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        final CustomDialog dlg = new CustomDialog(getActivity());
                        dlg.setLayoutRes(R.layout.layout_congratulation).setCancelable(true);
                        View view = dlg.getCustomView();
                        ((TextView)view.findViewById(R.id.tv_con_txt)).setText("发现" + cityName);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dlg.dismiss();
                            }
                        });
                        dlg.show();
                    }

                    @Override
                    public void onFailed(String code, String msg, Object localData) {

                    }
                });
                logic.sendRequest();
            }
        });

    }

    private void getReachedList() {
        GetReachedListLogic logic = new GetReachedListLogic(getActivity(), new ReachedModel(""));
        logic.setCallback(new BaseLogic.LogicCallback<ReachedList>() {
            @Override
            public void onSuccess(ReachedList response) {
                for (String id : response.getReachedLists()) {
                    mapUtils.setAreaChecked(id);
                }
                mapUtils.mapData.setReachedAchieveIdList(response.getReachedAchievementIds());
            }

            @Override
            public void onFailed(String code, String msg, ReachedList localData) {

            }
        });
        logic.sendRequest();
    }

    private void initAcheiveSetting() {
        layout_achSetting.setAchievementList(mapUtils.mapData.getAchievementList());
        layout_achSetting.setAchieveSettingListener(new AcheiveSettingLayout.onAchieveSettingListener() {
            @Override
            public void onAchieveSelected(String[] areaId, String achieveKind, String centerId) {
                if (achieveKind.equals("探索地图")) {
                    mapUtils.setAreaType(Graphics.MAP);
                    mapUtils.moveToUserPos();
                } else {
                    mapUtils.setAreaType(Graphics.ACHEIVE, areaId);
                    mapUtils.moveCamera(mapUtils.getCenterLatLng(centerId), 15);
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
            locTransY = btn_loc.getMeasuredHeight() + DisplayUtil.dip2px(getActivity(), 14);
        }
        if (isTrack) {
            AnimUtils.startObjectAnim(ll_search, "translationY", -searchHeight, 0, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", 0, headTransY, 300);
            AnimUtils.startObjectAnim(ll_discovery_opt, "translationY", 0, optTransY, 400);
            AnimUtils.startObjectAnim(vp_discoveryList, "translationY", vp_discoveryList.getTranslationY(), vpTransY, 800);
            AnimUtils.startObjectAnim(btn_loc, "translationY", locTransY, 0, 300);
            mapUtils.removeMarker();
            mapUtils.setIsNeedArea(true);
        } else {
            AnimUtils.startObjectAnim(ll_search, "translationY", 0, -searchHeight, 300);
            AnimUtils.startObjectAnim(rl_head, "translationY", headTransY, 0, 300);
            AnimUtils.startObjectAnim(ll_discovery_opt, "translationY", optTransY, 0, 400);
            AnimUtils.startObjectAnim(btn_loc, "translationY", 0, locTransY, 300);
            mapUtils.setIsNeedArea(false);
            getDiscoveryList();
        }

    }

    private void getDiscoveryList() {
        if (!isTrack) {
            if (vp_discoveryList.getTranslationY() == 0)
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
                    if (response.getDiscoveryList().size() > 0)
                        mapUtils.addMarker(response.getDiscoveryList().get(0).getUserPos().getLatlng());
                }
            }

            @Override
            public void onFailed(String code, String msg, DiscoveryList localData) {
                adapter.setDataList(localData == null ? null : localData.getDiscoveryList());
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && adapter != null) {
            adapter.cancelTask();
        }
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
        mapUtils.clearMapLocation();
        super.onDestroyView();
    }

    @Override
    public void fragmentCallback(int type, Intent data) {
        if (type == Constants.SHOW_ACHIEVE_AREA) {
            String achId = data.getStringExtra(Constants.ACH_ID);
            String cateId = data.getStringExtra(Constants.ACH_CATEGORY);
            layout_achSetting.checkAchItem(cateId, achId);
        } else {
            setIsTrack(type == R.id.rdb_track);
            if(layout_achSetting != null && layout_achSetting.isShown())
                layout_achSetting.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_search:
                commitInput.setNeedShow(false);
                break;
            case R.id.btn_loc:
                mapUtils.moveCamera(new LatLng(mapUtils.getMapLocation().getLatitude(), mapUtils.getMapLocation().getLongitude()));
                break;
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
