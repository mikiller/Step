package com.westepper.step.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.responses.ReachedList;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MapUtils;
import com.westepper.step.utils.PermissionUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mikiller on 2017/3/13.
 */

public abstract class SuperActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    Unbinder unbinder;
    protected int statusBarColor = Color.BLUE;
    public static UserInfo userInfo = new UserInfo();
    protected OnGetGeoFenceReceiver geoReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            setStatusBarColor();
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarColor(){
        getWindow().setStatusBarColor(statusBarColor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PermissionUtils.isNeedCheck){
            PermissionUtils.checkPermissions(this);
        }
        initData();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    abstract protected void initView();
    abstract protected void initData();

    @Override
    protected void onResume() {
        if(geoReceiver == null)
            geoReceiver = new OnGetGeoFenceReceiver();
        IntentFilter intentFilter = new IntentFilter(getString(R.string.geo_receiver));
        registerReceiver(geoReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(geoReceiver);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void showInputMethod(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void hideInputMethod(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                back();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void back(){
        hideInputMethod(getWindow().getDecorView());
        this.finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    public class OnGetGeoFenceReceiver extends BroadcastReceiver{
        ReachedList reachedList;
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null)
                reachedList = (ReachedList) intent.getSerializableExtra(Constants.REACHED_LIST);
            showCongraDlg("点亮L1区域", R.mipmap.ic_dis_l1);

            if(!TextUtils.isEmpty(reachedList.getReachedL2Id())){
                showCongraDlg(reachedList.getReachedL2Id(), R.mipmap.ic_dis_l2);
            }
            if(!TextUtils.isEmpty(reachedList.getReachedL3Id())){
                showCongraDlg(reachedList.getReachedL3Id(), R.mipmap.ic_dis_l3);
            }
            if(reachedList.getReachedAchievementIds() != null && reachedList.getReachedAchievementIds().size() > 0){
                for(String id : reachedList.getReachedAchievementIds()) {
                    AchieveArea ach = MapUtils.getInstance().getAchievement(id);
                    if(ach != null){
                        showCongraDlg("达成"+ach.getAchieveAreaName()+"成就", ach.getImgId());
                    }
                }
            }
        }

        private void showCongraDlg(String txt, int imgId){
            final CustomDialog dlg = new CustomDialog(SuperActivity.this).setLayoutRes(R.layout.layout_congratulation);
            dlg.setCancelable(true);

            TextView tv = (TextView) dlg.getCustomView().findViewById(R.id.tv_con_txt);
            tv.setText(txt);
            ImageView iv = (ImageView) dlg.getCustomView().findViewById(R.id.iv_con_icon);
            iv.setImageResource(imgId);
            dlg.getCustomView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                }
            });
            dlg.show();
        }
    }
}
