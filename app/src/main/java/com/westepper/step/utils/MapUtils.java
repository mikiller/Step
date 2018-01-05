package com.westepper.step.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

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
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.google.gson.Gson;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.City;
import com.westepper.step.responses.DisArea;
import com.westepper.step.responses.Graphics;
import com.westepper.step.responses.MapData;
import com.westepper.step.responses.ReachedList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by Mikiller on 2017/3/27.
 */

public class MapUtils {
    private static final String TAG = MapUtils.class.getSimpleName();
    private AMap aMap;
    private MyLocationStyle locationStyle;
    private AMapLocationClient locationClient;
    private GeoFenceClient geoFenceClient;
    private Inner_3dMap_location mapLocation;

    public MapData mapData;
    public ReachedList reachedList;
    public List<String> localReachedIds;
    Map<String, Area> areas = new HashMap<>();
    Map<String, Area> achieveAreas = new HashMap<>();
    Map<String, DisArea> districtAreas = new HashMap<>();
    Map<String, AchieveArea> achievements = new HashMap<>();
    List<String> showAreaIds = new ArrayList();
    boolean needArea = true;
    float currentZoom = 15;
    private BitmapDescriptor markerImg;
    private Marker lastMarker;
    private OnGetLocationListener getLocationListener;

    private Timer relocationTimer = new Timer();

    private MapUtils() {

        //mapLocation = new Inner_3dMap_location("");
//        pos_selected = BitmapDescriptorFactory.fromResource(R.mipmap.icon_pos_selected);
//        isMatched = new HashMap<>();
    }

    private static class MapFactory {
        private static MapUtils instance = new MapUtils();
    }

    public static MapUtils getInstance() {
        if (MapFactory.instance == null)
            MapFactory.instance = new MapUtils();
        return MapFactory.instance;
    }

    public Inner_3dMap_location getMapLocation() {
        return mapLocation;
    }

    public void setLocalReachedList(List<String> reachedList) {
        if(this.localReachedIds == null)
            this.localReachedIds = new ArrayList<>();
        if (reachedList != null) {
            localReachedIds.addAll(reachedList);
            localReachedIds = new ArrayList<>(new LinkedHashSet<>(reachedList));
        }
    }

    public void saveLocalReachedList(String id){
        if (localReachedIds.contains(id))
            return;
        if (id == null)
            localReachedIds.clear();
        else
            localReachedIds.add(id);
        MXPreferenceUtils.getInstance().setString(Constants.REACHED_ID + SuperActivity.userInfo.getUserId(), new Gson().toJson(localReachedIds));
    }

    public void setGetLocationListener(OnGetLocationListener getLocationListener) {
        this.getLocationListener = getLocationListener;
    }

    public void moveCamera(LatLng latLng) {
//        aMap.animateCamera(new CameraUpdateFactory().newCameraPosition(new CameraPosition(latLng, currentZoom, 0, 0)));
        moveCamera(latLng, currentZoom);
    }

    public void moveCamera(LatLng latLng, float zoom){
        aMap.animateCamera(new CameraUpdateFactory().newCameraPosition(new CameraPosition(latLng, zoom, 0, 0)));
    }

    public void moveToUserPos(){
        moveCamera(new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude()));
    }

    public void init(Context context, AMap aMap) {
        this.aMap = aMap;
        initLocationStyle(context, 5000);
//        initLocationClient(context);
        initGeoClient(context);
    }

    public void initLocationStyle(Context context, long interval) {
        setCustomStyle(context);
        aMap.setMyLocationStyle(locationStyle = createLocalStyle(interval));
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
//        aMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoom));

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            boolean hasshow = false;
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                currentZoom = cameraPosition.zoom;
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!needArea)
                    return;
                if (currentZoom < 11) {
                    hideArea();
                    hasshow = false;
                } else {
                    if (!hasshow) {
                        showArea();
                        hasshow = true;
                    }
                }
            }
        });

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(mapLocation == null) {
                    moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));

                }
                if (getLocationListener != null){
                    getLocationListener.onGetLocation( ((Inner_3dMap_location) location).getCity());
                }
                mapLocation = (Inner_3dMap_location) location;
//                Log.e(TAG, String.format("relocation: %1$f, %2$f", mapLocation.getLatitude(), mapLocation.getLongitude()));

            }
        });

//        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
//            @Override
//            public void onTouch(MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
//                    relocationTimer.cancel();
//                    aMap.setMyLocationEnabled(false);
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP ) {
//                    relocationTimer = new Timer();
//                    relocationTimer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            if(needArea) {
//                                aMap.setMyLocationStyle(locationStyle);
//                                aMap.setMyLocationEnabled(true);
//                            }
//                        }
//                    }, 8000);
//                }
//            }
//        });

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

    private MyLocationStyle createLocalStyle(long interval) {
        return createLocalStyle(interval, MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
    }

    public MyLocationStyle createLocalStyle(long interval, int locationType){
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationType(locationType);
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_loaction));
        locationStyle.anchor(0.5f, 0.5f);
        locationStyle.radiusFillColor(Color.TRANSPARENT);
        locationStyle.strokeWidth(0f);
        locationStyle.showMyLocation(true);
        locationStyle.interval(interval);

        return locationStyle;
    }

    private void hideArea() {
        for (Area area : areas.values()) {
            area.hide();
        }
        for (Area area : achieveAreas.values()) {
            area.hide();
        }
        for (DisArea area : districtAreas.values()){
            area.hide();
        }
    }

    public void showArea() {
        if (showAreaIds.size() == 0) {
            for (Area area : areas.values()) {
                area.show();
            }
            for (Area area : achieveAreas.values()) {
                area.hide();
            }
            for (DisArea area : districtAreas.values()){
                area.show();
            }
        } else {
            hideArea();
            for (String id : showAreaIds) {
                if (areas.get(id) != null)
                    areas.get(id).show();
                else if (achieveAreas.get(id) != null)
                    achieveAreas.get(id).show();
            }
        }
    }

    public void showDisArea(String id){
        if (districtAreas.get(id) != null){
            districtAreas.get(id).show();
        }
    }

    public void setIsNeedArea(boolean isNeed) {
        needArea = isNeed;
        if (needArea) {
            showArea();
            moveToUserPos();
//            moveCamera(new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude()));
        } else
            hideArea();
        locationStyle.showMyLocation(needArea);
    }

    public LatLng getCenterLatLng(String id){
        if(achieveAreas.get(id) != null){
            if(achieveAreas.get(id).getAreaType() == Area.POLYGON)
                return achieveAreas.get(id).getBorderList().get(0);
            else
                return achieveAreas.get(id).getCircle().getLatlng();
        }else if(areas.get(id) != null){
            if(areas.get(id).getAreaType() == Area.POLYGON)
                return areas.get(id).getBorderList().get(0);
            else
                return areas.get(id).getCircle().getLatlng();
        } else{
            return new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude());
        }
    }

    public void clearMapData() {
        currentZoom = 15;
        areas.clear();
        districtAreas.clear();
        achieveAreas.clear();
        achievements.clear();
    }

    public int getAreasSize() {
        return areas.size();
    }

    public void analyzeMapData(){
        for (Area area : mapData.getAchievementAreaList()) {
            addArea(achieveAreas, area, Graphics.ACHEIVE);
        }
        for (City city : mapData.getCityList()) {
            if (!city.getCityName().contains("上海"))
                continue;
            for (Area area : city.getAreaList()) {
                addArea(areas, area, Graphics.MAP);
            }
        }
        for (DisArea da : mapData.getRelations().getL1()){
            if (areas.get(da.getId()) != null) {
                areas.get(da.getId()).setP_id(da.getP_id());
                areas.get(da.getId()).setName(da.getName());
            }
        }
        for (DisArea da : mapData.getRelations().getL2()){
            da.createGraphics(aMap, Graphics.MAP);
            districtAreas.put(da.getId(), da);
        }
        for (DisArea da : mapData.getRelations().getL3()){
            da.createGraphics(aMap, Graphics.MAP);
            districtAreas.put(da.getId(), da);
        }
        for (Achieve achieve : mapData.getAchievementList()) {
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
                addAchievement(achArea);
            }
        }
    }

    private void addArea(Map<String, Area> areas, Area area, int graphicType) {
            if (area.getAreaType() == Area.POLYGON)
                createGeoFence(area.getAreaId(), area.getBorderList());
            else if (area.getAreaType() == Area.CIRCLE)
                createGeoFence(area.getAreaId(), area.getCircle().getLatlng(), area.getCircle().getRadius());
            area.createGraphics(aMap, graphicType);
            areas.put(area.getAreaId(), area);
    }

    public void addAchievement(AchieveArea ach){
        if(achievements.get(ach.getAchieveAreaId()) == null){
            achievements.put(ach.getAchieveAreaId(), ach);
        }
    }

    public Area getArea(String id){
        return areas.get(id);
    }

    public DisArea getDistrictArea(String id){
        return districtAreas.get(id);
    }

    public AchieveArea getAchievement(String id){
        return achievements.get(id);
    }

    public void setAreaChecked(String id, boolean isReached) {
        if (areas.get(id) != null)
            areas.get(id).setReached(isReached);
        else if (achieveAreas.get(id) != null)
            achieveAreas.get(id).setReached(isReached);
        //save to local
    }

    public void setDisAreaChecked(String id, boolean isReached){
        if (districtAreas.get(id) != null)
            districtAreas.get(id).setReached(isReached);
    }

    public void setAreaType(int type) {
        for (Area area : areas.values()) {
            area.setGraphicsType(type);
        }
    }

    public void setAreaType(int type, String[] ids) {
        if (ids == null)
            setAreaType(type);
        else {
            for (String id : ids) {
                if (areas.get(id) != null) {
                    areas.get(id).setGraphicsType(type);
                }
            }
        }
    }

    public void setShowAreaIds(String[] ids) {
        if (ids == null || ids.length == 0)
            showAreaIds = new ArrayList<>();
        else {
            showAreaIds = Arrays.asList(ids);
        }
        showArea();
    }

    public void addMarker(LatLng lbs) {
        removeMarker();
        lastMarker = addMarker(aMap, lbs);
        moveCamera(lbs);
    }

    public Marker addMarker(AMap aMap, LatLng lbs){
        if(markerImg == null || markerImg.getBitmap() == null)
            markerImg = BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lbs)
                .draggable(true).anchor(0.5f, 1.0f)
                .icon(markerImg);
        return aMap.addMarker(markerOptions);

    }

    public void removeMarker() {
        if (lastMarker != null) {
            lastMarker.remove();
            lastMarker = null;
        }
    }

    public void initLocationClient(Context context) {
        if (locationClient == null) {
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
                        mapLocation.setCity(aMapLocation.getCity());
                        mapLocation.setPoiName(aMapLocation.getPoiName());
                    }
                }
            });
        }
        startLoaction();
    }

    public void startLoaction() {
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

    public void initGeoClient(Context context) {
        if (geoFenceClient == null) {
            geoFenceClient = new GeoFenceClient(context);
            geoFenceClient.createPendingIntent(context.getString(R.string.geo_receiver));
//            geoFenceClient.setGeoFenceListener(new GeoFenceListener() {
//                @Override
//                public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {
//                    Log.e(TAG, "errcode : " + i);
//                    Log.e(TAG, "s:" + s + ", " + list.size());
//                }
//            });
        }
    }

    public void createGeoFence(String id, List<LatLng> pos) {
        List<DPoint> points = new ArrayList<>();
        for (LatLng p : pos) {
            points.add(new DPoint(p.latitude, p.longitude));
        }
        geoFenceClient.addGeoFence(points, id);

    }

    public void createGeoFence(String id, LatLng center, int radius) {
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

    public void searchPoi(Context context, String keyWord, String city, PoiSearch.OnPoiSearchListener listener) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(20);
        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(listener);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mapLocation.getLatitude(), mapLocation.getLongitude()), 3000));
        poiSearch.searchPOIAsyn();
    }

    public void searchMapAddress(Context context, String keyWord, String city, GeocodeSearch.OnGeocodeSearchListener listener){
        GeocodeSearch geoSearch = new GeocodeSearch(context);
        geoSearch.setOnGeocodeSearchListener(listener);
        geoSearch.getFromLocationNameAsyn(new GeocodeQuery(keyWord, city));
    }

    public void searchAddressFromLatlng(Context context, LatLng latLng, GeocodeSearch.OnGeocodeSearchListener listener){
        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(listener);
        geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 50, GeocodeSearch.AMAP));

    }

    public void clearMapLocation(){
        mapLocation = null;
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
        if (areas != null) {
            areas.clear();
            areas = null;
        }

        achieveAreas.clear();
        //mContext = null;
        MapFactory.instance = null;
    }

    public interface OnGetLocationListener {
        void onGetLocation(String cityName);
    }
}
