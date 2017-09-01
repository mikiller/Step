package com.mkframe.step.wrapper.channelSdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Mikiller on 2016/4/6.
 */
public class QQWrapper extends UserWrapper {
    private static final String TAG = QQWrapper.class.getSimpleName();

//    private Tencent mTencent = null;
//    private TencentAuth tencentAuth = null;

    public QQWrapper(Context context) {
        super(context);
//        init(context);
    }

    @Override
    public boolean init(){
        return false;
//        return init(mContext);
    }

//    public boolean init(Context context) {
//        mTencent = Tencent.createInstance(""/*Constants.TENCENT_APP_ID*/, context);
//        return hasInit = (mTencent != null);
//    }

    private boolean checkQQisInstalled() {
//        try {
//            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo("com.tencent.mobileqq", PackageManager.GET_UNINSTALLED_PACKAGES);
//
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            return false;
//        }
        return true;
    }

    @Override
    public boolean checkSDKIsReady() {
        if(!checkQQisInstalled()){
            DownloadThirdPlatformClient("QQ", "http://im.qq.com/download/");
            return false;
        }else if(!hasInit){
            Toast.makeText(mContext, "QQ初始化失败！请稍后再试", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void login() {
//        if (checkSDKIsReady()) {
//            if (mTencent.isSessionValid()) {
//                mTencent.logout(mContext);
//            }
//            mTencent.login((Activity) mContext, ""/*Constants.TENCENT_SCOPES*/, new TencentAuthCallback());
//        }
    }

    @Override
    public void share(Object... src) {
//        if (checkSDKIsReady()) {
//            isShare = true;
//            Bundle bundle = new Bundle();
//            if (src.length > 2)
//                bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, (String) src[2]);
//            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, (String) src[0]);
//            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "卡趣");
////        bundle.putString(com.tencent.connect.common.Constants.PARAM_IMAGE_URL, (String)src[1]);
//            mTencent.shareToQQ((Activity) mContext, bundle, new TencentAuthCallback());
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == com.tencent.connect.common.Constants.REQUEST_API) {
//            if (resultCode == com.tencent.connect.common.Constants.RESULT_LOGIN) {
//                mTencent.handleLoginData(data, new TencentAuthCallback());
//                return;
//            }
//        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestory() {
        super.onDestory();
//        mTencent = null;
//        tencentAuth = null;
    }

//    private class TencentAuthCallback implements IUiListener {
//
//        @Override
//        public void onCancel() {
//            Log.d(TAG, "onCancel");
//        }
//
//
//        public void onComplete(JSONObject response) {
////            try {
////                if (isShare) {
////                    isShare = false;
////                    Log.i(TAG, response.toString());
//////                    tongJiShare(SdkWrapper.PLATFORM.QQ.getPlatform(), "分享-QQ");
//////                    if (shareType.equals("1")) {
//////                        TCAgent.onEvent(mContext, "优惠详情", "分享-QQ");
//////                        ZhuGeWrapper.getInstance().trackEvent("优惠详情", new String[]{"分享-QQ", "成功"});
//////                        askBeansByShare("qq_connect");
//////                    } else
//////                        TCAgent.onEvent(mContext, "推荐有礼", "分享-QQ");
//////                    ZhuGeWrapper.getInstance().trackEvent("推荐有礼", new String[]{"分享-QQ", "分享-QQ 成功"});
////                    return;
////                }
////                tencentAuth = mObjectMapper.readValue(response.toString(), new TypeReference<TencentAuth>() {
////                });
////                PreferenceManager.setTencentOpenId(tencentAuth.getOpenId());
////                setTencentToken(tencentAuth);
////                if (isBinding) {
////                    startAuthTokenBindTask(new AuthTokenBindTask.AuthTokenBindRequest(Constants.TYPE_QQ, tencentAuth.getAccessToken(), String.valueOf(tencentAuth.getExpiresIn()), AppConfigs.getVersionCode(), PreferenceManager.getTencentOpenId(), "",""));
////                    isBinding = false;
////                } else {
////                    startAuthTokenTask(new AuthTokenTask.AuthTokenRequest(Constants.TYPE_QQ, tencentAuth.getAccessToken(), String.valueOf(tencentAuth.getExpiresIn()), AppConfigs.getVersionCode(), PreferenceManager.getTencentOpenId(), PreferenceManager.getKy_app_id(), ""));
////                }
////            } catch (Exception e) {
////                L.d("JsonWapper:" + e.getMessage());
////                authThirdPlatformFailed(e.getMessage());
////            }
//        }
//
//        @Override
//        public void onError(UiError e) {
//            Log.d(TAG, "onError:" + e.errorMessage);
////            authThirdPlatformFailed(e.errorMessage);
//        }
//
//        @Override
//        public void onComplete(Object arg0) {
//            // TODO Auto-generated method stub
//            onComplete((JSONObject) arg0);
//        }
//
//    }

//    public void setTencentToken(TencentAuth tencentAuth) {
//        if (tencentAuth != null) {
//            mTencent.setAccessToken(tencentAuth.getAccessToken(), String.valueOf(tencentAuth.getExpiresIn()));
//            mTencent.setOpenId(tencentAuth.getOpenId());
//        } else {
//            thirdPlatformInfos = new ThirdPlatformInfosEntity(mContext);
//            for (ThirdPlatformInfoEntity entity : thirdPlatformInfos.getPlatformInfos()) {
//                if (entity.getPlatform().equals(Constants.TYPE_QQ)) {
//                    mTencent.setAccessToken(entity.getToken(), entity.getExpire());
//                    mTencent.setOpenId(entity.getOpen_id());
//                }
//            }
//        }
//    }
}
