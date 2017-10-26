package com.westepper.step.logics;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netlib.mkokhttp.OkHttpManager;
import com.westepper.step.activities.MainActivity;
import com.westepper.step.activities.RegisterActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.BaseResponse;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.models.SignModel;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MXPreferenceUtils;

/**
 * Created by Mikiller on 2017/10/18.
 */

public class LoginLogic extends BaseLogic<SignModel> {
    public LoginLogic(Context context, BaseModel model) {
        super(context, model);
    }

    @Override
    protected void setResponseType() {
        responseType = new TypeToken<BaseResponse<SignModel>>(){}.getType();
    }

    @Override
    protected void setUrl() {
        url = "user/login";
    }

    @Override
    public void sendRequest() {
        super.sendRequest(OkHttpManager.RequestType.JSONPOST);
    }

    @Override
    public boolean isNeedDlg() {
        return true;
    }

    @Override
    public void onSuccess(SignModel response) {
        loginIM(response);
    }

    @Override
    public void onFailed(String code, String msg, SignModel localData) {
    }

    private void loginIM(SignModel response){
        MXPreferenceUtils.getInstance(context, MXPreferenceUtils.REPORTS).setString("account", response.getUserId());
        MXPreferenceUtils.getInstance().setString("token", response.getToken());
        LoginInfo loginInfo = new LoginInfo(response.getUserId(), response.getToken()/*"800036", "139ed2be4464ff498382c97a3e1b4d75"*/);
//        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback<LoginInfo>() {
        NimUIKit.doLogin(loginInfo, new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                Log.e(TAG, param.getAccount());
                ActivityManager.startActivity((Activity) context, MainActivity.class);
                ((SuperActivity)context).back();
            }

            @Override
            public void onFailed(int code) {
                Log.e(TAG, "code: " + code);
            }

            @Override
            public void onException(Throwable exception) {
                exception.printStackTrace();
            }
        });
    }
}
