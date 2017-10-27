package com.westepper.step.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.MyApplication;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.GetMapDataLogic;
import com.westepper.step.models.MapDataModel;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MXPreferenceUtils;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/19.
 */

public class SplashActivity extends SuperActivity {
    @BindView(R.id.iv_logo)
    SelectableRoundedImageView iv_logo;
    @BindView(R.id.iv_label)
    ImageView iv_label;

//    public boolean isLogin = true;
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
                        Log.e(TAG, "wontAutoLogin");
                        ActivityManager.startActivity(ActivityManager.lastActivity, WelcomeActivity.class);
                        ActivityManager.lastActivity.back();
                    }
                }
                MyApplication.isLogin = statusCode == StatusCode.LOGINED;
            }
        }, true);

        getMapData();

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!MyApplication.isLogin) {
                    //显示微信按钮
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,
                            Pair.create((View)iv_logo, getString(R.string.splash_trans)),
                            Pair.create((View)iv_label, getString(R.string.splash_label)));
                    ActivityCompat.startActivity(SplashActivity.this, intent, option.toBundle());
                    back();
                } else {
                    //进入主页
                    NimUIKit.setAccount(MXPreferenceUtils.getInstance().getString("account"));
                    ActivityManager.startActivity(SplashActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                    back();
                }
            }
        }, 3000);
    }

    private void getMapData(){
        try {
            MapDataModel model = new MapDataModel(String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode));
            GetMapDataLogic logic = new GetMapDataLogic(this, model);
            logic.sendRequest();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }
}
