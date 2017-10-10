package com.westepper.step.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.MyMenuItem;
import com.westepper.step.customViews.TitleBar;
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
    @BindView(R.id.iv_header)
    SelectableRoundedImageView iv_header;
    @BindView(R.id.menu_nickname)
    MyMenuItem menu_nickname;
    @BindView(R.id.menu_id)
    MyMenuItem menu_id;
    @BindView(R.id.menu_gender)
    MyMenuItem menu_gender;
    @BindView(R.id.menu_city)
    MyMenuItem menu_city;
    @BindView(R.id.edt_sign)
    EditText edt_sign;

    UserInfo userInfo;

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
                back();
            }
        });
        GlideImageLoader.getInstance().loadImage(this, userInfo.getHeadImg(), R.mipmap.ic_default_head, iv_header, 0);
        iv_header.setOnClickListener(this);
        menu_nickname.setSubText(userInfo.getNickName());
        menu_gender.setSubText(userInfo.getGender() == 1 ? "男" : "女");
        menu_city.setSubText(userInfo.getCity());
        menu_id.setSubText(userInfo.getUuid());
        edt_sign.setText(userInfo.getSign());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_header:
                Map<String, Object> args = new HashMap<>();
                args.put(Constants.GALLERY_TYPE, Constants.CHANGE_HEADER);
                ActivityManager.startActivityforResult(this, GalleryActivity.class, Constants.CHANGE_HEADER, args);
                break;
        }
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
                GlideImageLoader.getInstance().loadImage(this, filePath, R.mipmap.ic_default_head, iv_header, 0);
                break;
        }
    }
}
