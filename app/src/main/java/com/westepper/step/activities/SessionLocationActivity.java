package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.netease.nim.uikit.LocationProvider;
import com.westepper.step.R;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.utils.MapUtils;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/12/8.
 */

public class SessionLocationActivity extends SuperActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.iv_loc)
    ImageView iv_loc;
    AMap aMap;

    public static LocationProvider.Callback callback;
    private LatLng target;
    private String targetName = "暂无该位置信息";
    private Bundle saveBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveBundle = savedInstanceState;
        setContentView(R.layout.activity_session_location);

    }

    @Override
    protected void initView() {
        titleBar.setSubTxtEnabled(true);
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onSubClicked() {
                callback.onSuccess(target.longitude, target.latitude, targetName);
                back();
            }
        });

        map.onCreate(saveBundle);
        aMap = map.getMap();
        aMap.setMyLocationStyle(MapUtils.getInstance().createLocalStyle(0, MyLocationStyle.LOCATION_TYPE_LOCATE));
        aMap.getMyLocationStyle().showMyLocation(false);
        aMap.setMyLocationEnabled(true);
        aMap.animateCamera(new CameraUpdateFactory().newCameraPosition(new CameraPosition(new LatLng(MapUtils.getInstance().getMapLocation().getLatitude(), MapUtils.getInstance().getMapLocation().getLongitude()), 15, 0, 0)));

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                target = cameraPosition.target;
                MapUtils.getInstance().searchAddressFromLatlng(SessionLocationActivity.this, target, new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        targetName = (i == 1000) ? regeocodeResult.getRegeocodeAddress().getFormatAddress() : "暂无该位置信息";

                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
}
