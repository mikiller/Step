package com.westepper.step.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.common.ui.widget.UserLayout;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.activities.MyAchieveActivity;
import com.westepper.step.activities.MyDiscoveryActivity;
import com.westepper.step.activities.MyMessageListActivity;
import com.westepper.step.activities.PaihangActivity;
import com.westepper.step.activities.SettingActivity;
import com.westepper.step.activities.UserInfoActivity;
import com.westepper.step.base.BaseFragment;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.uilib.mxmenuitem.MyMenuItem;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.UserInfoLayout;
import com.westepper.step.logics.UpdateUserInfoLogic;
import com.westepper.step.responses.RankList;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.westepper.step.utils.MXPreferenceUtils;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    @BindView(R.id.userLayout)
    UserInfoLayout userLayout;
    @BindViews({R.id.iv_first, R.id.iv_second, R.id.iv_third})
    SelectableRoundedImageView[] rankHeads;
    @BindView(R.id.menu_paihang)
    MyMenuItem menu_paihang;
    @BindView(R.id.menu_msg)
    MyMenuItem menu_msg;
    @BindView(R.id.menu_mood)
    MyMenuItem menu_mood;
    @BindView(R.id.menu_go)
    MyMenuItem menu_go;
    @BindView(R.id.menu_discovery)
    MyMenuItem menu_discovery;
    @BindView(R.id.menu_acheive)
    MyMenuItem menu_acheive;

    UserInfo userInfo;
    RankList rankList = new RankList();
    boolean isVisible = false;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    protected void setLayoutRes() {
        layoutRes = R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        menu_paihang.setOnClickListener(this);
        menu_paihang.setSubText("NO.");
        menu_msg.setOnClickListener(this);
        menu_mood.setOnClickListener(this);
        menu_go.setOnClickListener(this);
        menu_discovery.setOnClickListener(this);
        menu_acheive.setOnClickListener(this);

        userInfo = SuperActivity.userInfo;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible)
            userLayout.setUserInfo(userInfo);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if((isVisible = getUserVisibleHint()) && userLayout != null){
            userLayout.setUserInfo(userInfo);
            menu_msg.setNeedNotify(MXPreferenceUtils.getInstance().getBoolean(Constants.HAS_NOTIFY));
        }
    }

    @Override
    public void fragmentCallback(int type, Intent data) {
        if(type == Constants.CHANGE_HEADER){
            userInfo = (UserInfo) data.getSerializableExtra(Constants.USERINFO);
        }else if(type == Constants.CHANGE_USER_BG){
            String filePath = "";
            if(data.getSerializableExtra(CameraGalleryUtils.TMP_FILE) != null)
                filePath = (String) data.getSerializableExtra(CameraGalleryUtils.TMP_FILE);
            else if( data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE) != null) {
                List<File> files = (List<File>) data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE);
                filePath = files.get(0).getPath();
            }
            userLayout.setCover(filePath);
            updateCover(filePath);
        }else if(type == Constants.GET_RANK){
            rankList = (RankList) data.getSerializableExtra(Constants.RANKLIST1);
            menu_paihang.setSubText("NO." + rankList.getUserRank());
            for(int i = 0; i < ((rankList.getRankList().size() < 3) ? rankList.getRankList().size() : 3); i++){
                GlideImageLoader.getInstance().loadImage(getActivity(), rankList.getRankList().get(i).getHeadUrl(), R.mipmap.ic_default_head, rankHeads[i], 0);
            }
        }else if (type == Constants.GET_NOTIFY){
            menu_msg.setNeedNotify(true);
        }
    }

    private void updateCover(String filePath){
        userInfo.setCover(filePath);
        userInfo.getBase64Img(UserInfo.COVER, userLayout.getBgWidth(), userLayout.getBgHeight());
        UpdateUserInfoLogic logic = new UpdateUserInfoLogic(getActivity(), userInfo);
        logic.setCallback(new BaseLogic.LogicCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                SuperActivity.userInfo.setCover(response.getCover());
            }

            @Override
            public void onFailed(String code, String msg, UserInfo localData) {
                Toast.makeText(getActivity(), "上传背景图失败", Toast.LENGTH_SHORT).show();
            }
        });
        logic.sendRequest();
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> args;
        switch (v.getId()) {
            case R.id.menu_paihang:
                args = new HashMap<>();
                args.put(Constants.RANKLIST1, rankList);
                ActivityManager.startActivity(getActivity(), PaihangActivity.class, args);
                break;
            case R.id.menu_msg:
                ActivityManager.startActivity(getActivity(), MyMessageListActivity.class);
                menu_msg.setNeedNotify(false);
                MXPreferenceUtils.getInstance().setBoolean(Constants.HAS_NOTIFY, false);
                break;
            case R.id.menu_mood:
                args = new HashMap<>();
                args.put(Constants.DIS_KIND, Constants.MOOD);
                ActivityManager.startActivity(getActivity(), MyDiscoveryActivity.class, args);
                break;
            case R.id.menu_go:
                args = new HashMap<>();
                args.put(Constants.DIS_KIND, Constants.OUTGO);
                ActivityManager.startActivity(getActivity(), MyDiscoveryActivity.class, args);
                break;
            case R.id.menu_discovery:
                args = new HashMap<>();
                args.put(Constants.ACH_KIND, Constants.ACH_CITY);
                ActivityManager.startActivity(getActivity(), MyAchieveActivity.class, args);
                break;
            case R.id.menu_acheive:
                args = new HashMap<>();
                args.put(Constants.ACH_KIND, Constants.ACH_BADGE);
                ActivityManager.startActivityforResult(getActivity(), MyAchieveActivity.class, Constants.SHOW_ACHIEVE_AREA, args);
                break;
        }
    }

}
