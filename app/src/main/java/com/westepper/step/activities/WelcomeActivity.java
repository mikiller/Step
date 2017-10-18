package com.westepper.step.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.westepper.step.R;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.MyApplication;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.LoginLogic;
import com.westepper.step.models.SignModel;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;

import java.util.Map;

import butterknife.BindView;


public class WelcomeActivity extends SuperActivity {

    @BindView(R.id.btn_weixinLogin)
    Button btn_weixinLogin;

    public static WXHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler = new WXHandler();
    }

    @Override
    protected void initView() {
        btn_weixinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login 微信
                MyApplication.shareAPI.getPlatformInfo(WelcomeActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, final Map<String, String> map) {
                        final SignModel model = new SignModel(map.get("uid"));
                        LoginLogic logic = new LoginLogic(WelcomeActivity.this, model);
                        logic.setCallback(new BaseLogic.LogicCallback() {
                            @Override
                            public void onSuccess(Object response) {

                            }

                            @Override
                            public void onFailed(String code, String msg, Object localData) {
                                if("1".equals(code)){
//                                    model.setHeadImg(map.get("iconurl"));
//                                    model.setNickName(map.get("name"));
//                                    model.setGender("男".equals(map.get("gender")) ? 1 : 2);
                                    ActivityManager.startActivity(WelcomeActivity.this, RegisterActivity.class, map);
                                }
                            }
                        });
                        logic.sendRequest();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Log.e(TAG, throwable.toString());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
    }

    public class WXHandler extends Handler{
        public WXHandler(){}
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Constants.WX_LOGIN){
                ActivityManager.startActivity(WelcomeActivity.this, RegisterActivity.class);
                back();
            }
        }
    }
}
