package com.westepper.step.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.uilib.mxgallery.listeners.GalleryTabListener;
import com.uilib.mxgallery.widgets.GalleryTabGroup;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.adapters.MyDiscoveryRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.responses.Discovery;
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
    @BindView(R.id.rcv_mydis)
    RecyclerView rcv_mydis;

    int disKind;
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
            tab.setTabNames(new GalleryTabListener() {
                @Override
                public void onTabChecked(RadioButton tab, int id) {

                }

                @Override
                public void onTabUpdated(GalleryTabGroup galleryTab, int tabCount, int itemCount) {

                }
            }, "我发布的", "我报名的");
            tab.checkTab(0);
        }

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
        adapter.setDiscoveryList(createMyDiscoverys());
    }

    private List<Discovery> createMyDiscoverys(){
        List<Discovery> dislist = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Discovery discovery = new Discovery();
            discovery.setDiscoveryKind(disKind);
            discovery.setInfo("机场已成我家，一周天天回家。好心人给我送口外卖吧。鸡排侠又饿了。");
            discovery.setUserPos(new UserPos(null, "上海市，上海电视台"));
            long stemp = 1507786461l*1000l - (long)(i*100000000l);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(stemp));
            discovery.setPushTime(stemp);
            List<String> imgs = new ArrayList<>();
            imgs.add("http://pic1.win4000.com/pic/c/09/8b8f408807.jpg");
            imgs.add("http://tse2.mm.bing.net/th?id=OIP.yIi9TwH7EiIlIbsgvwTdHwFNC7&pid=15.1");
            imgs.add("http://photocdn.sohu.com/20130909/Img386224746.jpg");
            imgs.add("http://pic4.nipic.com/20090826/1412106_020349894777_2.jpg");
            discovery.setImgList(imgs);
            if(disKind == Constants.OUTGO){
                Random random = new Random(System.currentTimeMillis() * i);
                discovery.setJoinCount(random.nextInt(5));
                discovery.setTotalCount(discovery.getJoinCount() + random.nextInt(10));
            }
            dislist.add(discovery);
        }
        return dislist;
    }
}
