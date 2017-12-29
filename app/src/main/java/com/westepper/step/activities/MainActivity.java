package com.westepper.step.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.netease.nim.uikit.contact_selector.activity.ContactSelectActivity;
import com.westepper.step.R;
import com.westepper.step.adapters.MainFragmentAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.UntouchableViewPager;
import com.westepper.step.logics.GetUserInfoLogic;
import com.westepper.step.logics.RankListLogic;
import com.westepper.step.models.RankModel;
import com.westepper.step.responses.MapData;
import com.westepper.step.responses.RankList;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.FileUtils;
import com.westepper.step.utils.MapUtils;
import com.westepper.step.utils.ContactsHelper;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/1.
 */

public class MainActivity extends SuperActivity {
    @BindView(R.id.vp_content)
    UntouchableViewPager vp_content;
    @BindView(R.id.rdg_guideBar)
    RadioGroup rdg_guideBar;

    MainFragmentAdapter adapter;
    boolean canQuit = false;
//    public static MapData mapData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        rdg_guideBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdb_msg:
                        vp_content.setCurrentItem(0);
                        break;
                    case R.id.rdb_mine:
                        vp_content.setCurrentItem(2);
                        getRankList();
                        break;
                    default:
                        vp_content.setCurrentItem(1);
                        adapter.getItem(1).fragmentCallback(checkedId, null);
                        break;
                }
            }
        });
        vp_content.setTouchable(false);
        vp_content.setOffscreenPageLimit(1);
        vp_content.setAdapter(adapter = new MainFragmentAdapter(getSupportFragmentManager()));
        vp_content.setCurrentItem(1);

        getUserInfo();

    }

    private void getUserInfo() {
        GetUserInfoLogic logic = new GetUserInfoLogic(this, new BaseModel());
        logic.setCallback(new BaseLogic.LogicCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                SuperActivity.userInfo = response;
                adapter.getItem(2).fragmentCallback(Constants.CHANGE_HEADER, new Intent().putExtra(Constants.USERINFO, response));
            }

            @Override
            public void onFailed(String code, String msg, UserInfo localData) {
                Toast.makeText(MainActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
            }
        });
        logic.sendRequest();
    }

    private void getRankList() {
        RankListLogic logic = new RankListLogic(this, new RankModel(Constants.RANK_FRIEND));
        logic.setCallback(new BaseLogic.LogicCallback<RankList>() {
            @Override
            public void onSuccess(RankList response) {
                Intent intent = new Intent();
                intent.putExtra(Constants.RANKLIST1, response);
                adapter.getItem(2).fragmentCallback(Constants.GET_RANK, intent);
            }

            @Override
            public void onFailed(String code, String msg, RankList localData) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        logic.sendRequest();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case Constants.CHANGE_HEADER:
            case Constants.CHANGE_USER_BG:
                adapter.getItem(2).fragmentCallback(requestCode, data);
                break;
            case Constants.SHOW_ACHIEVE_AREA:
                rdg_guideBar.check(R.id.rdb_track);
                adapter.getItem(1).fragmentCallback(requestCode, data);
                break;
            case Constants.REQUEST_CODE_ADVANCED:
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                ContactsHelper.createAdvancedTeam(MainActivity.this, userInfo.getNickName().concat("的群聊"), selected, null);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if(!canQuit){
            Toast.makeText(this, "再按一次返回键退出",Toast.LENGTH_SHORT).show();
            canQuit = true;
            rdg_guideBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    canQuit = false;
                }
            }, 1000);
            return;
        }else {
            MapUtils.getInstance().clearMapData();
            super.onBackPressed();
        }
    }
}
