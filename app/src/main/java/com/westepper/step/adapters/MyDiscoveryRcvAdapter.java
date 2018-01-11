package com.westepper.step.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.uilib.customdialog.CustomDialog;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.activities.DiscoveryDetailActivity;
import com.westepper.step.activities.JoinUserSelectorActivity;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.logics.DeleteDiscoverLogic;
import com.westepper.step.models.DisBase;
import com.westepper.step.models.DisModel;
import com.westepper.step.models.JoinModel;
import com.westepper.step.responses.Discovery;
import com.westepper.step.responses.ImgDetail;
import com.westepper.step.responses.UserInfo;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.AnimUtils;
import com.westepper.step.utils.ContactsHelper;
import com.westepper.step.utils.MXTimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Mikiller on 2017/10/12.
 */

public class MyDiscoveryRcvAdapter extends RecyclerView.Adapter<MyDiscoveryRcvAdapter.DisHolder> {

    Context mContext;
    List<Discovery> discoveryList;
    UserInfo userInfo;
    int disKind, type;
    int[] ids = new int[]{R.id.iv_img1, R.id.iv_img2, R.id.iv_img3};

    public void setDiscoveryList(List<Discovery> discoveryList, int type) {
        this.type = type;
        this.discoveryList = discoveryList;
        notifyDataSetChanged();
    }

    public void addDiscoveryList(List<Discovery> discoveryList) {
        if (this.discoveryList == null)
            this.discoveryList = new ArrayList<>();
        int startPos = this.discoveryList.size();
        this.discoveryList.addAll(discoveryList);
        notifyItemRangeChanged(startPos, discoveryList.size());
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public MyDiscoveryRcvAdapter(Context mContext, int disKind) {
        this.mContext = mContext;
        this.disKind = disKind;
    }

    @Override
    public DisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_my_discovery, null);
        return new DisHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DisHolder holder, final int position) {
        final Discovery discovery = discoveryList.get(position);
        if (isToday(discovery.getPushTime())) {
            holder.tv_date1.setText("今天");
            holder.tv_date2.setText("");
        } else {
            holder.tv_date1.setText(getDate("dd", discovery.getPushTime()) + "/");
            holder.tv_date2.setText(getDate("M", discovery.getPushTime()));
        }
        holder.tv_msg.setText(discovery.getInfo());
        holder.tv_pos.setText(discovery.getUserPos().getPoiTitle());
        holder.tv_good.setText(String.valueOf(discovery.getGoodNum()));
        holder.tv_commit.setText(String.valueOf(discovery.getCommitNum()));
        int i = 0;
        for (ImgDetail img : discovery.getImgList()) {
            if (i >= 3)
                break;
            GlideImageLoader.getInstance().loadImage(mContext, img.getImg_url(), R.mipmap.placeholder, holder.iv_imgs[i++], 0);
        }
        while (i < 3) {
            holder.iv_imgs[i++].setImageDrawable(null);
        }
        if (disKind == Constants.OUTGO) {
            holder.ll_chat.setVisibility(View.VISIBLE);
            if (discovery.getTotalCount() == 0)
                holder.tv_joinNum.setText(String.format("邀约不限，已报名%1$s人", discovery.getJoinCount()));
            else
                holder.tv_joinNum.setText(String.format("邀约%1$s人, 已报名%2$s人", discovery.getTotalCount(), discovery.getJoinCount()));
        }

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> args = new HashMap<>();
                args.put(Constants.DISCOVERY_DETAIL, new DisBase(discovery.getDiscoveryId(), discovery.getDiscoveryKind()));
                args.put(Constants.DIS_SCOPE, Constants.FRIEND);
                ActivityManager.startActivity((Activity) mContext, DiscoveryDetailActivity.class, args);
            }
        });
        holder.translateItem((int) holder.rl_item.getTranslationX(), 0);

        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDiscovery(position, discovery);
            }
        });

        holder.tv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(discovery.getTeamId())) {
                    if (type == 1)//我发布的
                        createTeam(discovery);
                    else
                        Toast.makeText(mContext, "暂未创建群聊", Toast.LENGTH_SHORT).show();
                } else
                    getTeamMembers(discovery);
            }
        });
    }

    private String getDate(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(new Date(time));
    }

    private boolean isToday(long time) {
        String d1 = getDate("dd", System.currentTimeMillis()),
                d2 = getDate("dd", time);
        return d1.equals(d2);
    }

    private void createTeam(Discovery discovery) {
        if (discovery.getJoinCount() > 0) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put(Constants.DIS_ID, discovery.getDiscoveryId());
            //args.put(Constants.TEAM_ID, discovery.getTeamId());
            ActivityManager.startActivityforResult((Activity) mContext, JoinUserSelectorActivity.class, Constants.INVITE_TEAMMEMBER, args);
        } else {
            ContactsHelper.createAdvancedTeam(mContext, userInfo.getNickName().concat("的约行").concat(MXTimeUtils.getFormatTime("yy/MM/dd HH:mm", System.currentTimeMillis())), new ArrayList<String>(), new JoinModel(discovery.getDiscoveryId()));
        }
    }

    private void getTeamMembers(final Discovery discovery) {
        TeamDataCache.getInstance().fetchTeamMemberList(discovery.getTeamId(), new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members) {
                if (success && members != null) {
//                    getJoinUserLogic(members);
                    if (type == 1) {
                        //我发布的
                        if (discovery.getJoinCount() > 0) {
                            Map<String, Object> args = new HashMap<String, Object>();
                            args.put(Constants.DIS_ID, discovery.getDiscoveryId());
                            args.put(Constants.TEAM_ID, discovery.getTeamId());
                            args.put(Constants.TEAM_MEMBER, members);
                            ActivityManager.startActivityforResult((Activity) mContext, JoinUserSelectorActivity.class, Constants.INVITE_TEAMMEMBER, args);
                        } else {
                            NimUIKit.startTeamSession(mContext, discovery.getTeamId());
                            ((SuperActivity) mContext).back();
                        }
                    } else if (type == 2) {
                        //我报名的
                        for (TeamMember member : members) {
                            if (member.getAccount().equals(SuperActivity.userInfo.getUserId())) {
                                NimUIKit.startTeamSession(mContext, discovery.getTeamId());
                                ((SuperActivity) mContext).back();
                                return;
                            }
                        }
                        //send join request
                        joinOutGoTeam(discovery.getTeamId());
                    }
                }
            }
        });
    }

    private void joinOutGoTeam(final String teamId) {
        final CustomDialog dlg = new CustomDialog(mContext);
        dlg.setTitle("已报名参加约行").setDlgEditable(true).setDlgButtonListener(new CustomDialog.onButtonClickListener() {
            @Override
            public void onCancel() {
                ((SuperActivity) mContext).hideInputMethod(dlg.getCurrentFocus());
            }

            @Override
            public void onSure() {
                NimUIKit.applyJoinTeam(mContext, teamId, dlg.getMsg());
                ((SuperActivity) mContext).hideInputMethod(dlg.getCurrentFocus());
            }
        }).show();
    }

    private void deleteDiscovery(final int pos, final Discovery discovery){
        DisModel model = new DisModel(discovery.getDiscoveryId(), discovery.getDiscoveryKind());
        DeleteDiscoverLogic logic = new DeleteDiscoverLogic(mContext, model);
        logic.setCallback(new BaseLogic.LogicCallback() {
            @Override
            public void onSuccess(Object response) {
                discoveryList.remove(discovery);
                notifyItemRemoved(pos);
            }

            @Override
            public void onFailed(String code, String msg, Object localData) {

            }
        });
        logic.sendRequest();
    }

    @Override
    public int getItemCount() {
        return discoveryList == null ? 0 : discoveryList.size();
    }

    protected class DisHolder extends RecyclerView.ViewHolder {

        private TextView tv_date1, tv_date2, tv_msg, tv_pos, tv_good, tv_commit, tv_joinNum, tv_chat;
        private SelectableRoundedImageView[] iv_imgs = new SelectableRoundedImageView[3];
        private LinearLayout ll_chat;
        private RelativeLayout rl_item;
        private Button btn_del;

        public DisHolder(View itemView) {
            super(itemView);
            tv_date1 = (TextView) itemView.findViewById(R.id.tv_date1);
            tv_date2 = (TextView) itemView.findViewById(R.id.tv_date2);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
            tv_pos = (TextView) itemView.findViewById(R.id.tv_pos);
            tv_good = (TextView) itemView.findViewById(R.id.tv_good);
            tv_commit = (TextView) itemView.findViewById(R.id.tv_commit);
            tv_joinNum = (TextView) itemView.findViewById(R.id.tv_joinNum);
            tv_chat = (TextView) itemView.findViewById(R.id.tv_chat);
            for (int i = 0; i < 3; i++) {
                iv_imgs[i] = (SelectableRoundedImageView) itemView.findViewById(ids[i]);
            }
            ll_chat = (LinearLayout) itemView.findViewById(R.id.ll_chat);
            rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            btn_del = (Button) itemView.findViewById(R.id.btn_del);

            rl_item.setOnTouchListener(new View.OnTouchListener() {
                float x = 0, dx;
                int width = -DisplayUtil.dip2px(mContext, 50);
                boolean isMove;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = event.getX();
                            isMove = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (type != 2) {
                                dx = event.getX() - x;
                                if (dx < -100) {
                                    //左移
                                    translateItem(0, width);
                                    isMove = true;
                                } else if (dx > 100) {
                                    translateItem(width, 0);
                                    isMove = true;
                                }
                            }
                            break;
                    }
                    return isMove;
                }
            });
        }

        private void translateItem(int start, int end) {
            AnimUtils.startObjectAnim(rl_item, "translationX", start, end, 300);
        }
    }
}
