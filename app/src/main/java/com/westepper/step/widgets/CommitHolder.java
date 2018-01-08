package com.westepper.step.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.westepper.step.R;
import com.westepper.step.utils.MXTimeUtils;

/**
 * Created by Mikiller on 2018/1/8.
 */

public class CommitHolder extends RecyclerView.ViewHolder {
    private HeadImageView iv_header;
    private TextView tv_nickName, tv_commit, tv_time, tv_reply, tv_resCounts;
    private TextView tv_replyName;
    private LinearLayout ll_subReply;
    public CommitHolder(View itemView) {
        super(itemView);
        iv_header = (HeadImageView) itemView.findViewById(R.id.iv_header);
        tv_nickName = (TextView) itemView.findViewById(R.id.tv_nickName);
        tv_replyName = (TextView) itemView.findViewById(R.id.tv_replyName);
        tv_commit = (TextView) itemView.findViewById(R.id.tv_commit);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_reply = (TextView) itemView.findViewById(R.id.tv_reply);
        ll_subReply = (LinearLayout) itemView.findViewById(R.id.ll_subReply);
        tv_resCounts = (TextView) itemView.findViewById(R.id.tv_resCounts);
    }

    public void setUserInfo(Context context, String name, String imgUrl){
        tv_nickName.setText(name);
        GlideImageLoader.getInstance().loadImage(context, imgUrl, R.mipmap.ic_default_head, iv_header, 0);
    }

    public void setReplyUserInfo(Context context, String nickName, String imgUrl, String replyName){
        tv_nickName.setText(nickName);
        GlideImageLoader.getInstance().loadImage(context, imgUrl, R.mipmap.ic_default_head, iv_header, 0);
        tv_replyName.setText("回复了".concat(replyName));
    }

    public void setTv_commit(String commit){
        tv_commit.setText(commit);
    }

    public void setTv_time(long time){
        tv_time.setText(MXTimeUtils.getDiffTime(time));
    }

    public void setResPonseCounts(int counts, View.OnClickListener listener){
        ll_subReply.setVisibility(counts > 0 ? View.VISIBLE : View.GONE);
        tv_resCounts.setText(String.format("有%1$d条回复", counts));
        tv_resCounts.setOnClickListener(listener);
    }

    public void setReplyClickListener(View.OnClickListener listener){
        tv_reply.setOnClickListener(listener);
    }

    public ViewGroup getSubReplyLayout(){
        return ll_subReply;
    }
}
