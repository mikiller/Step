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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uilib.customdialog.CustomDialog;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.adapters.DetailImgVpAdapter;
import com.westepper.step.adapters.DisDetailRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.BaseModel;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.CommitLogic;
import com.westepper.step.logics.GetCommitListLogic;
import com.westepper.step.logics.GetDisDetailInfoLogic;
import com.westepper.step.logics.GetDiscoveryListLogic;
import com.westepper.step.logics.JoinLogic;
import com.westepper.step.models.CommitModel;
import com.westepper.step.models.DisBase;
import com.westepper.step.models.JoinModel;
import com.westepper.step.responses.DiscoveryDetailInfo;
import com.westepper.step.responses.JoinResponse;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MXTimeUtils;
import com.westepper.step.widgets.CommitGlobalLayoutListener;
import com.westepper.step.responses.Commit;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.ImgDetail;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
    @BindView(R.id.tv_joinNum)
    TextView tv_joinNum;
    @BindView(R.id.tv_joinOpt)
    TextView tv_joinOpt;
    @BindView(R.id.ll_leftTime)
    LinearLayout ll_leftTime;
    @BindView(R.id.tv_hour)
    TextView tv_hour;
    @BindView(R.id.tv_min)
    TextView tv_min;
    @BindView(R.id.tv_sec)
    TextView tv_sec;
    @BindView(R.id.commitInput)
    CommitEditView commitInput;

    DetailImgVpAdapter vpAdapter;

    DisDetailRcvAdapter rcvAdapter;
    MyLinearLayoutManager rcvMgr;
    boolean isTouchImgVp = false;
    GetCommitListLogic commitLogic;
    GetDisDetailInfoLogic detailLogic;

    Discovery discovery;
    int scope;
    Timer timer;

    private float ActDownX, ActDownY, rcvDY, imgDy, titleDA;
    float maxRcvTransY, maxImgTransY;
    float rcvTransY, rlImgTransY;
    boolean isAnimRunning = false;
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
        titleBar.setBackImg(R.mipmap.ic_back2);
        titleBar.setSubImg(R.mipmap.ic_menu2);
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

        vp_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int minHeight = DisplayUtil.getScreenWidth(DiscoveryDetailActivity.this) / 4 * 3;
            int maxHeight = DisplayUtil.getScreenHeight(DiscoveryDetailActivity.this) / 4 * 3;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ImgDetail img = vpAdapter.getImgs().get(position);
                int fromHeight = getHeight(img.getImgHeight(), img.getImgWidth());
                int nextHeight = fromHeight;
                if (position < vpAdapter.getCount() - 1) {
                    ImgDetail img1 = vpAdapter.getImgs().get(position + 1);
                    nextHeight = getHeight(img1.getImgHeight(), img1.getImgWidth());
                }
                int height = (int) (fromHeight * (1 - positionOffset) + nextHeight * positionOffset);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) vp_img.getLayoutParams();
                lp.height = height;
                vp_img.setLayoutParams(lp);
            }

            private int getHeight(int src, int width) {
                if (width < DisplayUtil.getScreenWidth(DiscoveryDetailActivity.this))
                    src = (int) (1.0f * src / width * DisplayUtil.getScreenWidth(DiscoveryDetailActivity.this));
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
        vpAdapter = new DetailImgVpAdapter(this, discovery.getImgList());
        vp_img.setAdapter(vpAdapter);
        if (vpAdapter.getCount() == 0) {
            startAlpha(0, 1f);
            vp_img.getLayoutParams().height = 0;
        }
        setImgNum(0);

        rcvMgr = new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_detail.setLayoutManager(rcvMgr);
        rcvAdapter = new DisDetailRcvAdapter(this, rcv_detail);
        rcvAdapter.setDiscovery(discovery);
        rcvAdapter.setCommitListener(new DisDetailRcvAdapter.OnCommitListener() {
            @Override
            public void onCommit(final String id, String nickName) {
                if (!commitInput.isVisible()) {
                    commitInput.setNeedTransY(false);
                    commitInput.setHint(String.format("回复:%1$s", nickName));
                    showInputMethod(commitInput);
                    commitInput.setOnSendListener(new CommitEditView.OnSendListener() {
                        @Override
                        public void onSend(View focuseView, String txt) {
                            CommitLogic logic = new CommitLogic(DiscoveryDetailActivity.this, new CommitModel(id, discovery.getDiscoveryId(), discovery.getDiscoveryKind(), txt));
                            logic.setCallbackObject(commitInput, rcvAdapter).sendRequest();

                        }
                    });
                }
            }
        });
        rcv_detail.setAdapter(rcvAdapter);
        rcv_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemPos = rcvMgr.findFirstVisibleItemPosition();
                if (visibleItemPos <= 1) {
                    titleBar.setTitle(discovery.getNickName());
                } else {
                    titleBar.setTitle("留言");
                }
            }
        });

        ll_joinOtp.setVisibility(discovery.getDiscoveryKind() == Constants.MOOD ? View.GONE : View.VISIBLE);
        if (discovery.getDiscoveryKind() == Constants.OUTGO) {
            initJoinOpt();
        }


        vp_img.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!commitInput.isNeedTransY())
                    return;
                maxRcvTransY = vp_img.getMeasuredHeight() - titleBar.getHeight();
                maxImgTransY = (titleBar.getHeight() - rl_img.getHeight()) * 0.4f;
                rcv_detail.setTranslationY(vp_img.getHeight() - titleBar.getHeight());
            }
        });
    }

    private void setImgNum(int position) {
        String imgCount = String.format("%1$d / %2$d", position + 1, vpAdapter.getCount());
        tv_imgNum.setText(imgCount);
    }

    private void initJoinOpt() {
        setJoinNum(discovery.getJoinCount());
        tv_joinOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dlg for input join words
                //after get team id from yunxin send request
                getJoinLogic();
//                final CustomDialog dlg = new CustomDialog(DiscoveryDetailActivity.this);
//                dlg.setTitle("已报名参加约行").setDlgEditable(true).setDlgButtonListener(new CustomDialog.onButtonClickListener() {
//                    @Override
//                    public void onCancel() {
//                        hideInputMethod(dlg.getCurrentFocus());
//                    }
//
//                    @Override
//                    public void onSure() {
//                        JoinLogic logic = new JoinLogic(DiscoveryDetailActivity.this, new JoinModel(discovery.getDiscoveryId(), discovery.getTeamId()));
//                        logic.setCallback(new BaseLogic.LogicCallback<JoinResponse>() {
//                            @Override
//                            public void onSuccess(JoinResponse response) {
//                                setJoinNum(response.getJoinCount());
//                                tv_joinOpt.setText("已报名");
//                                tv_joinOpt.setEnabled(false);
//                            }
//
//                            @Override
//                            public void onFailed(String code, String msg, JoinResponse localData) {
//                                Toast.makeText(DiscoveryDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        logic.sendRequest();
//                        hideInputMethod(dlg.getCurrentFocus());
//                    }
//                }).show();

            }
        });
        tv_joinOpt.setEnabled(!discovery.getDiscoveryUserId().equals(SuperActivity.userInfo.getUserId()));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (ll_joinOtp == null)
                    return;
                ll_joinOtp.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!MXTimeUtils.isOutofLimit(discovery.getPushTime(), MXTimeUtils.DAY)) {
                            String time = MXTimeUtils.getLeftTime("HH:mm:ss", discovery.getPushTime(), MXTimeUtils.DAY);
                            String[] times = time.split(":");
                            tv_hour.setText(times[0]);
                            tv_min.setText(times[1]);
                            tv_sec.setText(times[2]);
                        } else {
                            ll_leftTime.setVisibility(View.GONE);
                            tv_joinOpt.setEnabled(false);
                            tv_joinOpt.setText("约行已结束");
                        }
                    }
                });

            }
        }, 0, 1000);
    }

    private void setJoinNum(int joinCount){
        if(discovery.getTotalCount() == 0)
            tv_joinNum.setText(String.format(getString(R.string.join_num1), joinCount));
        else
            tv_joinNum.setText(String.format(getString(R.string.join_num), discovery.getTotalCount(), joinCount));
    }

    private void getJoinLogic(){
        JoinLogic logic = new JoinLogic(DiscoveryDetailActivity.this, new JoinModel(discovery.getDiscoveryId(), discovery.getTeamId()));
        logic.setCallback(new BaseLogic.LogicCallback<JoinResponse>() {
            @Override
            public void onSuccess(JoinResponse response) {
                setJoinNum(response.getJoinCount());
                tv_joinOpt.setText("已报名");
                tv_joinOpt.setEnabled(false);
            }

            @Override
            public void onFailed(String code, String msg, JoinResponse localData) {
                Toast.makeText(DiscoveryDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        logic.sendRequest();
    }

    @Override
    protected void initData() {
        if (scope == Constants.FRIEND) {
            if (commitLogic == null) {
                commitLogic = new GetCommitListLogic(this, new CommitModel(discovery.getDiscoveryId(), discovery.getDiscoveryKind())).setAdapter(rcvAdapter);
            }
            commitLogic.sendRequest();
        }

        if(detailLogic == null){
            detailLogic = new GetDisDetailInfoLogic(this, new DisBase(discovery.getDiscoveryId(), discovery.getDiscoveryKind())).setRcvAdapter(rcvAdapter);
            detailLogic.setCallback(new BaseLogic.LogicCallback<DiscoveryDetailInfo>() {
                @Override
                public void onSuccess(DiscoveryDetailInfo response) {
                    setJoinNum(response.getJoinCount());
                    tv_joinOpt.setEnabled(!response.isJoin());
                    tv_joinOpt.setText(response.isJoin() ? "已报名" : "参加约行");
                }

                @Override
                public void onFailed(String code, String msg, DiscoveryDetailInfo localData) {

                }
            });
        }
        detailLogic.sendRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
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
                isTouchImgVp = ActDownY < rcvTransY + titleBar.getHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getY()) > 150) {
                    rcvDY = ev.getY() - ActDownY;
                    imgDy = rcvDY * 0.4f;
                }
                if (isTouchImgVp) {
                    if (Math.abs(ActDownX - ev.getX()) > 150) {
                        ActDownY = -1;
                        rcvDY = imgDy = 0;
                    }
                } else if (canMove()) {
                    rl_img.setTranslationY(getImgDeltaY(rlImgTransY, imgDy));
                    rcv_detail.setTranslationY(getDeltaY(rcvTransY, rcvDY));
                    titleBar.setBgAlpha(getTitleAlpha());
                } else {
                    ActDownY = ev.getY();
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

        if (commitInput.isVisible()) {
            Rect rect = new Rect();
            commitInput.getGlobalVisibleRect(rect);
            if (ActDownY > rect.top) {
                rcvDY = 0;
                return false;
            }
        }
//        if (rcvMgr.findLastVisibleItemPosition() == rcvAdapter.getItemCount() - 1) {
//            rcvDY = 0;
//            rst = false;
//        } else
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

    private class MyLinearLayoutManager extends LinearLayoutManager {
        boolean canScroll = false;

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public void setCanScroll(boolean canScroll) {
            this.canScroll = canScroll;
        }

        @Override
        public boolean canScrollVertically() {
            return canScroll;
        }
    }
}
