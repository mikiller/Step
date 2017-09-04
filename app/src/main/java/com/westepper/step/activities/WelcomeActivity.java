package com.westepper.step.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.westepper.step.R;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.utils.ActivityManager;

import butterknife.BindView;


public class WelcomeActivity extends SuperActivity {

    @BindView(R.id.btn_weixinLogin)
    Button btn_weixinLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void initView() {
        btn_weixinLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(WelcomeActivity.this, RegisterActivity.class);
                back();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
