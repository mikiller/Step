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

    private float ActDownX, ActDownY, DY;
    float imgHeight;
    float rcvTransY;
    boolean isAnimRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_detail);
    }

    @Override
    protected void initView() {
        adapter = new DisDetailRcvAdapter();
        rcv_detail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv_detail.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        List<String> commits = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            commits.add(String.valueOf(i));
        }
        adapter.setCommits(commits);
        adapter.setName("Mike");
        vp_img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgHeight = vp_img.getMeasuredHeight() - titleBar.getHeight();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean rst = super.dispatchTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                ActDownX = ev.getX();
                ActDownY = ev.getY();
                rcvTransY = rcv_detail.getTranslationY();
                rst = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(ev.getX() - ActDownX) > 200) {
                    //Log.e(TAG, "move x: " + Math.abs(ev.getX() - ActDownX));
                    break;
                }
                DY = ev.getY() - ActDownY;
                if(DY > 0 && rcvTransY == imgHeight)
                    break;
                else if(DY < 0 && rcvTransY == 0)
                    break;
                rcv_detail.setTranslationY((rcvTransY + DY) >= imgHeight ? imgHeight : ((rcvTransY + DY) <= 0 ? 0 : (rcvTransY + DY)));
                Log.e(TAG, "dy: " + DY);
                rst = false;
                break;
            case MotionEvent.ACTION_UP:
                if(!isAnimRunning) {
                    if (DY < -200) {
                        AnimUtils.startObjectAnim(rcv_detail, "translationY", rcv_detail.getTranslationY(), 0f, 500, new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if(((float)animation.getAnimatedValue()) == 0){
                                    isAnimRunning = false;
                                    animation.cancel();
                                }
                            }
                        });
                    } else if(DY > 200) {
                        AnimUtils.startObjectAnim(rcv_detail, "translationY", rcv_detail.getTranslationY(), imgHeight, 500, new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (((float) animation.getAnimatedValue()) == imgHeight) {
                                    isAnimRunning = false;
                                    animation.cancel();
                                }
                            }
                        });
                    }
                    else{
                        AnimUtils.startObjectAnim(rcv_detail, "translationY", rcvTransY + DY, rcvTransY, 300, new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (((float) animation.getAnimatedValue()) == rcvTransY) {
                                    isAnimRunning = false;
                                    animation.cancel();
                                }
                            }
                        });
                    }
                }
                break;
        }
        return rst;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean rst = true;
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }


}
