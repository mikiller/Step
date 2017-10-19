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
    LinearLayout layout_citylist;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.rdg_mainCity)
    RadioGroup rdg_mainCity;
    @BindView(R.id.rcv_city)
    RecyclerView rcv_city;
    @BindView(R.id.indexBar)
    IndexBar indexBar;

    List<CityBean> mDatas;
    CityAdapter adapter;
    LinearLayoutManager llMgr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        tv_city.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        edt_nickName.setText(getIntent().getStringExtra("name"));
        GlideImageLoader.getInstance().loadImage(this, getIntent().getStringExtra("iconurl"), R.mipmap.ic_default_head, iv_header, 0);
        if(getIntent().getStringExtra("gender").equals("男")){
            rdg_gender.check(R.id.rdb_male);
        }else
            rdg_gender.check(R.id.rdb_female);
        rdg_mainCity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdb_Shanghai:
                        setCity("上海");
                        break;
                    case R.id.rdb_Beijing:
                        setCity("北京");
                        break;
                    case R.id.rdb_Guangzhou:
                        setCity("广州");
                        break;
                    case R.id.rdb_Hangzhou:
                        setCity("杭州");
                        break;
                }
            }
        });
        btn_signup.setOnClickListener(this);
        ckb_protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_signup.setEnabled(isChecked);
            }
        });
        rcv_city.setLayoutManager(llMgr = new LinearLayoutManager(this));
    }

    private void setCity(String city){
        hideCityList();
        tv_city.setText(city);
    }

    @Override
    protected void initData() {
        getCitysFromFile();
    }

    private void getCitysFromFile(){
        try {
            InputStream is = getAssets().open("city.txt");
            byte[] txt = new byte[is.available()];
            is.read(txt);
            String cityjson = new String(txt);
            mDatas = new Gson().fromJson(cityjson, new TypeToken<List<CityBean>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexBar.setmLayoutManager(llMgr).setNeedRealIndex(true).setmSourceDatas(mDatas).invalidate();
        String lastTag = "";
        for(CityBean city : mDatas){
            if(!city.getBaseIndexTag().equals(lastTag)){
                city.setTop(true);
                lastTag = city.getBaseIndexTag();
            }
        }
        adapter = new CityAdapter(this, mDatas);
        adapter.setListener(new CityAdapter.CityClickListener() {
            @Override
            public void onCityClick(String city) {
                hideCityList();
                tv_city.setText(city);
            }
        });
        rcv_city.setAdapter(adapter);
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

//    private void loginIM(){
//        LoginInfo loginInfo = new LoginInfo("or4h41h0quiyc0il8-pwawbjyq4g", "20a699b323fc2b56e5c0f3b260cf0895");
//        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
//            @Override
//            public void onSuccess(LoginInfo param) {
//                Log.e(TAG, param.getAccount());
//                ActivityManager.startActivity(RegisterActivity.this, MainActivity.class);
//                finish();
//            }
//
//            @Override
//            public void onFailed(int code) {
//                Log.e(TAG, "code: " + code);
//            }
//
//            @Override
//            public void onException(Throwable exception) {
//                exception.printStackTrace();
//            }
//        };
//        NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
//    }


    private void startSignUpLogic(){
        SignModel model = new SignModel(edt_nickName.getText().toString(),
                rdg_gender.getCheckedRadioButtonId() == R.id.rdb_male ? 1 : 2,
                tv_city.getText().toString());
        model.setHeadImg(getIntent().getStringExtra("iconurl"));
        model.setUuid(getIntent().getStringExtra("uid"));
        Log.e(TAG, new Gson().toJson(model));
        ActivityManager.startActivity(this, MainActivity.class);
        back();
    }
}
