package com.westepper.step.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.boxes.apple.AppleReferenceMovieDescriptorBox;
import com.google.gson.Gson;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.settings.model.NoDisturbConfig;
import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.customViews.ToggleBox;
import com.westepper.step.logics.SetPrivacyLogic;
import com.westepper.step.models.Privacy;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.utils.MXPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/11.
 */

public class SettingActivity extends SuperActivity implements View.OnClickListener {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.ckb_newNotify)
    ToggleBox ckb_newNotify;
    @BindView(R.id.menu_privacy)
    MyMenuItem menu_privacy;
    @BindView(R.id.menu_advice)
    MyMenuItem menu_advice;
    @BindView(R.id.menu_about)
    MyMenuItem menu_about;
    @BindView(R.id.menu_clear)
    MyMenuItem menu_clear;
    @BindView(R.id.tv_logout)
    TextView tv_logout;
    @BindView(R.id.layout_about)
    LinearLayout layout_about;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.menu_grade)
    MyMenuItem menu_grade;
    @BindView(R.id.menu_service)
    MyMenuItem menu_service;
    @BindView(R.id.menu_contact)
    MyMenuItem menu_contact;
    @BindView(R.id.layout_privacy)
    LinearLayout layout_privacy;
    @BindView(R.id.ckb_needVerif)
    ToggleBox ckb_needVerif;
    @BindView(R.id.rdg_moodScope)
    RadioGroup rdg_moodScope;
    @BindView(R.id.rdg_outgoScope)
    RadioGroup rdg_outgoScope;
    @BindView(R.id.layout_service)
    RelativeLayout layout_service;
    @BindView(R.id.tv_service)
    TextView tv_service;
    @BindView(R.id.btn_close)
    ImageButton btn_close;

    //UserInfo userInfo;
    Privacy privacy;
    Gson gson  = new Gson();
    int[] moodScope = new int[]{R.id.rdb_moodScopeSelf, R.id.rdb_moodScopeAll, R.id.rdb_moodScopeFriend};
    int[] outgoScope = new int[]{R.id.rdb_outgoScopeAll, R.id.rdb_outgoScopeFriend};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.PRIVACY, privacy);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        privacy = userInfo.getPrivacy_info();
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                if (layout_about.getVisibility() == View.VISIBLE) {
                    AnimUtils.startAlphaAnim(layout_about, 1, 0, 300);
                    titleBar.setTitle("设置");
                } else if(layout_privacy.getVisibility() == View.VISIBLE){
                    AnimUtils.startAlphaAnim(layout_privacy, 1, 0, 300);
                    titleBar.setTitle("设置");
                    setPrivacyLogic();
                }else
                    back();
            }
        });

        ckb_newNotify.setChecked(NIMClient.getService(MixPushService.class).isEnable());
        ckb_newNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                NIMClient.getService(MixPushService.class).enable(isChecked).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        NIMClient.toggleNotification(isChecked);
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == ResponseCode.RES_UNSUPPORT) {
                            NIMClient.toggleNotification(isChecked);
                        } else if (code == ResponseCode.RES_EFREQUENTLY) {
                            Toast.makeText(SettingActivity.this, "操作太频繁", Toast.LENGTH_SHORT).show();
                            buttonView.setChecked(!isChecked);
                        } else {
                            Toast.makeText(SettingActivity.this, "设置失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            buttonView.setChecked(!isChecked);
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        buttonView.setChecked(!isChecked);
                    }
                });
            }
        });

        Log.e(TAG, "isenable: "+NIMClient.getService(MixPushService.class).isEnable());

        ckb_needVerif.setChecked(privacy.getNeedFriendVerifi() == 1);
        ckb_needVerif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                privacy.setNeedFriendVerifi(isChecked ? 1 : 0);
            }
        });
        rdg_moodScope.check(moodScope[privacy.getMoodScope()]);
        rdg_moodScope.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i = 0; i < 3; i++){
                    if(moodScope[i] == checkedId){
                        privacy.setMoodScope(i);
                        break;
                    }
                }
            }
        });
        rdg_outgoScope.check(outgoScope[privacy.getOutgoScope() - 1]);
        rdg_outgoScope.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == outgoScope[0]){
                    privacy.setOutgoScope(Privacy.ALL);
                }else{
                    privacy.setOutgoScope(Privacy.FRIEND);
                }
            }
        });

        tv_service.setText(Html.fromHtml(getString(R.string.service_doc)));

        menu_privacy.setOnClickListener(this);
        menu_advice.setOnClickListener(this);
        menu_about.setOnClickListener(this);
        menu_clear.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        menu_grade.setOnClickListener(this);
        menu_service.setOnClickListener(this);
        menu_contact.setOnClickListener(this);
        btn_close.setOnClickListener(this);
    }

    private void setPrivacyLogic(){
        SetPrivacyLogic logic = new SetPrivacyLogic(SettingActivity.this, new Privacy(privacy));
        logic.setCallback(new BaseLogic.LogicCallback() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailed(String code, String msg, Object localData) {
                if("0".equals(code)){
                    userInfo.setPrivacy_info(privacy);
                    MXPreferenceUtils.getInstance().setString(userInfo.getUserId(), gson.toJson(userInfo));
                }
            }
        });
        logic.sendRequest();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Map<String, Object> args;
        switch (v.getId()) {
            case R.id.menu_privacy:
                AnimUtils.startAlphaAnim(layout_privacy, 0, 1, 300);
                titleBar.setTitle("隐私");
                break;
            case R.id.menu_advice:
                args = new HashMap<>();
                args.put(Constants.ISREPORT, ReportAdviceActivity.FEEDBACK);
                ActivityManager.startActivity(this, ReportAdviceActivity.class, args);
                break;
            case R.id.menu_about:
                AnimUtils.startAlphaAnim(layout_about, 0, 1, 300);
                titleBar.setTitle("关于Step");
                try {
                    String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    tv_version.setText("Step v" + version);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.menu_clear:
                break;
            case R.id.tv_logout:
                NIMClient.getService(AuthService.class).logout();
                ActivityManager.startActivity(this, WelcomeActivity.class);
                back();
                break;
            case R.id.menu_contact:
                Spanned phone = Html.fromHtml(getString(R.string.contact_phone)),
                        email = Html.fromHtml(getString(R.string.email));
                new CustomDialog(this).setLayoutRes(R.layout.layout_newdis_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
                    @Override
                    public void onBtnClick(int id) {
                        if (id == R.id.btn_mood) {
                            callPhone();
                        }else{
                            sendEmail();
                        }
                    }
                }, R.id.btn_mood, R.id.btn_outgo).setCustomBtnText(phone, email).show();
                break;
            case R.id.menu_service:
                AnimUtils.startAlphaAnim(layout_service, 0, 1, 300);
                break;
            case R.id.btn_close:
                AnimUtils.startAlphaAnim(layout_service, 1, 0, 300);
                break;
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:12345678"));
        ActivityCompat.startActivity(this, intent, null);
    }

    private void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:westepper@163.com"));
        startActivity(intent);
    }
}
