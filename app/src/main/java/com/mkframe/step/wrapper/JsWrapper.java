package com.mkframe.step.wrapper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Mikiller on 2015/12/3.
 */
public class JsWrapper {
    private final static String TAG = JsWrapper.class.getSimpleName();

    private Activity mActivity;

//    private ShareChannelPannel shareChannelPannel;

    public JsWrapper (){

    }

    public JsWrapper(Activity act){
        this.mActivity = act;
    }

//    public JsWrapper(Activity act, ShareChannelPannel scp){
//        this.mActivity = act;
//        this.shareChannelPannel = scp;
//    }

//    public void share(JSONObject json){
//        try {
//            shareChannelPannel.setTitle(json.getString("title"));
//            shareChannelPannel.setExtra(json.getString("content"));
//            shareChannelPannel.setUrl(json.getString("url"));
//            Bitmap bitmap = HttpManager.getBmpFromUrl(json.getString("imgUrl"));
//            shareChannelPannel.setBitmap(bitmap);
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    shareChannelPannel.setVisibility(View.VISIBLE);
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }

//    public void jumpToJidian(String fragmentNum, String fragment){
//        ActivityMgr.startToMainActivity(mActivity, Integer.valueOf(fragmentNum, 16), Integer.parseInt(fragment));
//    }
//
//    public void jumpToLogin(){
//        ActivityMgr.startToLoginActivity(mActivity);
//    }
//
//    public void onDestory(){
//        mActivity = null;
//        shareChannelPannel = null;
//    }
}
