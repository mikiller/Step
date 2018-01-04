package com.westepper.step.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.westepper.step.R;
import com.westepper.step.responses.SimpleUser;
import com.westepper.step.utils.MXTimeUtils;

import java.util.List;

/**
 * Created by Mikiller on 2018/1/4.
 */

public class GoodUserAdapter extends RecyclerView.Adapter<GoodUserAdapter.UserHolder> {

    private Context mContext;
    private List<SimpleUser> users;
    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, null);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        SimpleUser user = users.get(position);
        holder.setHeader(user.getHeadImg());
        holder.setTv_userName(user.getNickName());
        holder.setTv_time(user.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public void setUsers(List<SimpleUser> users){
        this.users = users;
        notifyDataSetChanged();
    }

    protected class UserHolder extends RecyclerView.ViewHolder{
        private HeadImageView iv_header;
        private TextView tv_userName, tv_time;
        public UserHolder(View itemView) {
            super(itemView);
            iv_header = (HeadImageView) itemView.findViewById(R.id.iv_header);
            tv_userName = (TextView) itemView.findViewById(R.id.tv_userName);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }

        public void setHeader(String imgUrl){
            GlideImageLoader.getInstance().loadImage(mContext, imgUrl, R.mipmap.ic_default_head, iv_header, 0);
        }

        public void setTv_userName(String userName){
            tv_userName.setText(userName);
        }

        public void setTv_time(long time){
            tv_time.setText(MXTimeUtils.getDiffTime(time));
        }
    }
}
