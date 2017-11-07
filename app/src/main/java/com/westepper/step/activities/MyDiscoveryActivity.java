package com.westepper.step.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.uilib.mxgallery.listeners.GalleryTabListener;
import com.uilib.mxgallery.widgets.GalleryTabGroup;
import com.uilib.swipetoloadlayout.OnLoadMoreListener;
import com.uilib.swipetoloadlayout.OnRefreshListener;
import com.uilib.swipetoloadlayout.SwipeToLoadLayout;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.adapters.MyDiscoveryRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetMyDiscoveryLogic;
import com.westepper.step.models.DisModel;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.DiscoveryList;
import com.westepper.step.responses.ImgDetail;
import com.westepper.step.responses.UserPos;
import com.westepper.step.utils.ActivityManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/12.
 */

public class MyDiscoveryActivity extends SuperActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tab)
    GalleryTabGroup tab;
    @BindView(R.id.swipeLayout)
    SwipeToLoadLayout  swipeLayout;
//    @BindView(R.id.rcv_mydis)
    RecyclerView rcv_mydis;

    int disKind, page = 1, type = 1;
    MyDiscoveryRcvAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            disKind = savedInstanceState.getInt(Constants.DIS_KIND);
        }else if(getIntent() != null){
            disKind = getIntent().getIntExtra(Constants.DIS_KIND, Constants.MOOD);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydiscovery);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.DIS_KIND, disKind);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setTitle(disKind == Constants.MOOD ? "心情" : "约行");
        titleBar.setSubImg(R.mipmap.ic_new_camera);
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onMoreClicked() {
                Map<String, Object> args = new HashMap<String, Object>();
                args.put(Constants.DIS_KIND, disKind);
                ActivityManager.startActivity(MyDiscoveryActivity.this, GalleryActivity.class, args);
            }
        });
        if(disKind == Constants.OUTGO) {
            tab.setVisibility(View.VISIBLE);

            tab.setTabNames("我发布的", "我报名的");
            tab.checkTab(0);
            tab.setTabListener(new GalleryTabListener() {
                @Override
                public void onTabChecked(RadioButton tab, int id) {
                    type = id + 1;
                    getMyDiscovery(page = 1);
                    swipeLayout.setRefreshing(true);
                }

                @Override
                public void onTabUpdated(GalleryTabGroup galleryTab, int tabCount, int itemCount) {

                }
            });

        }

        swipeLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getMyDiscovery(++page);
            }
        });
        swipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyDiscovery(page = 1);
            }
        });
        rcv_mydis = (RecyclerView) swipeLayout.getTargetView();
        rcv_mydis.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyDiscoveryRcvAdapter(this, disKind);
        rcv_mydis.setAdapter(adapter);
        rcv_mydis.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = DisplayUtil.dip2px(MyDiscoveryActivity.this, 10);
            }
        });
    }

    @Override
    protected void initData() {
        getMyDiscovery(page);
        swipeLayout.setRefreshing(true);
    }

    private void getMyDiscovery(final int page) {
        GetMyDiscoveryLogic logic = new GetMyDiscoveryLogic(this, disKind == Constants.OUTGO ? new DisModel(disKind, page, type) : new DisModel(disKind, page));
        logic.setCallback(new BaseLogic.LogicCallback<DiscoveryList>() {
            @Override
            public void onSuccess(DiscoveryList response) {
                if(page == 1) {
                    adapter.setDiscoveryList(response.getDiscoveryList());
                    swipeLayout.setRefreshing(false);
                }
                else {
                    adapter.addDiscoveryList(response.getDiscoveryList());
                    swipeLayout.setLoadingMore(false);
                }
            }

            @Override
            public void onFailed(String code, String msg, DiscoveryList localData) {
                Log.e(TAG, code + ", " + msg);
                if(!"0".equals(code)){
                    Toast.makeText(MyDiscoveryActivity.this, msg, Toast.LENGTH_SHORT).show();
                }else if(swipeLayout.isRefreshing()){
                    swipeLayout.setRefreshing(false);
                    adapter.setDiscoveryList(new ArrayList<Discovery>());
                    Toast.makeText(MyDiscoveryActivity.this, "没有你的相关约行", Toast.LENGTH_SHORT).show();
                }else if(swipeLayout.isLoadingMore()){
                    swipeLayout.setLoadingMore(false);
                    Toast.makeText(MyDiscoveryActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        logic.sendRequest();
    }
}
