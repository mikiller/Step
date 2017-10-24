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
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/3/27.
 */

public class MapUtils {
    private static final String TAG = MapUtils.class.getSimpleName();
//    private Context mContext;
    private AMap aMap;
    private MyLocationStyle locationStyle;
    private AMapLocationClient locationClient;
    private GeoFenceClient geoFenceClient;
    private Inner_3dMap_location mapLocation;

    Map<String, Area> areas = new HashMap<>();
    List<String> showAreaIds = new ArrayList();
    boolean needArea = true;
    float currentZoom = 15;
    private BitmapDescriptor markerImg;
    private Marker lastMarker;
    private OnGetLocationListener getLocationListener;

    private MapUtils() {
        markerImg = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker);
        mapLocation = new Inner_3dMap_location("");
//        pos_selected = BitmapDescriptorFactory.fromResource(R.mipmap.icon_pos_selected);
//        isMatched = new HashMap<>();
    }

    private static class MapFactory {
        private static MapUtils instance = new MapUtils();
    }

//    public static MapUtils getInstance(Context context, AMap aMap) {
//        if (MapFactory.instance == null)
//            MapFactory.instance = new MapUtils();
//
//        //if (MapFactory.instance.aMap == null)
//            MapFactory.instance.aMap = aMap;
////        if (MapFactory.instance.mContext == null)
////            MapFactory.instance.mContext = context;
////        if(MapFactory.instance.mapLocation == null)
////            MapFactory.instance.mapLocation = new Inner_3dMap_location("");
//        //if(MapFactory.instance.geoFenceClient == null) {
//            MapFactory.instance.geoFenceClient = new GeoFenceClient(context);
//            MapFactory.instance.geoFenceClient.createPendingIntent("com.map.baidumapdemo.broadcast");
//        //}
//
//        return MapFactory.instance;
//    }

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

    public void init(Context context, AMap aMap){
        this.aMap = aMap;
        initLocationStyle(context, 2000);
        initLocationClient(context);
        initGeoClient(context);
    }

    public void initLocationStyle(Context context, long interval) {
        setCustomStyle(context);
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
                    hideArea();
                }else{
                    showArea();
                }
            }
        });
    }

    private void setCustomStyle(Context context) {
        InputStream fis = null;
        FileOutputStream fos = null;
        String path = context.getFilesDir().getAbsolutePath() + "/" + "mapStyle.data";
        try {
            fis = context.getAssets().open("mystyle_sdk_1503901911_0100.data");
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

    private void hideArea(){
        for(Area area : areas.values()){
            area.hide();
        }
    }

    private void showArea(){
        if(showAreaIds.size() == 0) {
            for (Area area : areas.values()) {
                area.show();
            }
        }else{
            hideArea();
            for(String id : showAreaIds){
                if(areas.get(id) != null)
                    areas.get(id).show();
            }
        }
    }

    public void setIsNeedArea(boolean isNeed){
        needArea = isNeed;
        if(needArea) {
            showArea();
            aMap.setMyLocationStyle(locationStyle);
        }else
            hideArea();
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

    public int getAreasSize(){
        return areas.size();
    }

    public void addArea(Area area, int graphicType){

        if(areas.get(area.getAreaId()) == null){
            Log.e(TAG, "add area: " + area.getAreaId());
            area.createGraphics(aMap, graphicType);
            if (area.getAreaType() == Area.POLYGON)
                createGeoFence(area.getAreaId(), area.getBorderList());
            else if (area.getAreaType() == Area.CIRCLE)
                createGeoFence(area.getAreaId(), area.getCircle().getLatng(), area.getCircle().getRadius());
            areas.put(area.getAreaId(), area);
        }else{
            areas.get(area.getAreaId()).createGraphics(aMap, graphicType);
        }
    }

    public void setAreaChecked(String id){
        if(areas.get(id) == null)
            return;
        areas.get(id).setReached(true);
        //save to local
    }

    public void setAreaType(int type){
        for(Area area : areas.values()){
            area.setGraphicsType(type);
        }
    }

    public void setShowAreaIds(String[] ids){
        if(ids == null || ids.length == 0)
            showAreaIds = new ArrayList<>();
        else{
            showAreaIds = Arrays.asList(ids);
        }
        showArea();
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

    public void initLocationClient(Context context) {
        if(locationClient == null) {
            locationClient = new AMapLocationClient(context);
            locationClient.setLocationOption(createClientOption());
            locationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    Log.e(TAG, "call location " + this.toString() + ", " + locationClient.toString());
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
        }
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

    public void initGeoClient(Context context){
        if(geoFenceClient == null) {
            geoFenceClient = new GeoFenceClient(context);
            geoFenceClient.createPendingIntent("com.map.baidumapdemo.broadcast");
        }
    }

    public void createGeoFence(String id, List<LatLng> pos){
        List<DPoint> points = new ArrayList<>();
        for(LatLng p : pos){
            points.add(new DPoint(p.latitude, p.longitude));
        }
        geoFenceClient.addGeoFence(points, id);

    }

    public void createGeoFence(String id, LatLng center, int radius){
        geoFenceClient.addGeoFence(new DPoint(center.latitude, center.longitude), radius, id);
    }

//    public void registGeoListener(BroadcastReceiver receiver){
//        String geoAction = "com.map.baidumapdemo.broadcast";
//        geoFenceClient.createPendingIntent(geoAction);
//
////        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
////        intentFilter.addAction(geoAction);
////        mContext.registerReceiver(receiver, intentFilter);
//    }
//
//    public void unregistGeoListener(BroadcastReceiver receiver){
//        mContext.unregisterReceiver(receiver);
//    }

    public float getDistance(LatLng start, LatLng end) {
        return AMapUtils.calculateLineDistance(start, end);
    }

    public void searchPoi(Context context, String keyWord, String city, PoiSearch.OnPoiSearchListener listener){
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(20);
        PoiSearch poiSearch = new PoiSearch(context, query);
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
//        if(geoFenceClient != null){
//            geoFenceClient.removeGeoFence();
//            geoFenceClient = null;
//        }
        removeMarker();
        if(areas != null){
            areas.clear();
            areas = null;
        }
        //mContext = null;
        MapFactory.instance = null;
    }

    public interface OnGetLocationListener {
        void onGetLocation(double lat, double lon);
    }
}
