package com.westepper.step.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.customViews.ToggleBox;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                if (layout_about.getVisibility() == View.VISIBLE) {
                    AnimUtils.startAlphaAnim(layout_about, 1, 0, 300);
                    titleBar.setTitle("设置");
                } else if(layout_privacy.getVisibility() == View.VISIBLE){
                    AnimUtils.startAlphaAnim(layout_privacy, 1, 0, 300);
                    titleBar.setTitle("设置");
                }else
                    back();
            }
        });

        menu_privacy.setOnClickListener(this);
        menu_advice.setOnClickListener(this);
        menu_about.setOnClickListener(this);
        menu_clear.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        menu_grade.setOnClickListener(this);
        menu_service.setOnClickListener(this);
        menu_contact.setOnClickListener(this);
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
