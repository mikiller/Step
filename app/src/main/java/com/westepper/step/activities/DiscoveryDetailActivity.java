package com.westepper.step.activities;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.westepper.step.R;
import com.westepper.step.adapters.DisDetailRcvAdapter;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.utils.AnimUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/21.
 */

public class DiscoveryDetailActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rl_img)
    RelativeLayout rl_img;
    @BindView(R.id.vp_img)
    ViewPager vp_img;
    @BindView(R.id.rcv_detail)
    RecyclerView rcv_detail;

    DisDetailRcvAdapter adapter;
    LinearLayoutManager rcvMgr;

    private float ActDownX, ActDownY, DY;
    float maxTransY;
    float rcvTransY;
    boolean isAnimRunning = false;
    int rcvFirstPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_detail);
    }

    @Override
    protected void initView() {
        adapter = new DisDetailRcvAdapter();
        rcvMgr = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_detail.setLayoutManager(rcvMgr);
        rcv_detail.setAdapter(adapter);
        rcv_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    rcvFirstPos = rcvMgr.findFirstVisibleItemPosition();
            }
        });
    }

    @Override
    protected void initData() {
        List<String> commits = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            commits.add(String.valueOf(i));
        }
        adapter.setCommits(commits);
        adapter.setName("Mike");
        vp_img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                maxTransY = vp_img.getMeasuredHeight() - titleBar.getHeight();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rcvTransY = rcv_detail.getTranslationY();
                ActDownX = ev.getX();
                ActDownY = isAnimRunning ? -1 : ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - ActDownX) > 200) {
                    break;
                }
                DY = ev.getY() - ActDownY;
                if (canMove()) {
                    rcv_detail.setTranslationY(getDeltaY(rcvTransY, DY));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isAnimRunning && rcvFirstPos == 0 && ActDownY > 0) {
                    if (DY < -150) {
                        startScrollAnim(rcv_detail.getTranslationY(), 0f);
                    } else if (DY > 150) {
                        startScrollAnim(rcv_detail.getTranslationY(), maxTransY);
                    } else {
                        startScrollAnim(rcvTransY + DY, rcvTransY);
                    }

                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isMaxY() {
        return DY >= 0 && (rcvTransY >= maxTransY);
    }

    private boolean isMinY() {
        return DY <= 0 && (rcvTransY <= 0);
    }

    private boolean canMove() {
        boolean rst = true;
        if (isAnimRunning || rcvFirstPos != 0 || ActDownY < 0) {
            DY = 0;
            rst = false;
        } else if (isMaxY() || isMinY()) {
            rst = false;
        }
        return rst;
    }

    private void startScrollAnim(float fromY, final float toY) {
        AnimUtils.startObjectAnim(rcv_detail, "translationY", fromY, toY, 300, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (((float) animation.getAnimatedValue()) == toY) {
                    isAnimRunning = false;
                    animation.cancel();
                } else {
                    isAnimRunning = true;
                    rcv_detail.scrollToPosition(0);
                }
            }
        });
    }

    private float getDeltaY(float srcY, float dY) {
        return (srcY + dY) >= maxTransY ? maxTransY : ((srcY + dY) <= 0 ? 0 : (srcY + dY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean rst = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }


}
