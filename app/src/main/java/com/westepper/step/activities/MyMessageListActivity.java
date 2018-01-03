package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.uilib.swipetoloadlayout.OnLoadMoreListener;
import com.uilib.swipetoloadlayout.OnRefreshListener;
import com.uilib.swipetoloadlayout.SwipeToLoadLayout;
import com.westepper.step.R;
import com.westepper.step.adapters.MyMessageAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetMyMessageListLogic;
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

    private MyMessageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messagelist);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }
        });
        swipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyMessageList();
            }
        });

        swipeLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        swipe_target.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        swipe_target.setAdapter(adapter = new MyMessageAdapter());
    }

    @Override
    protected void initData() {
        swipeLayout.setRefreshing(true);
        getMyMessageList();
    }

    private void getMyMessageList(){
        GetMyMessageListLogic logic = new GetMyMessageListLogic(this, new BaseModel());
        logic.setCallback(new BaseLogic.LogicCallback<MyMsgList>() {
            @Override
            public void onSuccess(MyMsgList response) {
                swipeLayout.setRefreshing(false);
                swipeLayout.setLoadingMore(false);
                adapter.setMsgList(response.getMessageList());
            }

            @Override
            public void onFailed(String code, String msg, MyMsgList localData) {
                swipeLayout.setRefreshing(false);
                swipeLayout.setLoadingMore(false);
            }
        });
        logic.sendRequest();
    }
}
