package com.westepper.step.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.westepper.step.R;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.MyApplication;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.LoginLogic;
import com.westepper.step.logics.SaveCidLogic;
import com.westepper.step.models.CidModel;
import com.westepper.step.models.SignModel;
import com.westepper.step.utils.ActivityManager;

import java.util.Map;

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
                //login 微信
                MyApplication.shareAPI.getPlatformInfo(WelcomeActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, final Map<String, String> map) {
                        final SignModel model = new SignModel(map.get("uid"));
                        LoginLogic logic = new LoginLogic(WelcomeActivity.this, model);
                        logic.setCallback(new BaseLogic.LogicCallback<SignModel>() {
                            @Override
                            public void onSuccess(final SignModel response) {
                                sendCid();
                            }

                            @Override
                            public void onFailed(String code, String msg, SignModel localData) {
                                if("1".equals(code)){
                                    ActivityManager.startActivity(WelcomeActivity.this, RegisterActivity.class, map);
                                    back();
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

    private void sendCid(){
        CidModel model = new CidModel();
        model.setcId(MyApplication.cId);
        SaveCidLogic logic = new SaveCidLogic(this, model);
        logic.sendRequest();
    }

    @Override
    protected void initData() {
    }
}
