package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.activities.AllCommitsActivity;
import com.westepper.step.activities.MyMessageListActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.GoodLogic;
import com.westepper.step.models.DisModel;
import com.westepper.step.responses.Commit;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.GoodCount;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MXPreferenceUtils;
import com.westepper.step.utils.MXTimeUtils;
import com.westepper.step.widgets.CommitHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/9/21.
 */

public class DisDetailRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CommitImpl{

    private final int DETAIL = 0, COMMITS = 1, COMMIT_TITLE = 2;
    private Context mContext;
    private Discovery discovery;
    private List<Commit> commits;
    //private RecyclerView rcv;
    private OnCommitListener commitListener;

    private boolean isDetail = true;


    public DisDetailRcvAdapter(Context context) {
        this.mContext = context;
        //rcv = recyclerView;
    }

    public DisDetailRcvAdapter(Context context, boolean isDetail) {
        this(context);
        this.isDetail = isDetail;
    }

    @Override
    public int getItemViewType(int position) {
        if(!isDetail)
            return COMMITS;
        else if(position == 0)
            return DETAIL;
        else if(position == 1)
            return COMMIT_TITLE;
        else
            return COMMITS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == DETAIL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dis_detail, null);
            viewHolder = new DetailHolder(view);
        }else if(viewType == COMMIT_TITLE){
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dis_commit_title, null);
            MyMenuItem view = new MyMenuItem(parent.getContext());
            viewHolder = new CommitTitleHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_detail, null);
            viewHolder = new CommitHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DetailHolder)
            updateDetailHolder((DetailHolder) holder);
        else if(holder instanceof CommitTitleHolder) {
//            ((CommitTitleHolder) holder).tv_commitNum.setText(String.valueOf(getCommitSize()));
            MyMenuItem view = (MyMenuItem) holder.itemView;
            view.setMenuName("留言");
            view.setSubText(String.format(mContext.getString(R.string.commit_title), commits.size()));
            view.setNeedSubText(true);
            view.setNeedNext(true);
            view.getSubTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> args = new HashMap<>();
                    args.put(Constants.DISUSER_ID, discovery.getDiscoveryUserId());
                    args.put(Constants.DIS_KIND, discovery.getDiscoveryKind());
                    args.put(Constants.DIS_ID, discovery.getDiscoveryId());
                    ActivityManager.startActivity((Activity) mContext, AllCommitsActivity.class, args);
                }
            });
        } else if(getCommitSize() > 0) {
            int pos = isDetail ? (holder.getAdapterPosition() - 2) : holder.getAdapterPosition();
            updateCommitHolder((CommitHolder) holder, commits.get(pos));
        }
    }

    private void updateDetailHolder(final DetailHolder holder){
        holder.tv_nickName.setText(discovery.getNickName());
        GlideImageLoader.getInstance().loadImage(mContext, discovery.getHeadUrl(), R.mipmap.ic_default_head, holder.iv_header, 0);
        Drawable drawable = mContext.getResources().getDrawable(discovery.getGender() == 1 ? R.mipmap.male : R.mipmap.female);
        drawable.setBounds(0,0,drawable.getMinimumWidth(), drawable.getMinimumHeight());
        holder.tv_nickName.setCompoundDrawables(null, null, drawable, null);
        holder.tv_goodNum.setText("赞·".concat(getGoodNum()));
        holder.tv_detailMsg.setText(discovery.getInfo());
        holder.tv_detailPos.setText(discovery.getUserPos().getPoiTitle());
        holder.rl_join.setVisibility(discovery.getDiscoveryKind() == 1 ? View.GONE : View.VISIBLE);
        holder.tv_time.setText(MXTimeUtils.getFormatTime("yyyy/MM/dd", discovery.getPushTime()));
        holder.tv_goodNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> args = new HashMap<String, Object>();
                args.put(Constants.IS_MESSAGE, 0);
                args.put(Constants.DIS_ID, discovery.getDiscoveryId());
                args.put(Constants.DIS_KIND, discovery.getDiscoveryKind());
                ActivityManager.startActivity((Activity) mContext, MyMessageListActivity.class, args);
            }
        });

        if(discovery.getDiscoveryKind() == Constants.OUTGO){
            holder.tv_joinTime.setText(discovery.getEndTime() == 0 ? "不限" : MXTimeUtils.getFormatTime("yyyy年MM月dd日", discovery.getEndTime()));
        }
    }

    private String getGoodNum(){
        return String.valueOf(discovery.getGoodNum());
    }

    private void updateCommitHolder(CommitHolder holder, final Commit commit){
        if(commit == null)
            return;
        holder.setUserInfo(mContext, commit.getUserInfo().getNickName(), commit.getUserInfo().getHeadImg());
        holder.setTv_commit(TextUtils.isEmpty(commit.getComment_content()) ? commit.getMsg() : commit.getComment_content());
        holder.setTv_time(commit.getCreate_time());
//        holder.tv_nickName.setText(commit.getUserInfo().getNickName());
//        holder.tv_commit.setText(commit.getComment_content());
        View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commitListener != null)
                    commitListener.onCommit(commit);
            }
        };
        holder.setResPonseCounts(commit.getResponses_count(),itemListener);
        holder.setReplyClickListener(itemListener);
//        holder.tv_nickName.setOnClickListener(itemListener);
//        holder.tv_commit.setOnClickListener(itemListener);
    }

    @Override
    public void setCommits(List commits){
        this.commits = commits;
        notifyDataSetChanged();
    }

    @Override
    public void addCommit(Commit commit){
        if(commits == null)
            commits = new ArrayList<>();
        this.commits.add(0, commit);
        notifyItemInserted(isDetail ? 2 : 0);
        //rcv.scrollToPosition(0);
        if(isDetail)
            notifyItemChanged(1);
    }

    public int getCommitSize(){
        return commits == null ? 0 : commits.size();
    }

    public void setDiscovery(Discovery dis){
        this.discovery = dis;
        notifyItemChanged(0);
    }

    public void setCommitListener(OnCommitListener listener){
        commitListener = listener;
    }

    @Override
    public int getItemCount() {
        if(isDetail && discovery != null)
            return (commits == null || commits.size() == 0) ? 1 : commits.size() + 2;
        else
            return commits == null ? 0 : commits.size();
    }

    protected class DetailHolder extends RecyclerView.ViewHolder{
        private SelectableRoundedImageView iv_header;
        private TextView tv_nickName, tv_goodNum, tv_detailMsg, tv_detailPos, tv_time, tv_joinTime;
        private RelativeLayout rl_join;
        public DetailHolder(View itemView) {
            super(itemView);
            iv_header = (SelectableRoundedImageView) itemView.findViewById(R.id.iv_header);
            tv_nickName = (TextView) itemView.findViewById(R.id.tv_nickName);
            tv_goodNum = (TextView) itemView.findViewById(R.id.tv_goodNum);
            tv_detailMsg = (TextView) itemView.findViewById(R.id.tv_detailMsg);
            tv_detailPos = (TextView) itemView.findViewById(R.id.tv_detailPos);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_joinTime = (TextView) itemView.findViewById(R.id.tv_joinTime);
            rl_join = (RelativeLayout) itemView.findViewById(R.id.rl_join);
        }
    }

    protected class CommitTitleHolder extends RecyclerView.ViewHolder{

        public CommitTitleHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnCommitListener{
        void onCommit(Commit commit);
    }
}
