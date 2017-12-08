package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.westepper.step.R;
import com.westepper.step.activities.MyCityActivity;
import com.westepper.step.base.Constants;
import com.westepper.step.customViews.AchieveBadge;
import com.westepper.step.responses.AchieveProgress;
import com.westepper.step.responses.CityProgress;
import com.westepper.step.responses.MyAchievements;
import com.westepper.step.utils.ActivityManager;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Mikiller on 2017/10/24.
 */

public class AchieveRcvAdapter extends RecyclerView.Adapter {
    private final int HEADER = 0, PROGRESS = 1;
    private final int[] achIcons = new int[]{R.mipmap.ic_ach_step, R.mipmap.ic_ach_dis, R.mipmap.ic_ach_sh, R.mipmap.ic_ach_pos, R.mipmap.ic_ach_timer};
    private Context mContext;
    private boolean needHead = true;
    private int achKind;

//    private View.OnClickListener listener;
    private OnMenuClickListener listener;
//    private List<AchieveProgress> pgsList;
    private MyAchievements myAchieve = new MyAchievements();

    public AchieveRcvAdapter(Context mContext, int achKind) {
        this.mContext = mContext;
        this.achKind = achKind;
    }

    public boolean isNeedHead() {
        return needHead;
    }

    public void setNeedHead(boolean needHead) {
        this.needHead = needHead;
    }

//    public List<AchieveProgress> getPgsList() {
//        return pgsList;
//    }

//    public void setPgsList(List<AchieveProgress> pgsList) {
//        this.pgsList = pgsList;
//        notifyDataSetChanged();
//    }


    public MyAchievements getMyAchieve() {
        return myAchieve;
    }

    public void setMyAchieve(MyAchievements myAchieve) {
        this.myAchieve = myAchieve;
        notifyDataSetChanged();
    }

    public void setListener(OnMenuClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_myachieve_headview, parent, false);
            return new HeadHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myach_city, parent, false);
            return new AchProgressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeadHolder){
            ((HeadHolder)holder).setBgColor();
            ((HeadHolder)holder).setTv_score(myAchieve.getCredit());
            ((HeadHolder)holder).setInfoBtn();
            ((HeadHolder)holder).setBadgeTitle();
            ((HeadHolder)holder).setBadge(myAchieve.getL1(), myAchieve.getL2(), myAchieve.getL3(), myAchieve.getL4());
            ((HeadHolder)holder).setAchTitle();
        }else if(holder instanceof AchProgressHolder){
            final String title;
            if (myAchieve.getType() == Constants.ACH_BADGE) {
                final AchieveProgress achPgs = myAchieve.getPercentList().get(holder.getAdapterPosition() - (needHead ? 1 : 0));
                ((AchProgressHolder)holder).setMenu_icon(achIcons[achPgs.getCategoryId() - 1]);
                ((AchProgressHolder)holder).setSubTitle(achPgs.getCategoryName());
                ((AchProgressHolder)holder).setNeedSubTitle(true);
                ((AchProgressHolder)holder).setPgs(achPgs.getPercent());
                title = achPgs.getCategoryName();
            } else {
                CityProgress cityPgs = myAchieve.getDiscoverCityList().get(holder.getAdapterPosition() - ((needHead ? 1 : 0)));
                ((AchProgressHolder)holder).setTitle(cityPgs.getCityName());
                ((AchProgressHolder)holder).setPoint(cityPgs.getPoint());
                ((AchProgressHolder)holder).setNeedSubTitle(false);
                ((AchProgressHolder)holder).setNeedNext(myAchieve.getType() == Constants.ACH_AREA ? false : true);
                ((AchProgressHolder)holder).setPgs(cityPgs.getReachedPercent());
                title = cityPgs.getCityName();
            }
            if(listener != null && ((AchProgressHolder) holder).canClick() && !"初识STEP".equals(title)){
                ((AchProgressHolder)holder).setAchMenuClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setNeedHead(false);
                        listener.onMenuClicked(title, myAchieve.getType());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int listSize = (myAchieve.getType() == Constants.ACH_BADGE) ? (myAchieve.getPercentList() == null ? 0 : myAchieve.getPercentList().size()) : (myAchieve.getDiscoverCityList() == null ? 0 : myAchieve.getDiscoverCityList().size());
        return (needHead ? 1 : 0) + (myAchieve == null ? 0 : listSize);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && needHead) ? HEADER : PROGRESS;
    }

    private class HeadHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_scoreBg;
        TextView tv_score;
        TextView tv_badgeTitle;
        TextView tv_achTitle;
        TextView btn_info;
        AchieveBadge myAchCity, myAchL1, myAchL2, myAchL3;
        public HeadHolder(View itemView) {
            super(itemView);
            rl_scoreBg = (RelativeLayout) itemView.findViewById(R.id.rl_scoreBg);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            tv_badgeTitle = (TextView) itemView.findViewById(R.id.tv_badgeTitle);
            tv_achTitle = (TextView) itemView.findViewById(R.id.tv_achTitle);
            btn_info = (TextView) itemView.findViewById(R.id.btn_info);
            myAchCity = (AchieveBadge) itemView.findViewById(R.id.myAchCity);
            myAchL1 = (AchieveBadge) itemView.findViewById(R.id.myAchL1);
            myAchL2 = (AchieveBadge) itemView.findViewById(R.id.myAchL2);
            myAchL3 = (AchieveBadge) itemView.findViewById(R.id.myAchL3);
        }

        public void setBgColor(){
            rl_scoreBg.setBackgroundColor(achKind == Constants.ACH_CITY ? mContext.getResources().getColor(R.color.splash_label) : mContext.getResources().getColor(R.color.colorPrimary));
        }

        public void setTv_score(int score){
            tv_score.setText(String.valueOf(score));
        }

        public void setBadgeTitle(){
            tv_badgeTitle.setText(achKind == Constants.ACH_CITY ? "城市徽章" : "成就等级");
        }

        public void setBadge(int l1, int l2, int l3, int l4){
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> args = new HashMap<String, Object>();
                    args.put(Constants.BADGE_KIND, (int)v.getTag());
                    Map<String, View> trans = new HashMap<String, View>();
                    trans.put(mContext.getString(R.string.city_badge), v);
                    ActivityManager.startActivityWithTransAnim((Activity) mContext, MyCityActivity.class, trans, args);
                }
            };
            if(achKind == Constants.ACH_CITY){
                myAchCity.setOnClickListener(listener);
                myAchL1.setOnClickListener(listener);
                myAchL2.setOnClickListener(listener);
                myAchL3.setOnClickListener(listener);
            }
            myAchCity.setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_city : R.mipmap.ic_ach_l1);
            myAchCity.setBadgeKind(1);
            myAchCity.setBadgeNum(l1);
            myAchL1.setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_l1 : R.mipmap.ic_ach_l2);
            myAchL1.setBadgeKind(2);
            myAchL1.setBadgeNum(l2);
            myAchL2.setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_l2 : R.mipmap.ic_ach_l3);
            myAchL2.setBadgeKind(3);
            myAchL2.setBadgeNum(l3);
            myAchL3.setBadgeImg(achKind == Constants.ACH_CITY ? R.mipmap.ic_dis_l3 : R.mipmap.ic_ach_l4);
            myAchL3.setBadgeKind(4);
            myAchL3.setBadgeNum(l4);


        }

        public void setAchTitle(){
            tv_achTitle.setText(achKind == Constants.ACH_CITY ? "发现城市" : "获得成就");
        }

        public void setInfoBtn(){
            Drawable drawable = mContext.getResources().getDrawable(achKind == Constants.ACH_CITY ? R.mipmap.ic_ach_info : R.mipmap.ic_dis_info);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btn_info.setCompoundDrawables(drawable, null, null, null);
            btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    protected class AchProgressHolder extends RecyclerView.ViewHolder{
        private TextView menu_title, menu_point, tv_subAchTitle, tv_pgs;
        private ProgressBar pgs;
        private ImageView iv_next;
        private ImageView menu_icon;
        public AchProgressHolder(View itemView) {
            super(itemView);
            menu_title = (TextView) itemView.findViewById(R.id.menu_title);
            menu_point = (TextView) itemView.findViewById(R.id.menu_point);
            menu_icon = (ImageView) itemView.findViewById(R.id.menu_icon);
            tv_subAchTitle = (TextView) itemView.findViewById(R.id.tv_subAchTitle);
            tv_pgs = (TextView) itemView.findViewById(R.id.tv_pgs);
            pgs = (ProgressBar) itemView.findViewById(R.id.pgs);
            iv_next = (ImageView) itemView.findViewById(R.id.iv_next);
        }

        public void setTitle(String title){
            menu_title.setText(title);
        }

        public void setPoint(String point){
            if (TextUtils.isEmpty(point)){
                menu_point.setVisibility(GONE);
            }else {
                menu_point.setVisibility(VISIBLE);
                menu_point.setText("(".concat(point).concat(")"));
            }
        }

        public void setSubTitle(String sub){
            tv_subAchTitle.setText(sub);
        }

        public void setNeedSubTitle(boolean isNeed){
            tv_subAchTitle.setVisibility(isNeed ? VISIBLE : INVISIBLE);
        }

        public void setNeedIcon(boolean isNeed){
            menu_icon.setVisibility(isNeed ? VISIBLE : GONE);
            menu_title.setVisibility(isNeed ? GONE : VISIBLE);
        }

        public void setNeedNext(boolean isNeed){
            iv_next.setVisibility(isNeed ? VISIBLE : GONE);
        }

        public boolean canClick(){
            return iv_next.getVisibility() == VISIBLE;
        }

        public void setMenu_icon(int resId){
            setNeedIcon(true);
            menu_icon.setImageResource(resId);
        }

        public void setPgs(double percent){
            pgs.setProgress((int) percent);
            tv_pgs.setText(String.format("%.2f", percent));
        }

        public void setAchMenuClickListener(View.OnClickListener listener){
            itemView.setOnClickListener(listener);
//            btn_next.setOnClickListener(listener);
        }
    }

    public interface OnMenuClickListener{
        void onMenuClicked(String title, int kind);
    }
}
