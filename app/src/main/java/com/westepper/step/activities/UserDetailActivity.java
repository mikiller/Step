package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.uilib.customdialog.CustomDialog;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.R;
import com.westepper.step.adapters.UserDetailDisPgsAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.customViews.UserInfoLayout;
import com.westepper.step.logics.GetMyAchievementsLogic;
import com.westepper.step.logics.GetUserInfoLogic;
import com.westepper.step.models.Privacy;
import com.westepper.step.responses.MyCreditAndPercents;
import com.westepper.step.responses.UserInfo;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/11/23.
 */

public class UserDetailActivity extends SuperActivity implements View.OnClickListener {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.userLayout)
    UserInfoLayout userLayout;
    @BindView(R.id.menu_city)
    MyMenuItem menu_city;
    @BindView(R.id.menu_mood)
    MyMenuItem menu_mood;
    @BindView(R.id.menu_outGo)
    MyMenuItem menu_outGo;
    @BindView(R.id.menu_discovery)
    MyMenuItem menu_discovery;
    @BindView(R.id.menu_achieve)
    MyMenuItem menu_achieve;
    @BindView(R.id.rcv_dis)
    RecyclerView rcv_dis;
    @BindView(R.id.rcv_ach)
    RecyclerView rcv_ach;

    String account;
    UserDetailDisPgsAdapter cityAdapter;
    UserDetailDisPgsAdapter achAdapter;
    BaseModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            account = savedInstanceState.getString(Constants.USERINFO);
        else if(getIntent() != null)
            account = getIntent().getStringExtra(Constants.USERINFO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.USERINFO, account);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setBackImg(R.mipmap.ic_back2);
        titleBar.setSubImg(R.mipmap.ic_menu2);
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onMoreClicked() {
                showFriendOptDlg();
            }
        });

        menu_mood.setOnClickListener(this);
        menu_outGo.setOnClickListener(this);
//        menu_discovery.setOnClickListener(this);
//        menu_achieve.setOnClickListener(this);

        rcv_dis.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcv_dis.setAdapter(cityAdapter = new UserDetailDisPgsAdapter(Constants.ACH_CITY));
        rcv_ach.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcv_ach.setAdapter(achAdapter = new UserDetailDisPgsAdapter(Constants.ACH_BADGE));
    }

    private void showFriendOptDlg(){
        final CustomDialog dlg = new CustomDialog(this);
        dlg.setLayoutRes(R.layout.layout_newdis_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
            @Override
            public void onBtnClick(int id) {
                if(id == R.id.btn_mood){
                    //添加备注
                }else{
                    ConfirmDelFriend();
                }
                dlg.dismiss();
            }
        }, R.id.btn_mood, R.id.btn_outgo).setCustomBtnText("添加备注", "删除好友").show();
    }

    private void ConfirmDelFriend(){
        CustomDialog dlg = new CustomDialog(this);
        dlg.setTitle("删除好友").setMsg("确定要删除该好友吗？").setDlgButtonListener(new CustomDialog.onButtonClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure() {
                deleteFriend();
            }
        }).show();
    }

    private void deleteFriend(){
        NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Toast.makeText(UserDetailActivity.this, "删除好友成功", Toast.LENGTH_SHORT).show();
                back();
            }

            @Override
            public void onFailed(int code) {
                if (code == 408) {
                    Toast.makeText(UserDetailActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDetailActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
            }
        });
    }

    @Override
    protected void initData() {
        getUserInfo();
        getMyAchievementsLogic(Constants.ACH_CITY);
        getMyAchievementsLogic(Constants.ACH_BADGE);
    }

    private void getUserInfo(){
        model = new BaseModel();
        model.setUserId(account);
        GetUserInfoLogic logic = new GetUserInfoLogic(this, model);
        logic.setCallback(new BaseLogic.LogicCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if(TextUtils.isEmpty(response.getSign())){
                    response.setSign(" ");
                }
                userLayout.setUserInfo(response);
                boolean isFriend = userLayout.setBtnState(response.getUserId(), response.getPrivacy_info().getNeedFriendVerifi());
                menu_city.setSubText(response.getCity());
                menu_mood.setVisibility(isMoodPublic(response.getPrivacy_info().getMoodScope(), isFriend) ? View.VISIBLE : View.GONE);
                menu_outGo.setVisibility(isOutGoPublic(response.getPrivacy_info().getOutgoScope(), isFriend) ? View.VISIBLE : View.GONE);
                titleBar.setSubStyle(isFriend ? TitleBar.IMG : TitleBar.NONE);
            }

            @Override
            public void onFailed(String code, String msg, UserInfo localData) {
                Toast.makeText(UserDetailActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
            }
        });
        logic.sendRequest();
    }

    private boolean isMoodPublic(int moodScope, boolean isFriend){
        return moodScope == Privacy.ALL || (moodScope == Privacy.FRIEND && isFriend);
    }

    private boolean isOutGoPublic(int outGoScope, boolean isFriend){
        return outGoScope == Privacy.ALL || isFriend;
    }

    private void getMyAchievementsLogic(final int kind){
        GetMyAchievementsLogic logic = new GetMyAchievementsLogic(this, model);
        logic.setType(kind);
        logic.setCallback(new BaseLogic.LogicCallback<MyCreditAndPercents>() {
            @Override
            public void onSuccess(MyCreditAndPercents response) {
                if(kind == Constants.ACH_CITY){
                    menu_discovery.setSubText(String.valueOf(response.getCredit()));
                    cityAdapter.setCityData(response.getDiscoverCityList());
                }else{
                    menu_achieve.setSubText(String.valueOf(response.getCredit()));
                    achAdapter.setAchData(response.getPercentList());
                }
            }

            @Override
            public void onFailed(String code, String msg, MyCreditAndPercents localData) {
                Toast.makeText(UserDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        logic.sendRequest();
    }

    @Override
    public void onClick(View v) {

    }
}
