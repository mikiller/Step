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
import android.view.inputmethod.CompletionInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.Marker;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.adapters.DetailImgVpAdapter;
import com.westepper.step.adapters.DisDetailRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.responses.Commit;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.ImgDetail;
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
    @BindView(R.id.tv_imgNum)
    TextView tv_imgNum;
    @BindView(R.id.rcv_detail)
    RecyclerView rcv_detail;
    @BindView(R.id.commit_title)
    RelativeLayout commit_title;
    @BindView(R.id.margin)
    View margin;

    DetailImgVpAdapter vpAdapter;

    DisDetailRcvAdapter adapter;
    LinearLayoutManager rcvMgr;

    Discovery discovery;

    private float ActDownX, ActDownY, rcvDY, imgDy, titleDA;
    float maxRcvTransY, maxImgTransY;
    float rcvTransY, rlImgTransY;
    boolean isAnimRunning = false;
    //    int rcvFirstPos;
    float titleAlpha = 0.01f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            discovery = savedInstanceState.getParcelable(Constants.DISCOVERY_DETAIL);
        else if(getIntent() != null)
            discovery = getIntent().getParcelableExtra(Constants.DISCOVERY_DETAIL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_detail);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.DISCOVERY_DETAIL, discovery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setBgAlpha(titleAlpha);

        //for test

        vpAdapter = new DetailImgVpAdapter(this, createImgs());
        vp_img.setAdapter(vpAdapter);
        vp_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int minHeight = DisplayUtil.getScreenWidth(DiscoveryDetailActivity.this) / 4 * 3;
            int maxHeight = DisplayUtil.getScreenHeight(DiscoveryDetailActivity.this) / 3 * 4;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ImgDetail img = vpAdapter.getImgs().get(position);
                int fromHeight = getHeight(img.getHeight());
                int nextHeight = fromHeight;
                if(position < vpAdapter.getCount() - 1) {
                    ImgDetail img1 = vpAdapter.getImgs().get(position + 1);
                    nextHeight = getHeight(img1.getHeight());
                }
                Log.e(TAG, "fromHeight: " + fromHeight + ", nextHeigh: " + nextHeight);
                int height = (int) (fromHeight * (1 -positionOffset) + nextHeight * positionOffset);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) vp_img.getLayoutParams();
                lp.height = height;
                vp_img.setLayoutParams(lp);
            }

            private int getHeight(int src){
               return src < minHeight ? minHeight : (src > maxHeight ? maxHeight : src);
            }

            @Override
            public void onPageSelected(int position) {
                setImgNum(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setImgNum(0);

        adapter = new DisDetailRcvAdapter(this);
        rcvMgr = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_detail.setLayoutManager(rcvMgr);
        rcv_detail.setAdapter(adapter);
        rcv_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.e(TAG, "current pos: " + rcvMgr.findFirstCompletelyVisibleItemPosition());
                commit_title.setVisibility(rcvMgr.findFirstCompletelyVisibleItemPosition() >= 2 ? View.VISIBLE : View.GONE);
                commit_title.setTranslationY(rcv_detail.getTranslationY() + titleBar.getHeight());
                margin.setVisibility(View.GONE);
                ((TextView)commit_title.findViewById(R.id.tv_commitNum)).setText(String.valueOf(adapter.getItemCount()));
            }
        });
    }

    private List<ImgDetail> createImgs(){
        List<ImgDetail> imgs = new ArrayList<>();
        ImgDetail img1 = new ImgDetail("http://gj.yuanlin.com/UploadFiles/201404/2014413143943688.jpg", 240, 750);
        imgs.add(img1);
        ImgDetail img2 = new ImgDetail("http://images2015.cnblogs.com/blog/652828/201509/652828-20150901135305419-1201699534.jpg", 1280, 960);
        imgs.add(img2);
        return imgs;
    }

    private void setImgNum(int position){
        String imgCount = String.format("%1$d / %2$d", position + 1, vpAdapter.getCount());
        tv_imgNum.setText(imgCount);
    }

    @Override
    protected void initData() {
        List<Commit> commits = new ArrayList<>();
        Commit commit = new Commit();
        commit.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.q53c9FWOXGvw00Xr-a162wD6D6&w=198&h=198&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit.setCommit("哈哈哈哈哈或");
        commit.setNickName("donghua");
        commits.add(commit);
        Commit commit1 = new Commit();
        commit1.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.kwrzMA37JwCYw9jlfN1QEgEsEa&w=229&h=204&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit1.setCommit("巴巴爸爸不不不不不不");
        commit1.setNickName("按到");
        commits.add(commit1);
        Commit commit2 = new Commit();
        commit2.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.scCB3oW9MAoZj84PRRrAAgEsEs&pid=15.1");
        commit2.setCommit("飞机撒了几分几分");
        commit2.setNickName("大打发斯蒂芬");
        commits.add(commit2);
        Commit commit3 = new Commit();
        commit3.setHeaderUrl("http://img1.3lian.com/img2012/12/49/d/54.jpg");
        commit3.setCommit("范德萨附近咖啡了");
        commit3.setNickName("大耳环");
        commits.add(commit3);
        Commit commit4 = new Commit();
        commit4.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.q53c9FWOXGvw00Xr-a162wD6D6&w=198&h=198&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit4.setCommit("哈哈哈哈哈或");
        commit4.setNickName("donghua");
        commits.add(commit4);
        Commit commit11 = new Commit();
        commit11.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.kwrzMA37JwCYw9jlfN1QEgEsEa&w=229&h=204&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit11.setCommit("巴巴爸爸不不不不不不");
        commit11.setNickName("按到");
        commits.add(commit11);
        Commit commit21 = new Commit();
        commit21.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.scCB3oW9MAoZj84PRRrAAgEsEs&pid=15.1");
        commit21.setCommit("飞机撒了几分几分");
        commit21.setNickName("大打发斯蒂芬");
        commits.add(commit21);
        Commit commit31 = new Commit();
        commit31.setHeaderUrl("http://img1.3lian.com/img2012/12/49/d/54.jpg");
        commit31.setCommit("范德萨附近咖啡了");
        commit31.setNickName("大耳环");
        commits.add(commit31);
        Commit commit22 = new Commit();
        commit22.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.q53c9FWOXGvw00Xr-a162wD6D6&w=198&h=198&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit22.setCommit("哈哈哈哈哈或");
        commit22.setNickName("donghua");
        commits.add(commit22);
        Commit commit13 = new Commit();
        commit13.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.kwrzMA37JwCYw9jlfN1QEgEsEa&w=229&h=204&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit13.setCommit("巴巴爸爸不不不不不不");
        commit13.setNickName("按到");
        commits.add(commit13);
        Commit commit23 = new Commit();
        commit23.setHeaderUrl("http://tse2.mm.bing.net/th?id=OIP.scCB3oW9MAoZj84PRRrAAgEsEs&pid=15.1");
        commit23.setCommit("飞机撒了几分几分");
        commit23.setNickName("大打发斯蒂芬");
        commits.add(commit23);
        Commit commit33 = new Commit();
        commit33.setHeaderUrl("http://img1.3lian.com/img2012/12/49/d/54.jpg");
        commit33.setCommit("范德萨附近咖啡了");
        commit33.setNickName("大耳环");
        commits.add(commit33);


        adapter.setCommits(commits);
        adapter.setDiscovery(discovery);
        vp_img.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                maxRcvTransY = vp_img.getMeasuredHeight() - titleBar.getHeight();
                maxImgTransY = (titleBar.getHeight() - rl_img.getHeight()) * 0.4f;
                rcv_detail.setTranslationY(vp_img.getHeight() - titleBar.getHeight());
                commit_title.setTranslationY(rcv_detail.getTranslationY() + titleBar.getHeight());
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
                titleAlpha = titleBar.getBgAlpha();
                ActDownX = ev.getX();
                ActDownY = isAnimRunning ? -1 : ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getY()) > 150) {
                    rcvDY = ev.getY() - ActDownY;
                    imgDy = rcvDY * 0.4f;
                }
                if ((ActDownY < rcvTransY + titleBar.getHeight())) {
                    if (Math.abs(ActDownX - ev.getX()) > 150) {
                        ActDownY = -1;
                        rcvDY = imgDy = 0;
                    }
                } else if (canMove()) {
                    rl_img.setTranslationY(getImgDeltaY(rlImgTransY, imgDy));
                    rcv_detail.setTranslationY(getDeltaY(rcvTransY, rcvDY));
                    titleBar.setBgAlpha(getTitleAlpha());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (canMove()) {
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
        if (isAnimRunning || rcvMgr.findFirstCompletelyVisibleItemPosition() != 0 || ActDownY < 0) {
            rcvDY = 0;
            rst = false;
        } else if (isMaxY() || isMinY()) {
            rcvDY = 0;
            rst = false;
        }
        return rst;
    }

    private float getTitleAlpha() {
        titleDA = 1 - getDeltaY(rcvTransY, rcvDY) / maxRcvTransY + 0.01f;
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
                    if (view instanceof RecyclerView)
                        ((RecyclerView) view).scrollToPosition(0);
                }
            }
        });
    }

    private void startAlpha(float fromA, float toA) {
        AnimUtils.startObjectAnim(titleBar.getTitleBg(), "alpha", fromA, toA, 300);
    }

    private float getDeltaY(float srcY, float dY) {
        return (srcY + dY) >= maxRcvTransY ? maxRcvTransY : ((srcY + dY) <= 0 ? 0 : (srcY + dY));
    }

    private float getImgDeltaY(float srcY, float dY) {
        return (srcY + dY) >= 0 ? 0 : ((srcY + dY) <= maxImgTransY ? maxImgTransY : (srcY + dY));
    }


}
