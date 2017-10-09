package com.westepper.step.wrapper.channelSdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.westepper.step.R;
import com.westepper.step.wrapper.SdkWrapper;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by Mikiller on 2016/4/5.
 */
public class WeixinWrapper extends UserWrapper {
    private final static String TAG = WeixinWrapper.class.getSimpleName();

    private IWXAPI weixinApi = null;

    private boolean is2Penyou = false;

    public boolean is2Penyou() {
        return is2Penyou;
    }

    public WeixinWrapper(Context context) {
        super(context);
//        init(context);
    }

    @Override
    public boolean init(){
        return init(mContext);
    }

    public boolean init(Context context) {
        return init(context, context.getString(R.string.wxappid));
    }

    public boolean init(Context context, String appId){
        weixinApi = WXAPIFactory.createWXAPI(context, appId, true);
        hasInit = weixinApi.registerApp(appId);
        return hasInit;
    }

    @Override
    public boolean checkSDKIsReady() {
        if (!weixinApi.isWXAppInstalled() || !weixinApi.isWXAppSupportAPI()) {
            DownloadThirdPlatformClient("微信", "http://weixin.qq.com");
            return false;
        }else if(!hasInit) {
            Toast.makeText(mContext, "微信初始化失败！请稍后再试", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private WXMediaMessage getWeixinMsg(Object... src) {
        WXWebpageObject web = new WXWebpageObject();
        web.webpageUrl = (String) src[3];
        WXMediaMessage msg = new WXMediaMessage(web);
        //字符串拼接待修改
        msg.title = (String) src[1];
        msg.description = "邀请好友，优惠同享";
//        msg.thumbData = BitmapHelper.bmpToByteArray((Bitmap) src[2], 50, false);
        return msg;
    }

    private SendMessageToWX.Req getWeixinReq(boolean is2Penyou, WXMediaMessage msg) {
        this.is2Penyou = is2Penyou;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = is2Penyou ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        return req;
    }

    @Override
    public void login() {
        if (checkSDKIsReady()) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "step";
            weixinApi.sendReq(req);
        }
    }

    @Override
    public void share(Object... src) {
        if (checkSDKIsReady())
            weixinApi.sendReq(getWeixinReq((Boolean) src[0], getWeixinMsg(src)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent, Context context) {
        weixinApi.handleIntent(intent, (IWXAPIEventHandler) context);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onResume() {
        mContext.registerReceiver(wxReceiver, new IntentFilter(SdkWrapper.PLATFORM.WEIXIN.getPlatform()));
    }

    @Override
    public void onPause() {
        try {
            mContext.unregisterReceiver(wxReceiver);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    @Override
    public void onDestory() {
        super.onDestory();
        if(weixinApi != null)
            weixinApi.detach();
        weixinApi = null;
        wxReceiver = null;
//        payResultListener = null;
    }

    public void handlerIntent(Intent intent, IWXAPIEventHandler handler) {
        if (weixinApi != null && handler != null && intent != null)
            weixinApi.handleIntent(intent, handler);
    }

    private BroadcastReceiver wxReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            try {
//                WeiXinAuth weixinAuth = mObjectMapper.readValue(intent.getStringExtra("jsonweixin"), WeiXinAuth.class);
//                Log.i("weixinAuth", weixinAuth.getToken());
                if (!isBinding) {
//                    startAuthTokenTask(new AuthTokenTask.AuthTokenRequest(Constants.TYPE_WEIXIN, weixinAuth.getToken(), weixinAuth.getExpiresIn(), AppConfigs.getVersionCode(), weixinAuth.getOpenid(), PreferenceManager.getKy_app_id(), weixinAuth.getUnionid()));
                } else {
//                    startAuthTokenBindTask(new AuthTokenBindTask.AuthTokenBindRequest(Constants.TYPE_WEIXIN, weixinAuth.getToken(), weixinAuth.getExpiresIn(), AppConfigs.getVersionCode(), weixinAuth.getOpenid(), weixinAuth.getUnionid(), ""));
                    isBinding = false;
                }
//            } catch (JsonParseException e) {
//                Log.i("weixinAuth", e.toString());
//            } catch (JsonMappingException e) {
//                Log.i("weixinAuth", e.toString());
//            } catch (IOException e) {
//                Log.i("weixinAuth", e.toString());
//                e.printStackTrace();
//            }
        }
    };



    /**
     * ***************************支付部分***************************
    * */
//    private PayResultListener payResultListener;
//
//    @Override
//    public PayResultListener getPayResultListener(){
//       return payResultListener;
//    }
//
//    @Override
//    public void setPayResultListener(PayResultListener payResultListener){
//        this.payResultListener = payResultListener;
//    }
//
//    private static String WXOrderNo;
//
//    @Override
//    public void pay(PayModel payEntity) {
//        init(mContext, getPayParam(payEntity, "appid"));
//        if(checkSDKIsReady()) {
//            WXOrderNo = payEntity.getOrder_no();
//            PayReq request = new PayReq();
////        request.appId = Constants.WEBXIN_APP_ID;
//            request.appId = getPayParam(payEntity, "appid");
//            request.packageValue = "Sign=WXPay";
//            request.partnerId = getPayParam(payEntity, "partnerid");
//            request.prepayId = getPayParam(payEntity, "prepayid");
//            request.nonceStr = getPayParam(payEntity, "noncestr");
//            request.timeStamp = getPayParam(payEntity, "timeStamp");
//            request.sign = getPayParam(payEntity, "sign");
//            weixinApi.sendReq(request);
//        }
//    }
//
//    @Override
//    public String getPayParam(PayModel entity, String key) {
//        try {
//            return entity.getValueList().get(entity.getKeyList().indexOf(key));
//        } catch (ArrayIndexOutOfBoundsException aiobe) {
//            return "";
//        }
//    }
//
//    @Override
//    public String getPayParam(PayModel entity) {
//        return null;
//    }
//
//    public void weixinPayCallback(int ErrCode) {
//        init(mContext.getApplicationContext(), ""/*Constants.WEBXIN_APP_ID*/);
//        if (payResultListener == null)
//            return;
//        switch (ErrCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                payResultListener.onPaySuccessed(WXOrderNo);
//                break;
//            case BaseResp.ErrCode.ERR_COMM:
//                payResultListener.onPayFailed(WXOrderNo);
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                Toast.makeText(mContext, WXOrderNo, Toast.LENGTH_SHORT).showPolygon();
//                payResultListener.onPayCanceled(WXOrderNo);
//                break;
//        }
//    }


}
