package com.westepper.step.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.netease.nim.uikit.LocationProvider;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact.core.query.PinYin;
import com.netease.nim.uikit.custom.DefaultUserInfoProvider;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.westepper.step.R;
import com.westepper.step.activities.MainActivity;
import com.westepper.step.activities.SessionLocationActivity;
import com.westepper.step.responses.ReachedList;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.SessionHelper;


/**
 * Created by Mikiller on 2017/6/6.
 */

public class MyApplication extends Application {
    private final String TAG = this.getClass().getSimpleName();
//    private SdkWrapper sdkWrapper;

    public static UMShareAPI shareAPI;
    public static boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        TCAgent.LOG_ON = true;
        TCAgent.init(this, "BC0AE4111D1940A2A85AD9A423E011EE", "test");
        TCAgent.setReportUncaughtExceptions(true);

        shareAPI = UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxc691f0095ed06444", "c8e1d200ef7b427992635c1bf2b090c7");
//        PlatformConfig.setQQZone("","");
//        PlatformConfig.setSinaWeibo("","","");

        NIMClient.init(this, loginInfo(), options());
        initUIKit();
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {

        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        initStatusBarNotificationConfig(options);

        // 配置保存图片，文件，log等数据的目录
        options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
        options.userInfoProvider = new DefaultUserInfoProvider(this);

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        // 在线多端同步未读数
        options.sessionReadAck = true;

        return options;
    }

    private void initStatusBarNotificationConfig(SDKOptions options) {
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = loadStatusBarNotificationConfig();
    }

    private StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        config.notificationColor = Color.parseColor("#3a9ebf");
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.westepper.step/raw/msg";

        return config;
    }

    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        String account = MXPreferenceUtils.getInstance(this, MXPreferenceUtils.REPORTS).getString("account");
        String token = MXPreferenceUtils.getInstance().getString("token");
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            NimUIKit.setAccount(account);
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initUIKit() {
        if (inMainProcess()) {
            PinYin.init(this);
            PinYin.validate();
            NimUIKit.init(this);
            SessionHelper.init();
            NimUIKit.setLocationProvider(new LocationProvider() {
                @Override
                public void requestLocation(Context context, Callback callback) {
                    SessionLocationActivity.callback = callback;
                    Intent intent = new Intent(context, SessionLocationActivity.class);
                    startActivity(intent);
                }

                @Override
                public void openMap(Context context, double longitude, double latitude, String address) {
                    Intent intent = new Intent(context, SessionLocationActivity.class);
                    intent.putExtra(Constants.LOC_TYPE, SessionLocationActivity.PREVIEW_LOC);
                    intent.putExtra(Constants.LOC, new LatLng(latitude, longitude));
                    intent.putExtra(Constants.ADDRESS, address);
                    startActivity(intent);
                }
            });
        }
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = getProcessName(this);
        return packageName.equals(processName);
    }

    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        android.app.ActivityManager am = ((android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (android.app.ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
