package com.westepper.step.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.wrapper.SdkWrapper;

import butterknife.BindView;


public class WelcomeActivity extends SuperActivity {

    @BindView(R.id.btn_weixinLogin)
    Button btn_weixinLogin;

    private SdkWrapper sdkWrapper;
    public static WXHandler handler;
    public static WelcomeActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sdkWrapper = SdkWrapper.getInstance();
        sdkWrapper.init(this, SdkWrapper.PLATFORM.WEIXIN);
        handler = new WXHandler();
        instance = this;
    }

    @Override
    protected void initView() {
        btn_weixinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkWrapper.getInstance().login(SdkWrapper.PLATFORM.WEIXIN);
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
                back();
            }
        }
    }
}
