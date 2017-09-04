package com.westepper.step.wrapper.channelSdk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.westepper.step.R;


/**
 * Created by Mikiller on 2016/3/31.
 */
public abstract class UserWrapper {

    public enum ShareType{
        Default("-1", ""), Discount("1", "优惠详情"), Recommend("2", "推荐有礼"), Game("3", "game");
        String type;
        String name;
        ShareType(String type, String name){
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    protected Context mContext = null;
//    protected ObjectMapper mObjectMapper = null;
    protected boolean hasInit = false;
    public boolean isShare = false;
    public boolean isBinding = false;
    public ShareType shareType = ShareType.Default;
    public Object[] shareContent = null;
    public String extra = "";


    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        switch (shareType){
            case "1":
                this.shareType = ShareType.Discount;
                break;
            case "2":
                this.shareType = ShareType.Game;
                break;
            case "3":
                this.shareType = ShareType.Recommend;
                break;
        }
    }

    public UserWrapper(Context context){
        mContext = context;
    }

//    public void askBeansByShare(String platform) {
//        new AskBeansByShareTask(mContext, new AskBeansByShareTask.AskBeansByShareRequest(platform, extra, shareType.getHot()));
//
//        shareType = ShareType.Default;
//        extra = "";
//    }

//    public void authThirdPlatformFailed(String msg) {
//        ToastUtil.showErrorToast(StringUtil.isEmpty(msg) ? mContext.getString(R.string.my_login_failed) : msg);
//    }
//
//    public void setLoginCallback(OnTaskCompleteListener<?> callback){
//        this.loginCallback = callback;
//    }
//
//    public void startAuthTokenTask(AuthTokenTask.AuthTokenRequest request) {
//        authTokenTask = new AuthTokenTask(mContext, request, loginCallback);
//        authTokenTask.start();
//    }
//
//    public void startAuthTokenBindTask(AuthTokenBindTask.AuthTokenBindRequest request) {
//        authTokenBindTask = new AuthTokenBindTask(mContext, request, loginCallback);
//        authTokenBindTask.start();
//    }

    public boolean isBinding(){
        return isBinding;
    }

    public void binding(){
        isBinding = true;
        login();
    }

    public void onDestory(){
        mContext = null;
        shareContent = null;
    }

    public void DownloadThirdPlatformClient(String platformName, final String url) {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.dialog_title_tip)
                .setMessage("未安装" + platformName + "客户端，是否现在去下载？")
                .setPositiveButton("现在下载", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton("以后再说", null)
                .create()
                .show();
    }

    public abstract boolean init();

    public abstract void login();

    public abstract void share(Object... src);

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void onNewIntent(Intent intent);

    public abstract void onResume();

    public abstract void onPause();

    public abstract boolean checkSDKIsReady();
}
