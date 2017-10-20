package com.westepper.step.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.MyApplication;
import com.westepper.step.base.SuperActivity;
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

    public boolean isLogin = true;
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
                    if(isLogin) {
                        Log.e(TAG, "wontAutoLogin");
                        ActivityManager.startActivity(ActivityManager.lastActivity, WelcomeActivity.class);
                        ActivityManager.lastActivity.back();
                    }
                }
                isLogin = statusCode == StatusCode.LOGINED;
            }
        }, true);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isLogin) {
                    //显示微信按钮
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,
                            Pair.create((View)iv_logo, getString(R.string.splash_trans)),
                            Pair.create((View)iv_label, getString(R.string.splash_label)));
                    ActivityCompat.startActivity(SplashActivity.this, intent, option.toBundle());
                    back();
                } else {
                    //进入主页
                    ActivityManager.startActivity(SplashActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                    back();
                }
            }
        }, 3000);
    }

    @Override
    protected void initData() {

    }
}
