package com.westepper.step.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.tendcloud.tenddata.TCAgent;
import com.uilib.utils.DisplayUtil;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.westepper.step.R;
import com.westepper.step.activities.MainActivity;
import com.westepper.step.utils.MXPreferenceUtils;


/**
 * Created by Mikiller on 2017/6/6.
 */

public class MyApplication extends Application {
//    private SdkWrapper sdkWrapper;

    public static UMShareAPI shareAPI;
    @Override
    public void onCreate() {
        super.onCreate();
        TCAgent.LOG_ON = true;
        TCAgent.init(this, "BC0AE4111D1940A2A85AD9A423E011EE", "test");
        TCAgent.setReportUncaughtExceptions(true);

        shareAPI = UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxc691f0095ed06444","c8e1d200ef7b427992635c1bf2b090c7");
//        PlatformConfig.setQQZone("","");
//        PlatformConfig.setSinaWeibo("","","");
    }

}
