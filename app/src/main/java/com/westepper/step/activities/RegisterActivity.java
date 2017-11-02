package com.westepper.step.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.adapters.CityAdapter;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.CityListLayout;
import com.westepper.step.logics.RegistLogic;
import com.westepper.step.models.CityBean;
import com.westepper.step.models.SignModel;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/8/31.
 */

public class RegisterActivity extends SuperActivity implements View.OnClickListener {
    @BindView(R.id.iv_header)
    SelectableRoundedImageView iv_header;
    @BindView(R.id.edt_nickName)
    EditText edt_nickName;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.rdg_gender)
    RadioGroup rdg_gender;
    @BindView(R.id.btn_signup)
    ImageButton btn_signup;
    @BindView(R.id.ckb_protocol)
    CheckBox ckb_protocol;
    @BindView(R.id.ll_regist)
    LinearLayout ll_regist;
    @BindView(R.id.layout_citylist)
    CityListLayout layout_citylist;
    @BindView(R.id.btn_close)
    ImageButton btn_close;

    String headUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        tv_city.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        headUrl = getIntent().getStringExtra("iconurl");
        edt_nickName.setText(getIntent().getStringExtra("name"));
        GlideImageLoader.getInstance().loadImage(this, headUrl, R.mipmap.ic_default_head, iv_header, 0);
        if("男".equals(getIntent().getStringExtra("gender"))){
            rdg_gender.check(R.id.rdb_male);
        }else if("女".equals(getIntent().getStringExtra("gender")))
            rdg_gender.check(R.id.rdb_female);

        btn_signup.setOnClickListener(this);
        ckb_protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_signup.setEnabled(isChecked);
            }
        });

        layout_citylist.setOnSelectedCityListener(new CityListLayout.onSelectedCityListener() {
            @Override
            public void onCitySelected(String city) {
                setCity(city);
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void setCity(String city){
        hideCityList();
        tv_city.setText(city);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_city:
                showCityList();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.btn_close:
                hideCityList();
                break;
            case R.id.btn_signup:
                if(checkArgs())
                    //loginIM();
                    startSignUpLogic();
                break;
        }
    }

    private void showCityList(){
        AnimUtils.startAlphaAnim(ll_regist, 1.0f, 0.0f, 300);
        AnimUtils.startAlphaAnim(layout_citylist, 0.0f, 1.0f, 200);
    }

    private void hideCityList(){
        AnimUtils.startAlphaAnim(ll_regist, 0.0f, 1.0f, 200);
        AnimUtils.startAlphaAnim(layout_citylist, 1.0f, 0.0f, 300);
    }

    private boolean checkArgs(){
        boolean rst = true;
        if(TextUtils.isEmpty(edt_nickName.getText().toString())) {
            Toast.makeText(this, "请填写昵称！", Toast.LENGTH_SHORT).show();
            rst = false;
        }
        else if(tv_city.getText().toString().equals("请选择城市")) {
            Toast.makeText(this, "请选择城市！", Toast.LENGTH_SHORT).show();
            rst = false;
        }else if(rdg_gender.getCheckedRadioButtonId() == View.NO_ID) {
            Toast.makeText(this, "请选择性别！", Toast.LENGTH_SHORT).show();
            rst = false;
        }
        return rst;
    }

    private void startSignUpLogic(){
        SignModel model = new SignModel(edt_nickName.getText().toString(),
                rdg_gender.getCheckedRadioButtonId() == R.id.rdb_male ? 1 : 2,
                tv_city.getText().toString());
        model.setHeadImg(headUrl);
        model.setUuid(getIntent().getStringExtra("uid"));
        RegistLogic logic = new RegistLogic(this, model);
        logic.sendRequest();
    }
}
