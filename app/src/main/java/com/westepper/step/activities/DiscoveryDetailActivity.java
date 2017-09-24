package com.westepper.step.activities;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.westepper.step.R;
import com.westepper.step.adapters.DetailImgVpAdapter;
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

    DetailImgVpAdapter vpAdapter;

    DisDetailRcvAdapter adapter;
    LinearLayoutManager rcvMgr;

    private float ActDownX, ActDownY, rcvDY, imgDy, titleDA;
    float maxRcvTransY, maxImgTransY;
    float rcvTransY, rlImgTransY;
    boolean isAnimRunning = false;
    int rcvFirstPos;
    float titleAlpha = 0.01f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_detail);
    }

    @Override
    protected void initView() {
        titleBar.setBgAlpha(titleAlpha);
        vpAdapter = new DetailImgVpAdapter(this, new String[]{"http://images2015.cnblogs.com/blog/652828/201509/652828-20150901135305419-1201699534.jpg"});
        vp_img.setAdapter(vpAdapter);

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
                maxRcvTransY = vp_img.getMeasuredHeight() - titleBar.getHeight();
                maxImgTransY = (titleBar.getHeight() - rl_img.getHeight()) * 0.4f;
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
                rlImgTransY = rl_img.getTranslationY();
                titleAlpha = titleBar.getAlpha();
                ActDownX = ev.getX();
                ActDownY = isAnimRunning ? -1 : ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - ActDownX) > 200) {
                    break;
                }
                rcvDY = ev.getY() - ActDownY;
                imgDy = rcvDY * 0.4f;
                if (canMove()) {
                    rl_img.setTranslationY(getImgDeltaY(rlImgTransY, imgDy));
                    rcv_detail.setTranslationY(getDeltaY(rcvTransY, rcvDY));
                    titleBar.setBgAlpha(getTitleAlpha());
                    Log.e(TAG, "titlealpha: " + titleAlpha);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isAnimRunning && rcvFirstPos == 0 && ActDownY > 0) {
                    if (rcvDY < -150) {
                        startAlpha(titleDA, 1f);
                        startScroll(rl_img, rl_img.getTranslationY(), maxImgTransY);
                        startScroll(rcv_detail, rcv_detail.getTranslationY(), 0f);
                    } else if (rcvDY > 150) {
                        startAlpha(titleDA, 0.01f);
                        startScroll(rl_img, rl_img.getTranslationY(), 0);
                        startScroll(rcv_detail, rcv_detail.getTranslationY(), maxRcvTransY);
                    } else {
                        startAlpha(titleDA, titleAlpha);
                        startScroll(rl_img, rlImgTransY + imgDy, rlImgTransY);
                        startScroll(rcv_detail, rcvTransY + rcvDY, rcvTransY);
                    }

                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isMaxY() {
        return rcvDY >= 0 && (rcvTransY >= maxRcvTransY);
    }

    private boolean isMinY() {
        return rcvDY <= 0 && (rcvTransY <= 0);
    }

    private boolean canMove() {
        boolean rst = true;
        if (isAnimRunning || rcvFirstPos != 0 || ActDownY < 0) {
            rcvDY = 0;
            rst = false;
        } else if (isMaxY() || isMinY()) {
            rst = false;
        }
        return rst;
    }

    private float getTitleAlpha(){
        titleDA = getDeltaY(rcvTransY, rcvDY);
        titleDA /= maxRcvTransY;
        titleDA = 1 - titleDA + 0.01f;
        Log.e(TAG, "titleda: " + titleDA);
        return titleDA;
    }

    private void startScroll(final View view, float fromY, final float toY) {
        AnimUtils.startObjectAnim(view, "translationY", fromY, toY, 300, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (((float) animation.getAnimatedValue()) == toY) {
                    isAnimRunning = false;
                    animation.cancel();
                } else {
                    isAnimRunning = true;
                    if(view instanceof RecyclerView)
                        ((RecyclerView)view).scrollToPosition(0);
                }
            }
        });
    }

    private void startAlpha(float fromA, float toA){
        AnimUtils.startObjectAnim(titleBar.getRlBg(), "alpha", fromA, toA, 300);
    }

    private float getDeltaY(float srcY, float dY) {
        return (srcY + dY) >= maxRcvTransY ? maxRcvTransY : ((srcY + dY) <= 0 ? 0 : (srcY + dY));
    }

    private float getImgDeltaY(float srcY, float dY){
        return (srcY + dY) >= 0 ? 0 : ((srcY + dY) <= maxImgTransY ? maxImgTransY : (srcY + dY));
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
