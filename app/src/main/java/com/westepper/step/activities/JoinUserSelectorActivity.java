package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.westepper.step.R;
import com.westepper.step.adapters.JoinUserAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetJoinUsersLogic;
import com.westepper.step.models.JoinModel;
import com.westepper.step.responses.JoinUsers;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ContactsHelper;
import com.westepper.step.utils.MXTimeUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/11/27.
 */

public class JoinUserSelectorActivity extends SuperActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rcv_users)
    RecyclerView rcv_users;

    String disId;
    String teamId;
    JoinUserAdapter adapter;
    List<TeamMember> teamMembers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            disId = savedInstanceState.getString(Constants.DIS_ID);
            teamId = savedInstanceState.getString(Constants.TEAM_ID);
            teamMembers = (List<TeamMember>) savedInstanceState.getSerializable(Constants.TEAM_MEMBER);
        } else if (getIntent() != null) {
            disId = getIntent().getStringExtra(Constants.DIS_ID);
            teamId = getIntent().getStringExtra(Constants.TEAM_ID);
            teamMembers = (List<TeamMember>) getIntent().getSerializableExtra(Constants.TEAM_MEMBER);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinuser_selector);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DIS_ID, disId);
        outState.putString(Constants.TEAM_ID, teamId);
        outState.putSerializable(Constants.TEAM_MEMBER, (Serializable) teamMembers);
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
                if (TextUtils.isEmpty(teamId)){
                    ContactsHelper.createAdvancedTeam(JoinUserSelectorActivity.this,  userInfo.getNickName().concat("的约行").concat(MXTimeUtils.getFormatTime("yy/MM/dd HH:mm", System.currentTimeMillis())), adapter.getAccounts(), new JoinModel(disId));
                }else
                    ContactsHelper.inviteMembers(JoinUserSelectorActivity.this, teamId, adapter.getAccounts());
            }
        });
        titleBar.setSubTxtEnabled(true);

        rcv_users.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initData() {
//        getTeamMembers();
        getJoinUserLogic();
    }

//    private void getTeamMembers(){
//        TeamDataCache.getInstance().fetchTeamMemberList(teamId, new SimpleCallback<List<TeamMember>>() {
//            @Override
//            public void onResult(boolean success, List<TeamMember> members) {
//                if (success && members != null) {
//                    titleBar.setSubTxtEnabled(success);
//                    getJoinUserLogic(members);
//                }
//            }
//        });
//    }

    private void getJoinUserLogic() {
        GetJoinUsersLogic logic = new GetJoinUsersLogic(this, new JoinModel(disId));
        logic.setCallback(new BaseLogic.LogicCallback<JoinUsers>() {
            @Override
            public void onSuccess(JoinUsers response) {
                if (teamMembers != null) {
                    for (TeamMember member : teamMembers) {
                        for (UserInfo user : response.getJoinUser()) {
                            if (user.getUserId().equals(userInfo.getUserId())) {
                                response.getJoinUser().remove(user);
                                break;
                            }
                            if (member.getAccount().equals(user.getUserId())) {
                                response.getJoinUser().remove(user);
                                break;
                            }
                        }
                    }
                }
                adapter = new JoinUserAdapter(JoinUserSelectorActivity.this, response.getJoinUser());
                rcv_users.setAdapter(adapter);
            }

            @Override
            public void onFailed(String code, String msg, JoinUsers localData) {
            }
        });
        logic.sendRequest();
    }


}

