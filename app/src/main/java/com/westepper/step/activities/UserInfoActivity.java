package com.westepper.step.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.customViews.TitleBar;
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
    TextView tv_sign;
    @BindView(R.id.tv_signNum)
    TextView tv_signNum;
    @BindView(R.id.rl_commitInput)
    RelativeLayout rl_commitInput;
    @BindView(R.id.edt_commit)
    EditText edt_commit;
    @BindView(R.id.btn_send)
    Button btn_send;

    int signNum = 140;
    UserInfo userInfo;
    CommitGlobalLayoutListener glListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            userInfo = (UserInfo) savedInstanceState.getSerializable(Constants.USERINFO);
        else if(getIntent() != null)
            userInfo = (UserInfo) getIntent().getSerializableExtra(Constants.USERINFO);
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
                Intent data = new Intent();
                data.putExtra(Constants.USERINFO, userInfo);
                setResult(RESULT_OK, data);
                back();
            }
        });
        GlideImageLoader.getInstance().loadImage(this, userInfo.getHeadImg(), R.mipmap.ic_default_head, iv_userHeader, 0);
        menu_nickname.setSubText(userInfo.getNickName());
        menu_gender.setSubText(userInfo.getGender() == 1 ? "男" : "女");
        menu_city.setSubText(userInfo.getCity());
        menu_id.setSubText(userInfo.getUuid());
        tv_sign.setText(userInfo.getSign());
        tv_signNum.setText(String.valueOf(signNum - tv_sign.getText().length()));

        iv_userHeader.setOnClickListener(this);
        menu_nickname.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(glListener = new CommitGlobalLayoutListener(this, rl_commitInput, edt_commit, btn_send));
        glListener.setOnSendListener(new CommitGlobalLayoutListener.OnSendListener() {
            @Override
            public void onSend(View focuseView, String txt) {
                edt_commit.clearFocus();
                switch (focuseView.getId()){
                    case R.id.menu_nickname:
                        if(TextUtils.isEmpty(txt)){
                            Toast.makeText(UserInfoActivity.this, "昵称不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        userInfo.setNickName(txt);
                        menu_nickname.setSubText(txt);
                        break;
                    case R.id.tv_sign:
                        tv_sign.setText(txt);
                        tv_signNum.setText(String.valueOf(signNum - tv_sign.getText().length()));
                        userInfo.setSign(tv_sign.getText().toString());
                        break;
                }
                hideInputMethod();
                titleBar.setSubTxtEnabled(true);
            }
        });
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
                glListener.setFocuceView(v);
                glListener.setHint("输入新昵称");
                showInputMethod();
                break;
            case R.id.tv_sign:
                glListener.setHint("写下你的个性签名");
                glListener.setFocuceView(v);
                showInputMethod();
                break;
        }
    }

    private void showInputMethod(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void hideInputMethod(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_commit.getWindowToken(), 0);
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
                titleBar.setSubTxtEnabled(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(glListener);
        } else {
            getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(glListener);
        }
        super.onDestroy();
    }
}
