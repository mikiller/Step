package com.westepper.step.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.activities.MyCityActivity;
import com.westepper.step.responses.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/27.
 */

public class JoinUserAdapter extends RecyclerView.Adapter<JoinUserAdapter.JoinUserHolder> {
    private Context mContext;

    List<String> accounts = new ArrayList<>();

    List<UserInfo> members;

    public List<String> getAccounts() {
        return accounts;
    }

    public JoinUserAdapter(Context context, List<UserInfo> members) {
        mContext = context;
        this.members = members;
    }

    @Override
    public JoinUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member_select, null);
        return new JoinUserHolder(view);
    }

    @Override
    public void onBindViewHolder(JoinUserHolder holder, int position) {
        final UserInfo user = members.get(position);
        holder.setUserInfo(user);
        holder.setOnCheckedListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    accounts.add(user.getUserId());
                }else{
                    accounts.remove(user.getUserId());
                }
                Log.e("join user adapter", "count: " + accounts.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return members == null ? 0 : members.size();
    }

    protected static class JoinUserHolder extends RecyclerView.ViewHolder{

        private CheckBox ckb_select;
        private SelectableRoundedImageView iv_header;
        private TextView tv_nickName;
        public JoinUserHolder(View itemView) {
            super(itemView);
            ckb_select = (CheckBox) itemView.findViewById(R.id.ckb_select);
            iv_header = (SelectableRoundedImageView) itemView.findViewById(R.id.iv_header);
            tv_nickName = (TextView) itemView.findViewById(R.id.tv_nickName);
        }

        public void setUserInfo(UserInfo userInfo){
            GlideImageLoader.getInstance().loadImage(itemView.getContext(), userInfo.getHeadImg(), R.mipmap.ic_default_head, iv_header, 0);
            tv_nickName.setText(userInfo.getNickName());
        }

        public void setOnCheckedListener(CompoundButton.OnCheckedChangeListener listener){
            ckb_select.setOnCheckedChangeListener(listener);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ckb_select.setChecked(!ckb_select.isChecked());
                }
            });
        }
    }
}
