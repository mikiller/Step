package com.westepper.step.wrapper;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.widget.Toast;

import com.westepper.step.wrapper.channelSdk.QQWrapper;
import com.westepper.step.wrapper.channelSdk.UserWrapper;
import com.westepper.step.wrapper.channelSdk.WeiboWrapper;
import com.westepper.step.wrapper.channelSdk.WeixinWrapper;


/**
 * Created by Mikiller on 2016/3/31.
 */
public class SdkWrapper {
    private final static String TAG = SdkWrapper.class.getSimpleName();

    public enum PLATFORM {
        WEIBO(0, "weibo"), WEIXIN(1, "weixin"), QQ(2, "qq_connect")/*, ALIPAY(3, "alipay")*/;
        int type;
        String platform;

        PLATFORM(int type, String platform) {
            this.type = type;
            this.platform = platform;
        }

        public int getType() {
            return type;
        }

        public String getPlatform() {
            return platform;
        }
    }

    public enum PAY_PLATFORM {
        WEIXIN(1), ALIPAY(2);
        int type;

        PAY_PLATFORM(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private boolean hasInit = false;
    private boolean hasLogin = false;

    private SparseArray<UserWrapper> userWrappers = null;
//    private SparseArray<PayWrapperImp> payWrappers = null;

    private static class SdkWrapperFactory {
        private static final SdkWrapper instance = new SdkWrapper();
    }

    private SdkWrapper() {
        userWrappers = new SparseArray<>();
//        payWrappers = new SparseArray<>();
    }

    public static SdkWrapper getInstance(Context context) {
        return SdkWrapperFactory.instance.init(context);
    }

    public static SdkWrapper getInstance() {
        return SdkWrapperFactory.instance;
    }

    private SdkWrapper init(Context context) {
        init(context, PLATFORM.WEIBO);
        init(context, PLATFORM.WEIXIN);
        init(context, PLATFORM.QQ);
        hasInit = true;
        return this;
    }

    public void init(Context context, PLATFORM platform) {
        UserWrapper wrapper = null;
        switch (platform) {
            case WEIBO:
                wrapper = new WeiboWrapper(context);
                break;
            case WEIXIN:
                wrapper = new WeixinWrapper(context);
                break;
            case QQ:
                wrapper = new QQWrapper(context);
                break;
        }
        if(wrapper.init())
            userWrappers.put(platform.getType(), wrapper);
        else
            Toast.makeText(context, platform.getPlatform().concat(" init failed！"), Toast.LENGTH_SHORT).show();
    }

    public boolean hasInit() {
        return hasInit;
    }

    public void login(PLATFORM platform) {
        if (isUserWrappersEmpty(platform)) {
            return;
        }
        userWrappers.get(platform.getType()).login();
    }

    public boolean hasLogin() {
        return hasLogin;
    }

    public void binding(PLATFORM platform) {
        if (isUserWrappersEmpty(platform))
            return;
        userWrappers.get(platform.getType()).binding();
    }

    public boolean isBinding(PLATFORM platform) {
        if (isUserWrappersEmpty(platform))
            return false;
        return userWrappers.get(platform.getType()).isBinding();
    }

    public void share(PLATFORM platform, Object... src) {
        if (isUserWrappersEmpty(platform))
            return;
        userWrappers.get(platform.getType()).share(src);
    }

    public void setExtra(PLATFORM platform, String extra) {
        if (isUserWrappersEmpty(platform))
            return;
        userWrappers.get(platform.getType()).setExtra(extra);
    }

    public void setShareType(PLATFORM platform, String shareType) {
        if (isUserWrappersEmpty(platform))
            return;
        userWrappers.get(platform.getType()).setShareType(shareType);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (userWrappers != null)
            userWrappers.get(PLATFORM.WEIBO.getType()).onActivityResult(requestCode, resultCode, data);
    }

    public void onNewIntent(Intent intent, Context context) {
        if (userWrappers == null)
            return;
        if(userWrappers.get(PLATFORM.WEIBO.getType())!=null)
            userWrappers.get(PLATFORM.WEIBO.getType()).onNewIntent(intent);
        else if(userWrappers.get(PLATFORM.WEIXIN.getType()) != null)
            userWrappers.get(PLATFORM.WEIXIN.getType()).onNewIntent(intent, context);
    }

    public UserWrapper getWrapper(PLATFORM platform) {
        if (isUserWrappersEmpty(platform))
            return null;
        return userWrappers.get(platform.getType());
    }

    public void onResume() {
        if (userWrappers == null || userWrappers.get(PLATFORM.WEIXIN.getType()) == null)
            return;
        userWrappers.get(PLATFORM.WEIXIN.getType()).onResume();
    }

    public void onPause() {
        if (userWrappers == null || userWrappers.get(PLATFORM.WEIXIN.getType()) == null)
            return;
        userWrappers.get(PLATFORM.WEIXIN.getType()).onPause();
    }

    public void onDestory() {
        hasInit = false;
        hasLogin = false;

        if (userWrappers != null) {
            for (int key = PLATFORM.WEIBO.getType(); key <= PLATFORM.QQ.getType(); key++) {
                if (userWrappers.get(key) != null) {
                    userWrappers.get(key).onDestory();
                    userWrappers.put(key, null);
                }
            }
            userWrappers.clear();
            userWrappers = null;
        }

//        if (payWrappers == null)
//            return;
//        for (int key = PAY_PLATFORM.WEIXIN.getType(); key <= PAY_PLATFORM.ALIPAY.getType(); key++) {
//            if (payWrappers.get(key) != null) {
//                payWrappers.get(key).onDestory();
//                payWrappers.put(key, null);
//            }
//        }
//        payWrappers.clear();
//        payWrappers = null;
    }

//    public void setAuthCallback(OnTaskCompleteListener<?> callback) {
//        for (int key = PLATFORM.WEIBO.getHot(); key <= PLATFORM.QQ.getHot(); key++) {
//            if (userWrappers.get(key) != null)
//                userWrappers.get(key).setLoginCallback(callback);
//        }
//    }

//    public void setPayResultListener(PayWrapperImp.PayResultListener payResultListener) {
//        setPayResultListener(payResultListener, PAY_PLATFORM.WEIXIN.getType());
//        setPayResultListener(payResultListener, PAY_PLATFORM.ALIPAY.getType());
//    }
//
//    public void setPayResultListener(PayWrapperImp.PayResultListener payResultListener, int platform){
//        if(payWrappers.get(platform) != null)
//            payWrappers.get(platform).setPayResultListener(payResultListener);
//    }

    private boolean isUserWrappersEmpty(PLATFORM platform) {
        if (userWrappers == null || userWrappers.get(platform.getType()) == null)
            return true;
        else
            return false;
    }

//    private boolean isPayWrappersEmpty(int platform) {
//        if (payWrappers == null || payWrappers.get(platform) == null)
//            return true;
//        else
//            return false;
//    }

//    public void tongJi(PLATFORM platform, String args) {
//        userWrappers.get(platform.getHot()).tongJiShare(platform.getPlatform(), args);
//    }
//
//    public void tongJi(PLATFORM platform, String eventName, String args, String value) {
//        userWrappers.get(platform.getHot()).tongJi(eventName, args, value);
//    }

//    public SdkWrapper initPay(Context context) {
//        initPay(context, PAY_PLATFORM.WEIXIN);
//        initPay(context, PAY_PLATFORM.ALIPAY);
//        return this;
//    }

//    public SdkWrapper initPay(Context context, PAY_PLATFORM platform) {
//        if(payWrappers != null && payWrappers.get(platform.getType()) != null)
//            return this;
//        PayWrapperImp payWrapper = null;
//        switch (platform) {
//            case WEIXIN:
//                payWrapper = new WeixinWrapper(context);
//                break;
//            case ALIPAY:
//                payWrapper = new AlipayWrapper(context);
//                break;
//        }
//        payWrappers.put(platform.getType(), payWrapper);
//        return this;
//    }
//
//    public void pay(int platform, PayModel payEntity) {
//        if (isPayWrappersEmpty(platform))
//            return;
//        payWrappers.get(platform).pay(payEntity);
//    }
//
//    public PayWrapperImp getPayWrapper(PAY_PLATFORM platform) {
//        return payWrappers.get(platform.getType());
//    }
}
