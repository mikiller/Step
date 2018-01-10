package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.uilib.customdialog.CustomDialog;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.activities.DiscoveryDetailActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.logics.CommitLogic;
import com.westepper.step.logics.GoodLogic;
import com.westepper.step.logics.JoinLogic;
import com.westepper.step.models.CommitModel;
import com.westepper.step.models.DisBase;
import com.westepper.step.models.DisModel;
import com.westepper.step.models.JoinModel;
import com.westepper.step.models.Privacy;
import com.westepper.step.responses.Commit;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.GoodCount;
import com.westepper.step.responses.ImgDetail;
import com.westepper.step.responses.JoinResponse;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.ContactsHelper;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MXTimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mikiller on 2017/9/4.
 */

public class DiscoveryAdapter extends PagerAdapter {
    private List<Discovery> dataList;
    private Context mContext;
    private int scope;

    DiscoveryHolder currentHolder;
    CommitEditView commitInput;
    getCurrentHolderListener currentHolderListener;
    Timer timer = new Timer();
    TimerTask timerTask;

    public DiscoveryAdapter(List<Discovery> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    public void setCommitInput(CommitEditView view){
        commitInput = view;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_discovery, null);
        container.addView(view);
        final DiscoveryHolder holder = new DiscoveryHolder(view);
        final Discovery discover = dataList.get(position);
        holder.setUserHeader(discover.getHeadUrl());
        holder.setNickName(discover.getNickName());
        holder.setMsg(discover.getInfo());
        holder.setGender(discover.getGender());
        holder.setKind(discover.getDiscoveryKind());
        holder.setScope(scope);
        holder.setImgs(discover.getImgList());
        holder.setTime(discover.getPushTime());
        holder.setGoodListener(getHasGood(discover.getDiscoveryId() + SuperActivity.userInfo.getUserId()), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                GoodLogic logic = new GoodLogic(mContext, new DisModel(discover.getDiscoveryId(), discover.getDiscoveryKind()));
                logic.setCallback(new BaseLogic.LogicCallback<GoodCount>() {
                    @Override
                    public void onSuccess(GoodCount response) {
                        discover.setGoodNum(response.getCount());
                    }

                    @Override
                    public void onFailed(String code, String msg, GoodCount localData) {

                    }
                });
                logic.sendRequest();
            }
        } );
        holder.setAddFriendListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getUserinfo from yunxin
                // get ext info
                // call contactshelper.addfriend
//                showAddFriendDlg();
                getUserInfoFromRemote(v, discover.getDiscoveryUserId());
            }
        });
        holder.setOnCommitListener(discover.getDiscoveryUserId(), discover.getNickName(), new OnCommitListener() {
            @Override
            public void onCommit(final String id, final String nickName) {
                if(!commitInput.isVisible()){
                    commitInput.setNeedTransY(false);
                    commitInput.setHint(String.format("回复:%1$s", nickName));
                    ((SuperActivity)container.getContext()).showInputMethod(commitInput);
                    commitInput.setOnSendListener(new CommitEditView.OnSendListener() {
                        @Override
                        public void onSend(View focuseView, String txt) {
                            CommitLogic logic = new CommitLogic(container.getContext(), new CommitModel(id, discover.getDiscoveryId(), discover.getDiscoveryKind(), txt));
                            logic.setCallback(new BaseLogic.LogicCallback<Commit>() {
                                @Override
                                public void onSuccess(Commit response) {
                                    discover.setCommitNum(discover.getCommitNum() + 1);
                                }

                                @Override
                                public void onFailed(String code, String msg, Commit localData) {

                                }
                            });
                            logic.setCallbackObject(commitInput, null).sendRequest();

                        }
                    });
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> args = new HashMap<String, Object>();
                args.put(Constants.DIS_SCOPE, scope);
                args.put(Constants.DISCOVERY_DETAIL, new DisBase(discover.getDiscoveryId(), discover.getDiscoveryKind()));
                ActivityManager.startActivity((Activity) mContext, DiscoveryDetailActivity.class, args);
            }
        });

        if(discover.getDiscoveryKind() == Constants.OUTGO) {
            holder.setJoinBtn(discover.isJoin(), discover.getDiscoveryUserId(), discover.getPushTime());
            holder.btn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinOutGoLogic(holder, discover);
                }
            });
        }
        view.setTag(holder);
        return view;
    }

    private boolean getHasGood(String key){
        return !MXPreferenceUtils.getInstance().getBoolean(key);
    }

    private void getUserInfoFromRemote(final View btn, final String account){
        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo nimUserInfo) {
                Privacy privacy = new Gson().fromJson(nimUserInfo.getExtension(), Privacy.class);
                ContactsHelper.addFriend(btn, account, (privacy != null && privacy.getNeedFriendVerifi() == 1));
            }

            @Override
            public void onFailed(int code) {
                if (code == 408) {
                    Toast.makeText(mContext, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable throwable) {
                Toast.makeText(mContext, "on exception:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void joinOutGoLogic(final DiscoveryHolder holder, final Discovery discover){
        JoinLogic logic = new JoinLogic(mContext, new JoinModel(discover.getDiscoveryId()));
        logic.setCallback(new BaseLogic.LogicCallback<JoinResponse>() {
            @Override
            public void onSuccess(JoinResponse response) {
                discover.setJoin(1);
                holder.setBtnJoinEnabled(false, "已报名");
                cancelTask();
            }

            @Override
            public void onFailed(String code, String msg, JoinResponse localData) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
        logic.sendRequest();
    }

    public void setDataList(List<Discovery> dataList) {
        this.dataList = dataList;
        this.notifyDataSetChanged();
    }

    public void setScope(int scope){
        this.scope = scope;
    }

    public Discovery getItem(int pos){
        return dataList == null ? null : dataList.get(pos);
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if(object != null) {
            currentHolder = (DiscoveryHolder) ((View) object).getTag();
            if(currentHolderListener != null) {
                currentHolderListener.getCurrentHolder(currentHolder);
                currentHolderListener = null;
            }
        }
    }

    public void cancelTask(){
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void setCurrentHolderListener(getCurrentHolderListener listener){
        currentHolderListener = listener;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public class DiscoveryHolder{
        private SelectableRoundedImageView iv_userHeader;
        private TextView tv_nickName, tv_msg, tv_time;
//        private ImageView iv_gender;
        private SelectableRoundedImageView[] iv_imgList = new SelectableRoundedImageView[3];
        private ImageButton btn_good, btn_discuss, btn_addFriend;
        private LinearLayout ll_join;
        private Button btn_join;
        public DiscoveryHolder(View root) {
            iv_userHeader = (SelectableRoundedImageView) root.findViewById(R.id.iv_userHeader);
            tv_nickName = (TextView) root.findViewById(R.id.tv_nickName);
            tv_msg = (TextView) root.findViewById(R.id.tv_msg);
            tv_time = (TextView) root.findViewById(R.id.tv_time);
//            iv_gender = (ImageView) root.findViewById(R.id.iv_gender);
            iv_imgList[0] = (SelectableRoundedImageView) root.findViewById(R.id.iv_img1);
            iv_imgList[1] = (SelectableRoundedImageView) root.findViewById(R.id.iv_img2);
            iv_imgList[2] = (SelectableRoundedImageView) root.findViewById(R.id.iv_img3);
            btn_good = (ImageButton) root.findViewById(R.id.btn_good);
            btn_discuss = (ImageButton) root.findViewById(R.id.btn_discuss);
            btn_addFriend = (ImageButton) root.findViewById(R.id.btn_addFriend);
            ll_join = (LinearLayout) root.findViewById(R.id.ll_join);
            btn_join = (Button) root.findViewById(R.id.btn_join);
        }

        public void setUserHeader(String url){
            GlideImageLoader.getInstance().loadImage(mContext, url, R.mipmap.ic_default_head, iv_userHeader, 0);
        }

        public void setNickName(String nickName){
            tv_nickName.setText(nickName);
        }

        public void setGender(int gender){
            Drawable drawable = mContext.getResources().getDrawable(gender == 1 ? R.mipmap.male : R.mipmap.female);
            drawable.setBounds(0,0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_nickName.setCompoundDrawables(null, null, drawable, null);
        }

        public void setMsg(String msg){
            tv_msg.setText(msg);
        }

        public void setTime(long time){
            tv_time.setText(MXTimeUtils.getDiffTime(time));
        }

        public void setKind(int discoveryKind){
            ll_join.setVisibility(discoveryKind == 1 ? View.GONE : View.VISIBLE);
        }

        public void setScope(int scope){
            btn_discuss.setVisibility(scope == 1 ? View.VISIBLE : View.GONE);
            btn_addFriend.setVisibility(scope == 1 ? View.GONE : View.VISIBLE);
        }

        public void setImgs(List<ImgDetail> imgList){
            for(int i = 0; i < 3; i++) {
                if(i >= imgList.size())
                    return;
                GlideImageLoader.getInstance().loadImage(mContext, imgList.get(i).getThumb_url(), R.mipmap.placeholder, iv_imgList[i], 0);
            }
        }

        public void setOnCommitListener(final String id, final String nickName, final OnCommitListener listener){
            if(listener != null){
                btn_discuss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCommit(id, nickName);
                    }
                });
            }
        }

        public void setGoodListener(boolean hasGood, View.OnClickListener listener){
            btn_good.setEnabled(hasGood);
            btn_good.setOnClickListener(listener);
        }

        public void setAddFriendListener(View.OnClickListener listener){
            btn_addFriend.setOnClickListener(listener);
        }

        public void updateLeftTime(final boolean isJoin, final String id, final long time){
            if(isJoin) {
                cancelTask();
                return;
            }
            timer.schedule(timerTask = new TimerTask() {
                @Override
                public void run() {
                    btn_join.post(new Runnable() {
                        @Override
                        public void run() {
                            setJoinBtn(isJoin, id, time);
                        }
                    });
                }
            }, 0, 60000);

        }

        public void setBtnJoinEnabled(boolean enabled, String txt){
            btn_join.setEnabled(enabled);
            btn_join.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
            btn_join.setText(txt);
        }

        private void setJoinBtn(boolean isJoin, String id, long time){
            if(isJoin)
                setBtnJoinEnabled(false, "已报名");
            else if(MXTimeUtils.isOutofLimit(time, MXTimeUtils.DAY))
                setBtnJoinEnabled(false, "已结束");
            else if(id.equals(MXPreferenceUtils.getInstance().getString("account")))
                setBtnJoinEnabled(false, "参加约行");
            else
                setBtnJoinEnabled(true, "参加约行 ".concat(MXTimeUtils.getLeftTime(time, MXTimeUtils.DAY)));
        }
    }

    public interface OnCommitListener{
        void onCommit(String id, String nickName);
    }

    public interface getCurrentHolderListener{
        void getCurrentHolder(DiscoveryHolder holder);
    }
}
