package com.westepper.step.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.models.DisBase;
import com.westepper.step.responses.MyMsgList;
import com.westepper.step.utils.MXTimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2018/1/3.
 */

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MsgHolder> {
    private Context mContext;
    private List<MyMsgList.MyMessage> messageList;
    @Override
    public MsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, null);
        return new MsgHolder(view);
    }

    @Override
    public void onBindViewHolder(MsgHolder holder, final int position) {
        MyMsgList.MyMessage message = messageList.get(position);
        holder.setHeader(message.getFromUser().getHeadImg());
        holder.setTv_userName(message.getFromUser().getNickName(), message.getMessageType(), message.getDiscoverKind());
        holder.setTv_time(message.getCreated_at());
        holder.setTv_commit(message.getContent(), message.getIsDelete(), message.getMessageType());
        holder.rl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMsgList.MyMessage msg = messageList.get(position);
                Map<String, Object> args = new HashMap<>();
                args.put(Constants.DISCOVERY_DETAIL, new DisBase(msg.getDiscoverId(), msg.getDiscoverKind()));
                args.put(Constants.DIS_SCOPE, Constants.FRIEND);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    public void setMsgList(List<MyMsgList.MyMessage> msgList){
        messageList = msgList;
        notifyDataSetChanged();
    }

    public void addMsgList(List<MyMsgList.MyMessage> msgList){
        messageList.addAll(msgList);
        notifyDataSetChanged();
    }

    public class MsgHolder extends RecyclerView.ViewHolder{
        private HeadImageView iv_header;
        private TextView tv_userName, tv_time, tv_commit;
        private RelativeLayout rl_bg;

        public MsgHolder(View itemView) {
            super(itemView);
            iv_header = (HeadImageView) itemView.findViewById(R.id.iv_header);
            tv_userName = (TextView) itemView.findViewById(R.id.tv_userName);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_commit = (TextView) itemView.findViewById(R.id.tv_commit);
            rl_bg = (RelativeLayout) itemView.findViewById(R.id.rl_bg);
        }

        public void setHeader(String imgUrl){
            GlideImageLoader.getInstance().loadImage(mContext, imgUrl, R.mipmap.ic_default_head, iv_header, 0);
        }

        public void setTv_userName(String userName, int msgType, int discoverKind){
            String doing = msgType == 3 ? "点赞了你的" : (msgType == 4 ? "参加了你的" : "回复了你的");
            String part = msgType == 2 ? "评论" : (discoverKind == 1 ? "心情" : "约行");
            tv_userName.setText(userName.concat(doing).concat(part));
        }

        public void setTv_time(long time){
            tv_time.setText(MXTimeUtils.getDiffTime(time));
        }

        public void setTv_commit(String commit, int isDelete, int msgType){
            tv_commit.setVisibility(msgType >= 3 ? View.GONE : View.VISIBLE);
            tv_commit.setText(isDelete == 1 ? "该评论已被删除" : commit);
        }
    }
}
