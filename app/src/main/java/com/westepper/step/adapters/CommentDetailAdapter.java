package com.westepper.step.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.Commit;
import com.westepper.step.utils.MXTimeUtils;

import java.util.List;

/**
 * Created by Mikiller on 2018/1/5.
 */

public class CommentDetailAdapter extends RecyclerView.Adapter<CommentDetailAdapter.CommitHolder> {
    private List<Commit> commits;
    private Context mContext;

    public CommentDetailAdapter(List<Commit> commits) {
        this.commits = commits;
    }

    @Override
    public CommitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_detail, null);
        return new CommitHolder(view);
    }

    @Override
    public void onBindViewHolder(CommitHolder holder, int position) {
        Commit commit = commits.get(position);
        holder.setUserInfo(commit.getUserInfo().getNickName(), commit.getUserInfo().getHeadImg());
        holder.setTv_commit(commit.getComment_content());
        holder.setTv_time(commit.getCreate_time());
        holder.tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commits == null ? 0 : commits.size();
    }

    protected class CommitHolder extends RecyclerView.ViewHolder{
        private HeadImageView iv_header;
        private TextView tv_nickName, tv_commit, tv_time, tv_reply;
        private LinearLayout ll_subReply;
        public CommitHolder(View itemView) {
            super(itemView);
            iv_header = (HeadImageView) itemView.findViewById(R.id.iv_header);
            tv_nickName = (TextView) itemView.findViewById(R.id.tv_nickName);
            tv_commit = (TextView) itemView.findViewById(R.id.tv_commit);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_reply = (TextView) itemView.findViewById(R.id.tv_reply);
            ll_subReply = (LinearLayout) itemView.findViewById(R.id.ll_subReply);
        }

        public void setUserInfo(String name, String imgUrl){
            tv_nickName.setText(name);
            GlideImageLoader.getInstance().loadImage(mContext, imgUrl, R.mipmap.ic_default_head, iv_header, 0);
        }

        public void setTv_commit(String commit){
            tv_commit.setText(commit);
        }

        public void setTv_time(long time){
            tv_time.setText(MXTimeUtils.getDiffTime(time));
        }
    }
}
