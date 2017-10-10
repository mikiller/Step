package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.activities.AllCommitsActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.customViews.MyMenuItem;
import com.westepper.step.responses.Commit;
import com.westepper.step.responses.Discovery;
import com.westepper.step.utils.ActivityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/9/21.
 */

public class DisDetailRcvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int DETAIL = 0, COMMITS = 1, COMMIT_TITLE = 2;
    private Context mContext;
    private Discovery discovery;
    private List<Commit> commits;
    private OnCommitListener commitListener;

    private boolean isDetail = true;


    public DisDetailRcvAdapter(Context context) {
        this.mContext = context;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dis_commitlist, null);
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> args = new HashMap<>();
                    args.put(Constants.COMMIT_LIST, commits);
                    ActivityManager.startActivity((Activity) mContext, AllCommitsActivity.class, args);
                }
            });
        } else if(getCommitSize() > 0) {
            int pos = isDetail ? (holder.getAdapterPosition() - 2) : holder.getAdapterPosition();
            updateCommitHolder((CommitHolder) holder, commits.get(pos));
        }
    }

    private void updateDetailHolder(DetailHolder holder){
        holder.tv_nickName.setText(discovery.getNickName());
        GlideImageLoader.getInstance().loadImage(mContext, discovery.getHeadUrl(), R.mipmap.ic_default_head, holder.iv_header, 0);
        holder.iv_gender.setImageResource(discovery.getGender() == 1 ? R.mipmap.male : R.mipmap.female);
        holder.tv_detailMsg.setText(discovery.getInfo());
        holder.tv_detailPos.setText(discovery.getUserPos().getPoiTitle());
        holder.rl_join.setVisibility(discovery.getDiscoveryKind() == 1 ? View.GONE : View.VISIBLE);
        holder.tv_time.setText("2017/4/5");
        holder.btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commitListener != null)
                    commitListener.onCommit(discovery.getDiscoveryId(), discovery.getNickName());
            }
        });
    }

    private void updateCommitHolder(CommitHolder holder, final Commit commit){
        if(commit == null)
            return;
        holder.tv_nickName.setText(commit.getNickName().concat(":"));
        holder.tv_commit.setText(commit.getMsg());
        View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commitListener != null)
                    commitListener.onCommit(commit.getCommitId(), commit.getNickName());
            }
        };
        holder.tv_nickName.setOnClickListener(itemListener);
        holder.tv_commit.setOnClickListener(itemListener);
    }

    public void setCommits(List commits){
        this.commits = commits;
        notifyDataSetChanged();
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
        if(isDetail)
            return commits == null ? 1 : commits.size() + 2;
        else
            return commits == null ? 0 : commits.size();
    }

    public class DetailHolder extends RecyclerView.ViewHolder{
        private SelectableRoundedImageView iv_header;
        private TextView tv_nickName, tv_goodNum, tv_detailMsg, tv_detailPos, tv_time, tv_joinTime;
        private ImageView iv_gender;
        private ImageButton btn_good, btn_commit;
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
            iv_gender = (ImageView) itemView.findViewById(R.id.iv_gender);
            btn_good = (ImageButton) itemView.findViewById(R.id.btn_good);
            btn_commit = (ImageButton) itemView.findViewById(R.id.btn_commit);
            rl_join = (RelativeLayout) itemView.findViewById(R.id.rl_join);
        }
    }

    public class CommitTitleHolder extends RecyclerView.ViewHolder{

        public CommitTitleHolder(View itemView) {
            super(itemView);
        }
    }

    public class CommitHolder extends RecyclerView.ViewHolder{
        private TextView tv_nickName, tv_commit, tv_commitTime;
        private ImageButton btn_recommit;
        public CommitHolder(View itemView) {
            super(itemView);
            tv_commit = (TextView) itemView.findViewById(R.id.tv_commit);
            tv_nickName = (TextView) itemView.findViewById(R.id.tv_nickName);
            tv_commitTime = (TextView) itemView.findViewById(R.id.tv_commitTime);
            btn_recommit = (ImageButton) itemView.findViewById(R.id.btn_recommit);
        }
    }

    public interface OnCommitListener{
        void onCommit(String id, String nickName);
    }
}
