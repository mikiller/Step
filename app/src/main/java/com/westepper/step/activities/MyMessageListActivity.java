package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.uilib.swipetoloadlayout.OnLoadMoreListener;
import com.uilib.swipetoloadlayout.OnRefreshListener;
import com.uilib.swipetoloadlayout.SwipeToLoadLayout;
import com.westepper.step.R;
import com.westepper.step.adapters.GoodUserAdapter;
import com.westepper.step.adapters.MyMessageAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetGoodUserLogic;
import com.westepper.step.logics.GetMyMessageListLogic;
import com.westepper.step.models.DisBase;
import com.westepper.step.responses.GoodUserList;
import com.westepper.step.responses.MyMsgList;

import butterknife.BindView;

/**
 * Created by Mikiller on 2018/1/3.
 */

public class MyMessageListActivity extends SuperActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.swipeLayout)
    SwipeToLoadLayout swipeLayout;
    @BindView(R.id.swipe_target)
    RecyclerView swipe_target;

    private RecyclerView.Adapter adapter;
    private int isMyMessage, kind;
    private String disId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isMyMessage = savedInstanceState.getInt(Constants.IS_MESSAGE);
            kind = savedInstanceState.getInt(Constants.DIS_KIND);
            disId = savedInstanceState.getString(Constants.DIS_ID);
        } else if (getIntent() != null) {
            isMyMessage = getIntent().getIntExtra(Constants.IS_MESSAGE, 1);
            kind = getIntent().getIntExtra(Constants.DIS_KIND, Constants.MOOD);
            disId = getIntent().getStringExtra(Constants.DIS_ID);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messagelist);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.IS_MESSAGE, isMyMessage);
        outState.putInt(Constants.DIS_KIND, kind);
        outState.putString(Constants.DIS_ID, disId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setTitle(isMyMessage == 1 ? getString(R.string.menu_msg) : String.format(getString(R.string.good_user_title), kind == Constants.MOOD ? "心情" : "约行"));
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }
        });
        swipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isMyMessage == 1)
                    getMyMessageList();
                else
                    getGoodUserList();
            }
        });

        swipeLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        swipe_target.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = isMyMessage == 1 ? new MyMessageAdapter() : new GoodUserAdapter();
        swipe_target.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        swipeLayout.setRefreshing(true);
        if (isMyMessage == 1)
            getMyMessageList();
        else
            getGoodUserList();
    }

    private void getMyMessageList() {
        GetMyMessageListLogic logic = new GetMyMessageListLogic(this, new BaseModel());
        logic.setCallback(new BaseLogic.LogicCallback<MyMsgList>() {
            @Override
            public void onSuccess(MyMsgList response) {
                swipeLayout.setRefreshing(false);
                swipeLayout.setLoadingMore(false);
                ((MyMessageAdapter) adapter).setMsgList(response.getMessageList());
            }

            @Override
            public void onFailed(String code, String msg, MyMsgList localData) {
                swipeLayout.setRefreshing(false);
                swipeLayout.setLoadingMore(false);
            }
        });
        logic.sendRequest();
    }

    private void getGoodUserList() {
        GetGoodUserLogic logic = new GetGoodUserLogic(this, new DisBase(disId, kind));
        logic.setCallback(new BaseLogic.LogicCallback<GoodUserList>() {
            @Override
            public void onSuccess(GoodUserList response) {
                swipeLayout.setRefreshing(false);
                swipeLayout.setLoadingMore(false);
                ((GoodUserAdapter) adapter).setUsers(response.getUsers());
            }

            @Override
            public void onFailed(String code, String msg, GoodUserList localData) {
                swipeLayout.setRefreshing(false);
                swipeLayout.setLoadingMore(false);
            }
        });
        logic.sendRequest();
    }
}
