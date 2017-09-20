package com.westepper.step.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.MotionEvent;

import com.amap.api.fence.GeoFenceClient;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.westepper.step.R;
import com.westepper.step.fragments.MapFragment;
import com.westepper.step.responses.Area;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/3/27.
 */

public class MapUtils {
    private static final String TAG = MapUtils.class.getSimpleName();
    private Context mContext;
    private AMap aMap;
    private MyLocationStyle locationStyle;
    private AMapLocationClient locationClient;
    private GeoFenceClient geoFenceClient;
    private Inner_3dMap_location mapLocation;

    Map<String, Area> areas = new HashMap<>();
    boolean needArea = true;
    float currentZoom = 15;
    private BitmapDescriptor markerImg;
    private Marker lastMarker;
    private OnGetLocationListener getLocationListener;

    private MapUtils() {
        markerImg = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker);
//        pos_selected = BitmapDescriptorFactory.fromResource(R.mipmap.icon_pos_selected);
//        isMatched = new HashMap<>();
    }

    private static class MapFactory {
        private static MapUtils instance = new MapUtils();
    }

    public static MapUtils getInstance(Context context, AMap aMap) {
        if (MapFactory.instance == null)
            MapFactory.instance = new MapUtils();

        if (MapFactory.instance.aMap == null)
            MapFactory.instance.aMap = aMap;
        if (MapFactory.instance.mContext == null)
            MapFactory.instance.mContext = context;
        if(MapFactory.instance.mapLocation == null)
            MapFactory.instance.mapLocation = new Inner_3dMap_location("");
        if(MapFactory.instance.geoFenceClient == null)
            MapFactory.instance.geoFenceClient = new GeoFenceClient(context);

        return MapFactory.instance;
    }

    public static MapUtils getInstance() {
        if (MapFactory.instance == null)
            MapFactory.instance = new MapUtils();
        return MapFactory.instance;
    }

    public Inner_3dMap_location getMapLocation() {
        return mapLocation;
    }

    public void setGetLocationListener(OnGetLocationListener getLocationListener) {
        this.getLocationListener = getLocationListener;
    }

    public void moveCamera(LatLng latLng) {
        aMap.animateCamera(new CameraUpdateFactory().newCameraPosition(new CameraPosition(latLng, currentZoom, 0, 0)));
    }

    public void initLocationStyle(long interval) {
        setCustomStyle();
        createLocalStyle(interval);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(currentZoom));

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                currentZoom = cameraPosition.zoom;
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if(!needArea)
                    return;
                if(currentZoom < 13){
                    hideBorder();
                }else{
                    showBorder();
                }
            }
        });
    }

    private void setCustomStyle() {
        InputStream fis = null;
        FileOutputStream fos = null;
        String path = mContext.getFilesDir().getAbsolutePath() + "/" + "mapStyle.data";
        try {
            fis = mContext.getAssets().open("mystyle_sdk_1503901911_0100.data");
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            File file = new File(path);
            if (file.exists())
                file.delete();
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(bytes);
            aMap.setCustomMapStylePath(file.getPath());
            aMap.setMapCustomEnable(true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void stopLocalStyle() {
        aMap.setMyLocationEnabled(false);
    }

    private MyLocationStyle createLocalStyle(long interval) {
        locationStyle = new MyLocationStyle();
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_loaction));
        locationStyle.anchor(0.5f, 0.5f);
        locationStyle.radiusFillColor(Color.TRANSPARENT);
        locationStyle.strokeWidth(0f);
        locationStyle.showMyLocation(true);
        locationStyle.interval(interval);
        aMap.setMyLocationStyle(locationStyle);
        aMap.setMyLocationEnabled(true);
        return locationStyle;
    }

    private void hideBorder(){
        for(Area area : areas.values()){
            area.hide();
        }
    }

    private void showBorder(){
        for(Area area : areas.values()){
            area.show();
        }
    }

    public void setIsNeedArea(boolean isNeed){
        needArea = isNeed;
        if(needArea) {
            showBorder();
            aMap.setMyLocationStyle(locationStyle);
        }else
            hideBorder();
        aMap.setMyLocationEnabled(needArea);
    }

    public void setAMapListeners() {
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mapLocation = (Inner_3dMap_location) location;
                Log.e(TAG, String.format("relocation: %1$f, %2$f", mapLocation.getLatitude(), mapLocation.getLongitude()));
                if (getLocationListener != null)
                    getLocationListener.onGetLocation(mapLocation.getLatitude(), mapLocation.getLongitude());
            }
        });
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            float lastX, lastY;

            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        if(locationTimer != null) {
//                            locationTimer.cancel();
//                            locationTimer = null;
//                        }
                        //aMap.setMyLocationEnabled(false);
                        lastX = motionEvent.getRawX();
                        lastY = motionEvent.getRawY();
//                        Log.e(TAG, "click map");
//                        if(hasMarkerChecked) {
//                            onMarkerUnchecked();
//                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        if (hasMarkerChecked) {
//                            if (Math.abs(motionEvent.getRawX() - lastX) > 10.0f || Math.abs(motionEvent.getRawY() - lastY) > 10.0f) {
//                                onMarkerUnchecked();
//                            }
//                        }
                        break;
                    case MotionEvent.ACTION_UP:
//                        locationTimer = new Timer(false);
//                        locationTimer.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                initLocationStyle(CommonConstanse.TIMER_TASK_DELAY);
//                            }
//                        }, CommonConstanse.RELOCATION_DELAY);
                        break;
                }
            }
        });
    }

    public void clearAll() {
        aMap.clear();
    }

    public void addArea(Area area){
        area.createPolygon(aMap);
        if(areas.get(area.getId()) == null){
            areas.put(area.getId(), area);
        }
    }

    public void addCheckedArea(Area area){
        area.setHasChecked(true);
        area.createPolygon(aMap);
        if(areas.get(area.getId()) == null){
            areas.put(area.getId(), area);
        }
    }

    public void setAreaChecked(String id){
        if(areas.get(id) == null)
            return;
        areas.get(id).setHasChecked(true);
        Polygon polygon = areas.get(id).getPolygon();
        polygon.setStrokeWidth(0);
        polygon.setFillColor(mContext.getResources().getColor(R.color.bolder_sold));
        //save to local
    }

    public void addMarker(LatLng lbs) {
        removeMarker();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lbs)
                .draggable(true).anchor(0.5f, 1.0f)
                .icon(markerImg);

        lastMarker = aMap.addMarker(markerOptions);
        moveCamera(lbs);
    }

    public void removeMarker(){
        if(lastMarker != null){
            lastMarker.remove();
            lastMarker = null;
        }
    }

    public void initLocationClient() {
        if(locationClient == null)
            locationClient = new AMapLocationClient(mContext);
        locationClient.setLocationOption(createClientOption());
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.e(TAG, "call location");
                if (aMapLocation == null)
                    return;
                if (aMapLocation.getErrorCode() == 0) {
                    Log.e(TAG, "map location: " + aMapLocation.getLatitude() + ", " + aMapLocation.getLongitude());
                    mapLocation.setLatitude(aMapLocation.getLatitude());
                    mapLocation.setLongitude(aMapLocation.getLongitude());
                    mapLocation.setAddress(aMapLocation.getAddress());
                }
            }
        });
        startLoaction();
    }

    public void startLoaction(){
        locationClient.startLocation();
    }

    private AMapLocationClientOption createClientOption() {
        AMapLocationClientOption clientOption = new AMapLocationClientOption();
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        clientOption.setNeedAddress(true);
        clientOption.setWifiScan(true);
        clientOption.setOnceLocation(true);
        return clientOption;
    }

    public void createGeoFenceClient(String id, List<LatLng> pos){
        List<DPoint> points = new ArrayList<>();
        for(LatLng p : pos){
            points.add(new DPoint(p.latitude, p.longitude));
        }
        geoFenceClient.addGeoFence(points, id);

    }

    public void registGeoListener(BroadcastReceiver receiver){
        String geoAction = "com.map.baidumapdemo.broadcast";
        geoFenceClient.createPendingIntent(geoAction);

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(geoAction);
        mContext.registerReceiver(receiver, intentFilter);
    }

    public void unregistGeoListener(BroadcastReceiver receiver){
        mContext.unregisterReceiver(receiver);
    }

    public float getDistance(LatLng start, LatLng end) {
        return AMapUtils.calculateLineDistance(start, end);
    }

    public void searchPoi(String keyWord, String city, PoiSearch.OnPoiSearchListener listener){
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(20);
        PoiSearch poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(listener);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mapLocation.getLatitude(), mapLocation.getLongitude()), 3000));
        poiSearch.searchPOIAsyn();
    }

    public void destory() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
            locationClient = null;
        }
        locationStyle = null;
        if (aMap != null) {
            aMap.clear();
            aMap = null;
        }
        if(geoFenceClient != null){
            geoFenceClient.removeGeoFence();
            geoFenceClient = null;
        }
        removeMarker();
        if(areas != null){
            areas.clear();
            areas = null;
        }
        mContext = null;
        MapFactory.instance = null;
    }

    public interface OnGetLocationListener {
        void onGetLocation(double lat, double lon);
    }
}
