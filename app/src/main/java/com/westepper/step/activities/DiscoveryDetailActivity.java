package com.westepper.step.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.customdialog.CustomDialog;
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
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.ll_joinOpt)
    LinearLayout ll_joinOtp;
    @BindView(R.id.tv_joinOpt)
    TextView tv_joinOpt;
    @BindView(R.id.rl_commitInput)
    RelativeLayout rl_commitInput;
    @BindView(R.id.edt_commit)
    EditText edt_commit;

    DetailImgVpAdapter vpAdapter;

    DisDetailRcvAdapter rcvAdapter;
    MyLinearLayoutManager rcvMgr;

    Discovery discovery;
    int scope;

    ViewTreeObserver.OnGlobalLayoutListener glListener;

    private float ActDownX, ActDownY, rcvDY, imgDy, titleDA;
    float maxRcvTransY, maxImgTransY;
    float rcvTransY, rlImgTransY;
    boolean isAnimRunning = false, needTransY = true;
    //    int rcvFirstPos;
    float titleAlpha = 0.01f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            discovery = savedInstanceState.getParcelable(Constants.DISCOVERY_DETAIL);
            scope = savedInstanceState.getInt(Constants.DIS_SCOPE);
        } else if (getIntent() != null) {
            discovery = getIntent().getParcelableExtra(Constants.DISCOVERY_DETAIL);
            scope = getIntent().getIntExtra(Constants.DIS_SCOPE, Constants.NEARBY);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_detail);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.DISCOVERY_DETAIL, discovery);
        outState.putInt(Constants.DIS_SCOPE, scope);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setBgAlpha(titleAlpha);
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onMoreClicked() {
                final CustomDialog dlg = new CustomDialog(DiscoveryDetailActivity.this);
                dlg.setLayoutRes(R.layout.layout_newdis_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
                    @Override
                    public void onBtnClick(int id) {
                        Map<String, Object> args = new HashMap<String, Object>();
                        args.put(Constants.ISREPORT, id == R.id.btn_mood ? ReportAdviceActivity.REPORT : ReportAdviceActivity.ADVICE);
                        ActivityManager.startActivity(DiscoveryDetailActivity.this, ReportAdviceActivity.class, args);
                        dlg.dismiss();
                    }
                }, R.id.btn_mood, R.id.btn_outgo).setCustomBtnText("举报", "报错").show();
            }
        });
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
                if (position < vpAdapter.getCount() - 1) {
                    ImgDetail img1 = vpAdapter.getImgs().get(position + 1);
                    nextHeight = getHeight(img1.getHeight());
                }
                int height = (int) (fromHeight * (1 - positionOffset) + nextHeight * positionOffset);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) vp_img.getLayoutParams();
                lp.height = height;
                vp_img.setLayoutParams(lp);
            }

            private int getHeight(int src) {
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

        rcvMgr = new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_detail.setLayoutManager(rcvMgr);
        rcvAdapter = new DisDetailRcvAdapter(this);
        rcvAdapter.setCommitListener(new DisDetailRcvAdapter.OnCommitListener() {
            @Override
            public void onCommit(String id) {
                if(rl_commitInput.getVisibility() == View.GONE) {
                    needTransY = false;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        rcv_detail.setAdapter(rcvAdapter);

        ll_joinOtp.setVisibility(discovery.getDiscoveryKind() == Constants.MOOD ? View.GONE : View.VISIBLE);
        tv_joinOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
            }
        });

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(glListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int sbHeight, keyboardHeight, screenHeight = DisplayUtil.getScreenHeight(DiscoveryDetailActivity.this);
            Rect window = new Rect();

            @Override
            public void onGlobalLayout() {
                getWindow().getDecorView().getWindowVisibleDisplayFrame(window);
                keyboardHeight = screenHeight - window.height();
                if(keyboardHeight > screenHeight / 3){
                    rl_commitInput.setVisibility(View.VISIBLE);
                    edt_commit.requestFocus();
                    rl_commitInput.setTranslationY(sbHeight - keyboardHeight);
                }else{
                    sbHeight = keyboardHeight;
                    rl_commitInput.setVisibility(View.GONE);
                    edt_commit.setText("");
                    needTransY = true;
                }
            }
        });
    }

    private List<ImgDetail> createImgs() {
        List<ImgDetail> imgs = new ArrayList<>();
        ImgDetail img1 = new ImgDetail("http://gj.yuanlin.com/UploadFiles/201404/2014413143943688.jpg", 240, 750);
        imgs.add(img1);
        ImgDetail img2 = new ImgDetail("http://images2015.cnblogs.com/blog/652828/201509/652828-20150901135305419-1201699534.jpg", 1280, 960);
        imgs.add(img2);
        return imgs;
    }

    private void setImgNum(int position) {
        String imgCount = String.format("%1$d / %2$d", position + 1, vpAdapter.getCount());
        tv_imgNum.setText(imgCount);
    }

    @Override
    protected void initData() {
        List<Commit> commits = new ArrayList<>();
        Commit commit = new Commit();
        commit.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.q53c9FWOXGvw00Xr-a162wD6D6&w=198&h=198&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit.setMsg("哈哈哈哈哈或");
        commit.setNickName("donghua");
        commits.add(commit);
        Commit commit1 = new Commit();
        commit1.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.kwrzMA37JwCYw9jlfN1QEgEsEa&w=229&h=204&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit1.setMsg("巴巴爸爸不不不不不不");
        commit1.setNickName("按到");
        commits.add(commit1);
        Commit commit2 = new Commit();
        commit2.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.scCB3oW9MAoZj84PRRrAAgEsEs&pid=15.1");
        commit2.setMsg("飞机撒了几分几分");
        commit2.setNickName("大打发斯蒂芬");
        commits.add(commit2);
        Commit commit3 = new Commit();
        commit3.setHeadUrl("http://img1.3lian.com/img2012/12/49/d/54.jpg");
        commit3.setMsg("范德萨附近咖啡了");
        commit3.setNickName("大耳环");
        commits.add(commit3);
        Commit commit4 = new Commit();
        commit4.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.q53c9FWOXGvw00Xr-a162wD6D6&w=198&h=198&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit4.setMsg("哈哈哈哈哈或");
        commit4.setNickName("donghua");
        commits.add(commit4);
        Commit commit11 = new Commit();
        commit11.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.kwrzMA37JwCYw9jlfN1QEgEsEa&w=229&h=204&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit11.setMsg("巴巴爸爸不不不不不不");
        commit11.setNickName("按到");
        commits.add(commit11);
        Commit commit21 = new Commit();
        commit21.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.scCB3oW9MAoZj84PRRrAAgEsEs&pid=15.1");
        commit21.setMsg("飞机撒了几分几分");
        commit21.setNickName("大打发斯蒂芬");
        commits.add(commit21);
        Commit commit31 = new Commit();
        commit31.setHeadUrl("http://img1.3lian.com/img2012/12/49/d/54.jpg");
        commit31.setMsg("范德萨附近咖啡了");
        commit31.setNickName("大耳环");
        commits.add(commit31);
        Commit commit22 = new Commit();
        commit22.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.q53c9FWOXGvw00Xr-a162wD6D6&w=198&h=198&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit22.setMsg("哈哈哈哈哈或");
        commit22.setNickName("donghua");
        commits.add(commit22);
        Commit commit13 = new Commit();
        commit13.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.kwrzMA37JwCYw9jlfN1QEgEsEa&w=229&h=204&c=7&qlt=90&o=4&dpr=1.25&pid=1.7");
        commit13.setMsg("巴巴爸爸不不不不不不");
        commit13.setNickName("按到");
        commits.add(commit13);
        Commit commit23 = new Commit();
        commit23.setHeadUrl("http://tse2.mm.bing.net/th?id=OIP.scCB3oW9MAoZj84PRRrAAgEsEs&pid=15.1");
        commit23.setMsg("飞机撒了几分几分");
        commit23.setNickName("大打发斯蒂芬");
        commits.add(commit23);
        Commit commit33 = new Commit();
        commit33.setHeadUrl("http://img1.3lian.com/img2012/12/49/d/54.jpg");
        commit33.setMsg("范德萨附近咖啡了");
        commit33.setNickName("大耳环");
        commits.add(commit33);


        if (scope == Constants.FRIEND)
            rcvAdapter.setCommits(commits);
        rcvAdapter.setDiscovery(discovery);
        vp_img.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!needTransY)
                    return;
                maxRcvTransY = vp_img.getMeasuredHeight() - titleBar.getHeight();
                maxImgTransY = (titleBar.getHeight() - rl_img.getHeight()) * 0.4f;
                rcv_detail.setTranslationY(vp_img.getHeight() - titleBar.getHeight());

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
                rcvMgr.setCanScroll(rcv_detail.getTranslationY() == 0);
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

        if(rl_commitInput.getVisibility() == View.VISIBLE){
            Rect rect = new Rect();
            rl_commitInput.getGlobalVisibleRect(rect);
            if(ActDownY > rect.top) {
                rcvDY = 0;
                return false;
            }
        }
        if (rcvMgr.findLastVisibleItemPosition() == rcvAdapter.getItemCount() - 1) {
            rcvDY = 0;
            rst = false;
        } else if (isAnimRunning || rcvMgr.findFirstCompletelyVisibleItemPosition() != 0 || ActDownY < 0) {
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

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(glListener);
        } else {
            getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(glListener);
        }
        super.onDestroy();
    }

    private class MyLinearLayoutManager extends LinearLayoutManager{
        boolean canScroll = false;

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public void setCanScroll(boolean canScroll){
            this.canScroll = canScroll;
        }
        @Override
        public boolean canScrollVertically() {
            return canScroll;
        }
    }
}
