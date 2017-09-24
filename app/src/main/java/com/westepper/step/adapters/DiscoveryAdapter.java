package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.activities.DiscoveryDetailActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.responses.Discovery;
import com.westepper.step.utils.ActivityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikiller on 2017/9/4.
 */

public class DiscoveryAdapter extends PagerAdapter {
    private List<Discovery> dataList;
    private Context mContext;
    private int scope;

    public DiscoveryAdapter(List<Discovery> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_discovery, null);
        container.addView(view);
        ViewHolder holder = new ViewHolder(view);
        Discovery discover = dataList.get(position);
        holder.setNickName(discover.getNickName());
        holder.setMsg(discover.getInfo());
        holder.setGender(discover.getGender());
        holder.setKind(discover.getDiscoveryKind());
        holder.setScope(scope);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> args = new HashMap<String, Object>();
                args.put(Constants.DISCOVERY_DETAIL, dataList.get(position));
                ActivityManager.startActivity((Activity) mContext, DiscoveryDetailActivity.class, args);
            }
        });
        return view;
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
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public class ViewHolder{
        private TextView tv_nickName, tv_msg, tv_time;
        private ImageView iv_gender;
        private SelectableRoundedImageView iv_img1, iv_img2, iv_img3;
        private CheckBox ckb_good;
        private ImageButton btn_discuss, btn_addFriend;
        private LinearLayout ll_join;
        private Button btn_join;
        public ViewHolder(View root) {
            tv_nickName = (TextView) root.findViewById(R.id.tv_nickName);
            tv_msg = (TextView) root.findViewById(R.id.tv_msg);
            tv_time = (TextView) root.findViewById(R.id.tv_time);
            iv_gender = (ImageView) root.findViewById(R.id.iv_gender);
            iv_img1 = (SelectableRoundedImageView) root.findViewById(R.id.iv_img1);
            iv_img2 = (SelectableRoundedImageView) root.findViewById(R.id.iv_img2);
            iv_img3 = (SelectableRoundedImageView) root.findViewById(R.id.iv_img3);
            ckb_good = (CheckBox) root.findViewById(R.id.ckb_good);
            btn_discuss = (ImageButton) root.findViewById(R.id.btn_discuss);
            btn_addFriend = (ImageButton) root.findViewById(R.id.btn_addFriend);
            ll_join = (LinearLayout) root.findViewById(R.id.ll_join);
            btn_join = (Button) root.findViewById(R.id.btn_join);
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

        }

        public void setKind(int discoveryKind){
            ll_join.setVisibility(discoveryKind == 1 ? View.GONE : View.VISIBLE);
        }

        public void setScope(int scope){
            btn_discuss.setVisibility(scope == 1 ? View.VISIBLE : View.GONE);
            btn_addFriend.setVisibility(scope == 1 ? View.GONE : View.VISIBLE);
        }
    }
}
