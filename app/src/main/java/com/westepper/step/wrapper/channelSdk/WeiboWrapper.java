package com.westepper.step.wrapper.channelSdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.westepper.step.wrapper.channelSdk.UserWrapper;


/**
 * Created by Mikiller on 2016/3/31.
 */
public class WeiboWrapper extends UserWrapper {
    private final static String TAG = WeiboWrapper.class.getSimpleName();

//    private AuthInfo weiboAuthInfo = null;
//    private SsoHandler weiboSsoHandler = null;
//    private Oauth2AccessToken weiboOauth2AccessToken = null;
//    private IWeiboShareAPI weiboShareApi = null;
//    private static IWeiboHandler.Response weiboResponse = null;

//    public void setOauth2AccessToken(ThirdPlatformInfoEntity info) {
//        if (weiboOauth2AccessToken == null)
//            weiboOauth2AccessToken = new Oauth2AccessToken(info.getToken(), info.getExpire());
//        else {
//            weiboOauth2AccessToken.setToken(info.getToken());
//            weiboOauth2AccessToken.setExpiresIn(info.getExpire());
//        }
//    }

    public WeiboWrapper(Context context){
        super(context);
//        init(context);
    }

    @Override
    public boolean init(){
        return init(mContext);
    }
    public boolean init(Context context){
//        weiboAuthInfo = new AuthInfo(context, Constants.WEIBO_APP_ID, Constants.WEIBO_APP_REDIRECT, Constants.WEIBO_APP_SCOPE);
//        weiboAuthInfo = new AuthInfo(context, "", "", "");
//        weiboSsoHandler = new SsoHandler((Activity) context, weiboAuthInfo);
        return registWeiboShareApi(context);
    }

    public boolean registWeiboShareApi(Context context) {
//        weiboShareApi = WeiboShareSDK.createWeiboAPI(context, ""/*Constants.WEIBO_APP_ID*/);
//        hasInit = weiboShareApi.registerApp();
//        if (context instanceof IWeiboHandler.Response)
//            weiboResponse = (IWeiboHandler.Response) context;
        return hasInit;
    }

    @Override
    public boolean checkSDKIsReady() {
        if(!hasInit)
            Toast.makeText(mContext, "微博初始化失败！请稍后再试", Toast.LENGTH_SHORT).show();
        return hasInit;
    }

    @Override
    public void login(){
//        if(checkSDKIsReady())
//            weiboSsoHandler.authorize(new AuthListener());
    }

    @Override
    public void share(Object... src){
//        if(checkSDKIsReady()) {
//            if(weiboOauth2AccessToken == null){
//                setWeiboToken(null);
//            }
//            if (weiboOauth2AccessToken == null || !weiboOauth2AccessToken.isSessionValid()/* || !weiboSsoHandler.isWeiboAppInstalled()*/) {
//                isShare = true;
//                shareContent = src;
//                login();
//            } else {
////                tongJiShare(SdkWrapper.PLATFORM.WEIBO.getPlatform(), "分享-新浪微博");
//                weiboShareApi.sendRequest((Activity) mContext, getWeiboRequest(getWeiboMessage(getMediaObjects(src))), weiboAuthInfo, weiboOauth2AccessToken.getToken(), new AuthListener());
//            }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (weiboSsoHandler != null) {
//            weiboSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
    }

    @Override
    public void onNewIntent(Intent intent) {
//        if (weiboShareApi != null)
//            weiboShareApi.handleWeiboResponse(intent, weiboResponse);
    }

    @Override
    public void onResume() {}

    @Override
    public void onPause() {}

    @Override
    public void onDestory() {
        super.onDestory();
//        weiboAuthInfo = null;
//        weiboSsoHandler = null;
//        weiboOauth2AccessToken = null;
//        weiboShareApi = null;
//        weiboResponse = null;
    }

//    private class AuthListener implements WeiboAuthListener {
//
//        @Override
//        public void onCancel() {
//            // TODO Auto-generated method stub
//            if (isShare) {
//                isShare = false;
//                Toast.makeText(mContext, "取消分享", Toast.LENGTH_SHORT).show();
//            }
////            authThirdPlatformFailed(null);
//        }
//
//        @Override
//        public void onComplete(Bundle value) {
//            // TODO Auto-generated method stub
//
////            expiresIn = value.getString("expires_in");
//            if(weiboOauth2AccessToken == null)
//                setWeiboToken(value);
//            if (weiboOauth2AccessToken.isSessionValid()) {
//                //mHandler.sendEmptyMessage(HandlerKeys.HANDLER_SHOW_PROGRESS);
//                if (isShare) {
////                    ((AbsActivity) mContext).setHasFinishAnimation(true);
//                    isShare = false;
//                    share(shareContent);
//                }else{}
////                    new AccountAPI(mContext, Constants.WEIBO_APP_ID, weiboOauth2AccessToken).getUid(new WeiboCallback());
//            } else {
//                // 以下几种情况，您会收到 Code：
//                // 1. 当您未在平台上注册的应用程序的包名与签名时；
//                // 2. 当您注册的应用程序包名与签名不正确时；
//                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
//                String code = value.getString("code");
//                String message = "授权失败";
//                if (!TextUtils.isEmpty(code)) {
//                    message = message.concat("\nObtained the code: ").concat(code);
//                }
////                authThirdPlatformFailed(message);
//            }
//        }
//
//        @Override
//        public void onWeiboException(WeiboException exception) {
//            // TODO Auto-generated method stub
////            authThirdPlatformFailed(exception.getMessage());
//        }
//
//    }

    public void setWeiboToken(Bundle value) {
//        if (value != null) {
//            weiboOauth2AccessToken = Oauth2AccessToken.parseAccessToken(value);
//        } else {
////            thirdPlatformInfos = new ThirdPlatformInfosEntity(mContext);
////            for (ThirdPlatformInfoEntity entity : thirdPlatformInfos.getPlatformInfos()) {
////                if (entity.getPlatform().equals(Constants.TYPE_WEIBO)) {
////                    setOauth2AccessToken(entity);
////                    break;
////                }
////            }
//        }
    }

//    private class WeiboCallback implements RequestListener {
//
//        @Override
//        public void onComplete(String response) {
//            // TODO Auto-generated method stub
////            try {
////                if (response.contains("uid")) {
////                    WeiboGetUid weiboGetUid = mObjectMapper.readValue(response, new TypeReference<WeiboGetUid>() {
////                    });
////                    new UsersAPI(mContext, Constants.WEIBO_APP_ID, weiboOauth2AccessToken).show(weiboGetUid.getUid(), this);
//////                } else if (isShare) {
//////                    ((AbsActivity) mContext).setHasFinishAnimation(true);
//////                    isShare = false;
//////                    share(shareContent);
////                } else if (isBinding) {
////                    startAuthTokenBindTask(new AuthTokenBindTask.AuthTokenBindRequest(Constants.TYPE_WEIBO, weiboOauth2AccessToken.getToken(), String.valueOf(weiboOauth2AccessToken.getExpiresTime() / 1000)/*expiresIn*/, AppConfigs.getVersionCode(), "", "", weiboOauth2AccessToken.getRefreshToken()));
////                    isBinding = false;
////                } else {
////                    startAuthTokenTask(new AuthTokenTask.AuthTokenRequest(Constants.TYPE_WEIBO, weiboOauth2AccessToken.getToken(), String.valueOf(weiboOauth2AccessToken.getExpiresTime() / 1000)/*expiresIn*/, AppConfigs.getVersionCode(), "", PreferenceManager.getKy_app_id(), ""));
////                }
////            } catch (Exception e) {
////                authThirdPlatformFailed(null);
////            }
//        }
//
//        @Override
//        public void onWeiboException(WeiboException exception) {
//            // TODO Auto-generated method stub
////            authThirdPlatformFailed(exception.getMessage());
//        }
//
//    }

//    private ArrayList<BaseMediaObject> getMediaObjects(Object... src) {
//        if (src == null || src.length == 0)
//            return null;
//        ArrayList<BaseMediaObject> mediaList = new ArrayList<BaseMediaObject>();
//        mediaList.add(createTextObject((String) src[0]));
//        if (src.length > 1)
//            mediaList.add(createImageObject((Bitmap) src[1]));
//        if (src.length > 2)
//            mediaList.add(createWebpageObject((Bitmap) src[1], (String) src[2]));
//        return mediaList;
//    }
//
//    private TextObject createTextObject(String text) {
//        TextObject textObject = new TextObject();
//        textObject.text = text;
//        return textObject;
//    }
//
//    private ImageObject createImageObject(Bitmap bitMap) {
//        ImageObject imageObject = new ImageObject();
//        imageObject.setImageObject(bitMap);
//        return imageObject;
//    }
//
//    private WebpageObject createWebpageObject(Bitmap bitmap, String url) {
//        WebpageObject webpageObject = new WebpageObject();
//        webpageObject.identify = Utility.generateGUID();
//        webpageObject.title = "分享有奖";
//        webpageObject.description = "邀请好友，优惠同享";
//
//        // 设置 Bitmap 类型的图片到视频对象里
//        webpageObject.setThumbImage(bitmap);
//        webpageObject.actionUrl = url;
//
//        return webpageObject;
//    }
//
//    private WeiboMultiMessage getWeiboMessage(ArrayList<BaseMediaObject> medias) {
//        if (medias == null || medias.size() <= 0)
//            return null;
//        WeiboMultiMessage msg = new WeiboMultiMessage();
//        msg.textObject = (TextObject) medias.get(0);
//        if (medias.size() > 1)
//            msg.imageObject = (ImageObject) medias.get(1);
//        if (medias.size() > 2)
//            msg.mediaObject = medias.get(2);
//        return msg;
//    }
//
//    private SendMultiMessageToWeiboRequest getWeiboRequest(WeiboMultiMessage msg) {
//        if (msg == null)
//            return null;
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = msg;
//        return request;
//    }

}
