package com.westepper.step.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.westepper.step.R;
import com.westepper.step.activities.RegisterActivity;
import com.westepper.step.activities.WelcomeActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.wrapper.SdkWrapper;
import com.westepper.step.wrapper.channelSdk.WeixinWrapper;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/9.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    WeixinWrapper wrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wrapper = (WeixinWrapper) SdkWrapper.getInstance().getWrapper(SdkWrapper.PLATFORM.WEIXIN);
        wrapper.handlerIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wrapper.handlerIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result, code;
        SendAuth.Resp resp = (SendAuth.Resp) baseResp;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                code = resp.code;
                result = "登录成功";
                ActivityManager.startActivity(WelcomeActivity.instance, RegisterActivity.class);
                WelcomeActivity.handler.sendEmptyMessage(Constants.WX_LOGIN);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "用户取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "认证失败";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "版本不支持";
                break;
            default:
                result = "未知错误";
                break;
        }
        if(!TextUtils.isEmpty(baseResp.errStr))
            result = result.concat(baseResp.errStr);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }

}
