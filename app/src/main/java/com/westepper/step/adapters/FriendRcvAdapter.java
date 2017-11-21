package com.westepper.step.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.session.activity.P2PMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.westepper.step.R;
import com.westepper.step.responses.UserInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/21.
 */

public class FriendRcvAdapter extends RecyclerView.Adapter<FriendRcvAdapter.FriendHolder> {
    private List<NimUserInfo> userList = new ArrayList<>();
    private Context mContext;

    public FriendRcvAdapter(NimUserInfo user) {
        userList.add(user);
    }

    @Override
    public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        FriendHolder holder = new FriendHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_friend, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendHolder holder, int position) {
        NimUserInfo user = userList.get(position);
        holder.setHeaderImg(user.getAccount());
        holder.setName(user.getName());
        holder.setUserId(user.getAccount());
        holder.addFriend(mContext, user.getAccount());

    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void addFriend(NimUserInfo user){
        userList.clear();
        userList.add(user);
        notifyDataSetChanged();
    }

    public static class FriendHolder extends RecyclerView.ViewHolder{
        private HeadImageView iv_header;
        private TextView tv_name, tv_id, btn_add;
        public FriendHolder(View itemView) {
            super(itemView);

            iv_header = (HeadImageView) itemView.findViewById(R.id.iv_header);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            btn_add = (TextView) itemView.findViewById(R.id.btn_add);
        }

        public void setHeaderImg(String account){
            iv_header.loadBuddyAvatar(account);
        }

        public void setName(String name){
            tv_name.setText(name);
        }

        public void setUserId(String id){
            tv_id.setText("ID:".concat(id));
        }

        public void addFriend(final Context context, final String account){
            final boolean isFirend = NIMClient.getService(FriendService.class).isMyFriend(account);
            if(isFirend) {
                btn_add.setText("聊天");
                Toast.makeText(context, account.concat("已经是你的好友"), Toast.LENGTH_SHORT).show();
            }else
                btn_add.setText("添加");
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFirend){

                    }else{
                        NimUIKit.startP2PSession(context, account);
                    }
                }
            });
        }
    }
}
