package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
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

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.activities.DiscoveryDetailActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.logics.CommitLogic;
import com.westepper.step.logics.GoodLogic;
import com.westepper.step.models.CommitModel;
import com.westepper.step.models.DisModel;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.GoodCount;
import com.westepper.step.responses.ImgDetail;
import com.westepper.step.utils.ActivityManager;
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
        DiscoveryHolder holder = new DiscoveryHolder(view);
        final Discovery discover = dataList.get(position);
        holder.setUserHeader(discover.getHeadUrl());
        holder.setNickName(discover.getNickName());
        holder.setMsg(discover.getInfo());
        holder.setGender(discover.getGender());
        holder.setKind(discover.getDiscoveryKind());
        holder.setScope(scope);
        holder.setImgs(discover.getImgList());
        holder.setTime(discover.getPushTime());
        holder.setGoodListener(getHasGood(discover.getDiscoveryId() + MXPreferenceUtils.getInstance().getString("account")), new View.OnClickListener() {
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
                args.put(Constants.DISCOVERY_DETAIL, discover);
                ActivityManager.startActivity((Activity) mContext, DiscoveryDetailActivity.class, args);
            }
        });

        if(discover.getDiscoveryKind() == Constants.OUTGO) {
            if(MXTimeUtils.isOutofLimit(discover.getPushTime(), MXTimeUtils.DAY))
                holder.setBtnJoinEnabled(false, "已结束");
            else
                holder.setBtnJoinEnabled(true, "参加约行 ".concat(MXTimeUtils.getLeftTime(discover.getPushTime(), MXTimeUtils.DAY)));
        }
        view.setTag(holder);
        return view;
    }

    private boolean getHasGood(String key){
        return !MXPreferenceUtils.getInstance().getBoolean(key);
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
        private ImageView iv_gender;
        private SelectableRoundedImageView[] iv_imgList = new SelectableRoundedImageView[3];
        private ImageButton btn_good;
        private ImageButton btn_discuss, btn_addFriend;
        private LinearLayout ll_join;
        private Button btn_join;
        public DiscoveryHolder(View root) {
            iv_userHeader = (SelectableRoundedImageView) root.findViewById(R.id.iv_userHeader);
            tv_nickName = (TextView) root.findViewById(R.id.tv_nickName);
            tv_msg = (TextView) root.findViewById(R.id.tv_msg);
            tv_time = (TextView) root.findViewById(R.id.tv_time);
            iv_gender = (ImageView) root.findViewById(R.id.iv_gender);
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
            iv_gender.setImageResource(gender == 1 ? R.mipmap.male : R.mipmap.female);
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
                GlideImageLoader.getInstance().loadImage(mContext, imgList.get(i).getImg_url(), R.mipmap.placeholder, iv_imgList[i], 0);
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

        public void updateLeftTime(final long time){
            timer.schedule(timerTask = new TimerTask() {
                @Override
                public void run() {
                    btn_join.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!MXTimeUtils.isOutofLimit(time, MXTimeUtils.DAY)) {
                                btn_join.setText("参加约行 ".concat(MXTimeUtils.getLeftTime(time, MXTimeUtils.DAY)));
                                Log.e("dis adapter", btn_join.getText().toString());
                            }else{
                                setBtnJoinEnabled(false, "已结束");
                            }
                        }
                    });
                }
            }, 0, 60000);

        }

        public void setBtnJoinEnabled(boolean enabled, String txt){
            btn_join.setEnabled(enabled);
            btn_join.setText(txt);
        }
    }

    public interface OnCommitListener{
        void onCommit(String id, String nickName);
    }

    public interface getCurrentHolderListener{
        void getCurrentHolder(DiscoveryHolder holder);
    }
}
