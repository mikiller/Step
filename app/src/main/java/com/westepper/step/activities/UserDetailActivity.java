package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.uilib.mxprogressbar.MXCircleProgressBar;
import com.westepper.step.R;
import com.westepper.step.base.SuperActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/11/23.
 */

public class UserDetailActivity extends SuperActivity {

    @BindView(R.id.pgs1)
    MXCircleProgressBar pgs1;
    @BindView(R.id.pgs2)
    MXCircleProgressBar pgs2;

    float i = 10.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void initView() {
        pgs1.setProgress(25);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                pgs2.post(new Runnable() {
                    @Override
                    public void run() {
                        pgs2.setProgress(i+=1.3);
                        if(i >= 100)
                            cancel();
                    }
                });
            }
        }, 0, 2000);
    }

    @Override
    protected void initData() {

    }
}
