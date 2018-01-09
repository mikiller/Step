package com.westepper.step.widgets.getui;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.westepper.step.activities.MainActivity;
import com.westepper.step.base.MyApplication;

/**
 * Created by Mikiller on 2018/1/3.
 */

public class GetuiIntentService extends GTIntentService {
    private Callback callback;

    public GetuiIntentService() {
    }

    public class Binder extends android.os.Binder{
        public GetuiIntentService getService() {
            return GetuiIntentService.this;
        }
    }

    public interface Callback{
        void onNotify();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String payload =  new String(msg.getPayload());
        if ("2".equals(payload)){
            sendNotify();
        }
    }

    private void sendNotify(){
        if (callback != null)
            callback.onNotify();
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        MyApplication.cId = clientid;
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}
