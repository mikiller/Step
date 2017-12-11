package com.westepper.step.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.netease.nim.uikit.LocationProvider;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.utils.MapUtils;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/12/8.
 */

public class SessionLocationActivity extends SuperActivity {

    public static int CHOOSE_LOC = 1, PREVIEW_LOC = 0;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.map)
    MapView map;
    //    @BindView(R.id.iv_loc)
//    ImageView iv_loc;
    AMap aMap;

    int locationType;

    public static LocationProvider.Callback callback;
    private LatLng target;
    private String targetName = "暂无该位置信息";
    private Bundle saveBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null)
            locationType = savedInstanceState.getInt(Constants.LOC_TYPE);
        else if (getIntent() != null)
            locationType = getIntent().getIntExtra(Constants.LOC_TYPE, CHOOSE_LOC);
        super.onCreate(savedInstanceState);
        saveBundle = savedInstanceState;
        setContentView(R.layout.activity_session_location);

    }

    @Override
    protected void initView() {
        titleBar.setSubStyle(locationType == PREVIEW_LOC ? TitleBar.NONE : TitleBar.TXT);
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

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            TextView infoWindow;

            private void createInfoWindow() {
                infoWindow = new TextView(SessionLocationActivity.this);
                infoWindow.setMaxWidth(DisplayUtil.getScreenWidth(SessionLocationActivity.this) - 100);
                infoWindow.setBackgroundResource(R.color.bg_mask);
                infoWindow.setTextColor(Color.WHITE);
                infoWindow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                infoWindow.setPadding(15, 10, 15, 10);
            }

            @Override
            public View getInfoWindow(Marker marker) {
                if (infoWindow == null)
                    createInfoWindow();
                infoWindow.setText(TextUtils.isEmpty(targetName) ? "暂无位置信息" : targetName);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        if (locationType == CHOOSE_LOC) {
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                Marker marker;
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if(marker != null)
                        marker.hideInfoWindow();
                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    target = cameraPosition.target;
                    MapUtils.getInstance().searchAddressFromLatlng(SessionLocationActivity.this, target, new GeocodeSearch.OnGeocodeSearchListener() {
                        @Override
                        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                            targetName = (i == 1000) ? regeocodeResult.getRegeocodeAddress().getFormatAddress() : "暂无该位置信息";
                            if(marker != null)
                                marker.remove();
                            marker = MapUtils.getInstance().addMarker(aMap, target);
                            marker.showInfoWindow();
                        }

                        @Override
                        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                        }
                    });

                }
            });

            target = new LatLng(MapUtils.getInstance().getMapLocation().getLatitude(), MapUtils.getInstance().getMapLocation().getLongitude());
        } else {
            target = getIntent().getParcelableExtra(Constants.LOC);
            targetName = getIntent().getStringExtra(Constants.ADDRESS);
            MapUtils.getInstance().addMarker(aMap, target).showInfoWindow();
        }
        moveCamera();
    }

    private void moveCamera(){
        aMap.animateCamera(new CameraUpdateFactory().newCameraPosition(new CameraPosition(target, 15, 0, 0)));
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
        outState.putInt(Constants.LOC_TYPE, locationType);
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }
}
