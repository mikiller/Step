package com.mkframe.step.base;

import android.app.Application;

import com.tendcloud.tenddata.TCAgent;


/**
 * Created by Mikiller on 2017/6/6.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TCAgent.LOG_ON = true;
        TCAgent.init(this, "BC0AE4111D1940A2A85AD9A423E011EE", "test");
        TCAgent.setReportUncaughtExceptions(true);
    }


}
