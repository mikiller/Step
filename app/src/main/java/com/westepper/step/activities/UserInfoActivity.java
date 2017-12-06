package com.westepper.step.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.westepper.step.R;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.customViews.CityListLayout;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.UpdateUserInfoLogic;
import com.westepper.step.models.SignModel;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.widgets.CommitGlobalLayoutListener;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/10.
 */

public class UserInfoActivity extends SuperActivity implements View.OnClickListener{
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.iv_userHeader)
    SelectableRoundedImageView iv_userHeader;
    @BindView(R.id.menu_nickname)
    MyMenuItem menu_nickname;
    @BindView(R.id.menu_id)
    MyMenuItem menu_id;
    @BindView(R.id.menu_gender)
    MyMenuItem menu_gender;
    @BindView(R.id.menu_city)
    MyMenuItem menu_city;
    @BindView(R.id.tv_sign)
    EditText tv_sign;
    @BindView(R.id.tv_signNum)
    TextView tv_signNum;
    @BindView(R.id.commitInput)
    CommitEditView commitInput;
    @BindView(R.id.ll_info)
    LinearLayout ll_info;
    @BindView(R.id.layout_citylist)
    CityListLayout layout_citylist;

    int signNum = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            userInfo = (UserInfo) savedInstanceState.getSerializable(Constants.USERINFO);
//        else if(getIntent() != null)
//            userInfo = (UserInfo) getIntent().getSerializableExtra(Constants.USERINFO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.USERINFO, userInfo);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onSubClicked() {
                updateUserInfo();
            }
        });
        GlideImageLoader.getInstance().loadImage(this, userInfo.getHeadImg(), R.mipmap.ic_default_head, iv_userHeader, 0);
        menu_nickname.setSubText(userInfo.getNickName());
        menu_gender.setSubText(userInfo.getGender() == 1 ? "男" : "女");
        menu_city.setSubText(userInfo.getCity());
        menu_id.setSubText(userInfo.getUserId());
        tv_sign.setText(userInfo.getSign());
        tv_signNum.setText(String.valueOf(signNum - tv_sign.getText().length()));

        iv_userHeader.setOnClickListener(this);
        menu_nickname.setOnClickListener(this);
        menu_city.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        tv_sign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_signNum.setText(String.valueOf(signNum - tv_sign.getText().length()));
                userInfo.setSign(tv_sign.getText().toString());
                titleBar.setSubTxtEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commitInput.setOnSendListener(new CommitEditView.OnSendListener() {
            @Override
            public void onSend(View focuseView, String txt) {
                switch (focuseView.getId()){
                    case R.id.menu_nickname:
                        if(TextUtils.isEmpty(txt)){
                            Toast.makeText(UserInfoActivity.this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        txt = txt.trim().replace("\n", "");
                        userInfo.setNickName(txt);
                        menu_nickname.setSubText(txt);
                        break;
//                    case R.id.tv_sign:
//                        tv_sign.setText(txt);
//                        tv_signNum.setText(String.valueOf(signNum - tv_sign.getText().length()));
//                        userInfo.setSign(tv_sign.getText().toString());
//                        break;
                }
                hideInputMethod(commitInput);
                titleBar.setSubTxtEnabled(true);
            }
        });

        layout_citylist.setOnSelectedCityListener(new CityListLayout.onSelectedCityListener() {
            @Override
            public void onCitySelected(String city) {
                hideCityList();
                userInfo.setCity(city);
                menu_city.setSubText(city);
                titleBar.setSubTxtEnabled(true);
            }
        });
    }

    private void updateUserInfo(){
        UpdateUserInfoLogic logic = new UpdateUserInfoLogic(this, userInfo);
        logic.setCallback(new BaseLogic.LogicCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                userInfo.setHeadImg(response.getHeadImg());
                Intent intent = new Intent();
                intent.putExtra(Constants.USERINFO, userInfo);
                setResult(Activity.RESULT_OK, intent);
                back();
            }

            @Override
            public void onFailed(String code, String msg, UserInfo localData) {
                Toast.makeText(UserInfoActivity.this, "更新用户信息失败，请稍后重试", Toast.LENGTH_SHORT).show();
                back();
            }
        });
        logic.sendRequest();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userHeader:
                Map<String, Object> args = new HashMap<>();
                args.put(Constants.GALLERY_TYPE, Constants.CHANGE_HEADER);
                ActivityManager.startActivityforResult(this, GalleryActivity.class, Constants.CHANGE_HEADER, args);
                break;
            case R.id.menu_nickname:
                commitInput.setFocuceView(v);
                commitInput.setHint("输入新昵称");
                showInputMethod(commitInput);
                break;
            case R.id.menu_city:
                showCityList();
                break;
            case R.id.tv_sign:
//                commitInput.setHint("写下你的个性签名");
//                commitInput.setFocuceView(v);
//                showInputMethod(commitInput);
                commitInput.setNeedShow(false);
                break;
        }
    }

    private void showCityList(){
        AnimUtils.startAlphaAnim(ll_info, 1.0f, 0.0f, 300);
        AnimUtils.startAlphaAnim(layout_citylist, 0.0f, 1.0f, 200);
    }

    private void hideCityList(){
        AnimUtils.startAlphaAnim(ll_info, 0.0f, 1.0f, 200);
        AnimUtils.startAlphaAnim(layout_citylist, 1.0f, 0.0f, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        switch (requestCode){
            case Constants.CHANGE_HEADER:
                String filePath = "";
                if(data.getSerializableExtra(CameraGalleryUtils.TMP_FILE) != null)
                    filePath = (String) data.getSerializableExtra(CameraGalleryUtils.TMP_FILE);
                else if( data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE) != null) {
                    List<File> files = (List<File>) data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE);
                    filePath = files.get(0).getPath();
                }
                userInfo.setHeadImg(filePath);
                GlideImageLoader.getInstance().loadImage(this, filePath, R.mipmap.ic_default_head, iv_userHeader, 0);
                userInfo.getBase64Img(UserInfo.HEADIMG, 240, 240);
                titleBar.setSubTxtEnabled(true);
                break;
        }
    }
}
