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

import com.google.gson.Gson;
import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.westepper.step.R;
import com.westepper.step.activities.GalleryActivity;
import com.westepper.step.activities.MyAchieveActivity;
import com.westepper.step.activities.MyDiscoveryActivity;
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
    @BindView(R.id.iv_header)
    SelectableRoundedImageView iv_header;
    @BindView(R.id.iv_header_bg)
    ImageView iv_header_bg;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.btn_setting)
    ImageButton btn_setting;
    @BindView(R.id.btn_info)
    ImageButton btn_info;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.tv_signature)
    TextView tv_signature;
    @BindView(R.id.iv_gender)
    ImageView iv_gender;
    @BindViews({R.id.iv_first, R.id.iv_second, R.id.iv_third})
    SelectableRoundedImageView[] rankHeads;
    @BindView(R.id.menu_paihang)
    MyMenuItem menu_paihang;
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
        RelativeLayout.LayoutParams lp;
        lp = (RelativeLayout.LayoutParams) iv_header_bg.getLayoutParams();
        lp.height = DisplayUtil.getScreenWidth(getActivity()) *9 / 16;
        iv_header_bg.setLayoutParams(lp);

        iv_header_bg.setOnClickListener(this);
        iv_header.setOnClickListener(this);
        btn_setting.setOnClickListener(this);

        menu_paihang.setOnClickListener(this);
        menu_paihang.setSubText("NO.");
        menu_mood.setOnClickListener(this);
        menu_go.setOnClickListener(this);
        menu_discovery.setOnClickListener(this);
        menu_acheive.setOnClickListener(this);

        userInfo = SuperActivity.userInfo;
    }

    private void setUserInfo(){
        if(userInfo == null || tv_user_name == null)
            return;
        tv_user_name.setText(userInfo.getNickName());
        tv_userId.setText("ID: ".concat(userInfo.getUserId()));
//        if(TextUtils.isEmpty(userInfo.getSign())){
//            tv_signature.setText("");
//        }else
            tv_signature.setText(userInfo.getSign());
        GlideImageLoader.getInstance().loadImage(getActivity(), userInfo.getHeadImg(), R.mipmap.ic_default_head, iv_header, 100);
        if(!TextUtils.isEmpty(userInfo.getCover()) && !userInfo.getCover().contains("data:image/jpeg;base64,")){
            iv_header_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideImageLoader.getInstance().loadImage(getActivity(), userInfo.getCover(), R.mipmap.ic_addcover, iv_header_bg, 0);
        }
        tv_city.setText(userInfo.getCity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible)
            setUserInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisible = getUserVisibleHint()){
            setUserInfo();
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
            iv_header_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideImageLoader.getInstance().loadImage(getActivity(), filePath, R.mipmap.ic_addcover, iv_header_bg, 0);
            updateCover(filePath);
        }else if(type == Constants.GET_RANK){
            rankList = (RankList) data.getSerializableExtra(Constants.RANKLIST1);
            menu_paihang.setSubText("NO." + rankList.getUserRank());
            for(int i = 0; i < ((rankList.getRankList().size() < 3) ? rankList.getRankList().size() : 3); i++){
                GlideImageLoader.getInstance().loadImage(getActivity(), rankList.getRankList().get(i).getHeadUrl(), R.mipmap.ic_default_head, rankHeads[i], 0);
            }
        }
    }

    private void updateCover(String filePath){
        userInfo.setCover(filePath);
        userInfo.getBase64Img(UserInfo.COVER, iv_header_bg.getWidth(), iv_header_bg.getHeight());
        UpdateUserInfoLogic logic = new UpdateUserInfoLogic(getActivity(), userInfo);
        logic.sendRequest();
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> args;
        switch (v.getId()) {
            case R.id.iv_header_bg:
                args = new HashMap<>();
                args.put(Constants.GALLERY_TYPE, Constants.CHANGE_USER_BG);
                ActivityManager.startActivityforResult(getActivity(), GalleryActivity.class, Constants.CHANGE_USER_BG, args);
                break;
            case R.id.iv_header:
                args = new HashMap<>();
                args.put(Constants.USERINFO, userInfo);
                ActivityManager.startActivityforResult(getActivity(), UserInfoActivity.class, Constants.CHANGE_HEADER, args);
                break;
            case R.id.btn_setting:
                ActivityManager.startActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.menu_paihang:
                args = new HashMap<>();
                args.put(Constants.RANKLIST1, rankList);
                ActivityManager.startActivity(getActivity(), PaihangActivity.class, args);
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
//            case R.id.tv_user_name:
//                ((BaseActivity) getActivity()).startToActivity(UserRegisterActivity.class);
//                break;
//            case R.id.menu_mood:
//                if (((BaseActivity) getActivity()).isUserLogin(true, BaseListActivity.class.getName()))
//                    ((BaseActivity) getActivity()).startToActivity(BaseListActivity.class, BaseListFragment.class.getSimpleName(), OrderListFragment.class.getName());
//                break;
//            case R.id.menu_advice:
//                ((BaseActivity) getActivity()).startToActivity(AdviceActivity.class);
//                break;
//            case R.id.menu_contact_us:
//                ((MainActivity)getActivity()).setEmailMaskVisible(true);
//                break;
//            case R.id.menu_setting:
//                ((BaseActivity) getActivity()).startToActivity(SettingActivity.class, "fragmentName", SettingFragment.class.getName());
//                break;
        }
    }

}
