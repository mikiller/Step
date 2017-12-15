package com.westepper.step.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.uilib.customdialog.CustomDialog;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.MyApplication;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.DiscoverCityLogic;
import com.westepper.step.logics.GetMapDataLogic;
import com.westepper.step.logics.GetReachedListLogic;
import com.westepper.step.models.DiscoverCityModel;
import com.westepper.step.models.MapDataModel;
import com.westepper.step.models.ReachedModel;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.responses.Area;
import com.westepper.step.responses.City;
import com.westepper.step.responses.DiscoveredCities;
import com.westepper.step.responses.Graphics;
import com.westepper.step.responses.MapData;
import com.westepper.step.responses.ReachedList;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.FileUtils;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MapUtils;

import org.apache.lucene.util.automaton.RunAutomaton;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/19.
 */

public class SplashActivity extends SuperActivity {
    @BindView(R.id.iv_logo)
    SelectableRoundedImageView iv_logo;
    @BindView(R.id.iv_label)
    ImageView iv_label;
    MapData tmp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            @Override
            public void onEvent(StatusCode statusCode) {

                if (statusCode.wontAutoLogin()) {
                    //返回登录页
                    if(MyApplication.isLogin) {
                        ActivityManager.startActivity(ActivityManager.lastActivity, WelcomeActivity.class);
                        ActivityManager.lastActivity.back();
                    }
                }
                MyApplication.isLogin = statusCode == StatusCode.LOGINED;
            }
        }, true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                tmp = FileUtils.getDataFromLocal(FileUtils.getFilePath(SplashActivity.this, Constants.MAP_DATA), MapData.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMapData();
                    }
                });
            }
        }).start();

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, 3000);
    }

    private void getMapData(){
        try {
            MapDataModel model = new MapDataModel(tmp == null ? getPackageManager().getPackageInfo(getPackageName(), 0).versionName : tmp.getVersion());
            GetMapDataLogic logic = new GetMapDataLogic(this, model);
            logic.setCallback(new BaseLogic.LogicCallback<MapData>() {
                @Override
                public void onSuccess(MapData response) {

                    String data = new Gson().toJson(response);
                    FileUtils.saveToLocal(data, FileUtils.getFilePath(SplashActivity.this, Constants.MAP_DATA));
                    MapUtils.getInstance().mapData = response;
                }

                @Override
                public void onFailed(String code, String msg, MapData localData) {
                    MapUtils.getInstance().mapData = tmp;
                }
            });
            logic.sendRequest();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startApp(){
        if (!MyApplication.isLogin) {
            //显示微信按钮
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,
                    Pair.create((View)iv_logo, getString(R.string.splash_trans)),
                    Pair.create((View)iv_label, getString(R.string.splash_label)));
            ActivityCompat.startActivity(SplashActivity.this, intent, option.toBundle());
        } else {
            //进入主页
            ActivityManager.startActivity(SplashActivity.this, MainActivity.class);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        }
        back();
    }

    @Override
    protected void initData() {

    }

}
