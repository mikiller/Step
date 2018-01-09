package com.westepper.step.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uilib.customdialog.CustomDialog;
import com.westepper.step.R;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.responses.Commit;
import com.westepper.step.widgets.CommitHolder;

import java.util.List;

/**
 * Created by Mikiller on 2018/1/5.
 */

public class CommentDetailAdapter extends RecyclerView.Adapter<CommitHolder> implements CommitImpl{
    private List<Commit> commits;
    private Context mContext;
    private String disUserId;

    private CommitOptLostener optListener;

    public CommentDetailAdapter(String userId) {
        this.disUserId = userId;
    }

    public CommentDetailAdapter(List<Commit> commits, String userId) {
        this(userId);
        this.commits = commits;
    }

    @Override
    public CommitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_detail, null);
        return new CommitHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommitHolder holder, int position) {
        final Commit commit = commits.get(position);

        holder.setUserInfo(mContext, commit.getUserInfo().getNickName(), commit.getUserInfo().getHeadImg());
        holder.setTv_commit(commit.getComment_content());
        holder.setTv_time(commit.getCreate_time());
        holder.setResPonseCounts(commit.getResponses_count(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubCommits(holder.getSubReplyLayout(), commit);
                v.setEnabled(false);
            }
        });
        holder.setReplyClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReplyDlg(commit, null);
            }
        });
    }

    private void addSubCommits(ViewGroup parent, final Commit commit) {
        parent.removeAllViews();
        for (final Commit.SubCommit sc : commit.getComment_responses()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_commit_reply, null);
            CommitHolder holder = new CommitHolder(view);
            holder.setTv_time(sc.getCreate_time());
            holder.setTv_commit(sc.getResponse_content());
            holder.setReplyUserInfo(mContext, sc.getFrom_user_info().getNickName(), sc.getFrom_user_info().getHeadImg(), sc.getTo_user_info().getNickName());
            holder.setReplyClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReplyDlg(commit, sc);
                }
            });
            parent.addView(holder.itemView);
        }
    }

    private void showReplyDlg(final Commit commit, final Commit.SubCommit subCommit) {
        boolean canDel = canDelete(commit, subCommit);
        int[] ids = canDel ? new int[]{R.id.btn_mood, R.id.btn_outgo} : new int[]{R.id.btn_mood};
        String[] txts = canDel ? new String[]{"回复", "删除"} : new String[]{"回复"};

        final CustomDialog dlg = new CustomDialog(mContext);
        dlg.setLayoutRes(R.layout.layout_newdis_dlg).setOnCustomBtnClickListener(new CustomDialog.onCustomBtnsClickListener() {
            @Override
            public void onBtnClick(int id) {
                dlg.dismiss();
                if (optListener == null)
                    return;
                if (id == R.id.btn_mood) {
                    optListener.onRerply(commit, subCommit);
                } else if (id == R.id.btn_outgo) {
                    optListener.onDelete(commit, subCommit);
                }
            }
        }, ids).setCustomBtnText(txts).show();
    }

    private boolean canDelete(Commit commit, Commit.SubCommit subCommit){
        boolean rst = false;
        if (SuperActivity.userInfo.getUserId().equals(disUserId))
            rst = true;
        else if (subCommit != null){
            if (subCommit.getFrom_user_info().getUserId().equals(SuperActivity.userInfo.getUserId()))
                rst = true;
        }else if(commit.getUserInfo().getUserId().equals(SuperActivity.userInfo.getUserId())) {
            rst = true;
        }
        return rst;
    }

    @Override
    public int getItemCount() {
        return commits == null ? 0 : commits.size();
    }

    @Override
    public void setCommits(List<Commit> commits) {
        this.commits = commits;
        notifyDataSetChanged();
    }

    @Override
    public void addCommit(Commit commit){
        int pos = 0;
        for (Commit c : commits){
            if (c.getCommentId().equals(commit.getCommentId())){
                commits.remove(c);
                break;
            }
            pos++;
        }
        commits.add(pos, commit);
        notifyItemChanged(pos);
    }

    public void deleteCommit(Commit commit, Commit.SubCommit subCommit){
        int pos = commits.indexOf(commit);
        if (subCommit != null){
            commits.get(pos).getComment_responses().remove(subCommit);
            commits.get(pos).setResponses_count(commits.get(pos).getResponses_count() - 1);
            notifyItemChanged(pos);
        }else {
            commits.remove(commit);
            notifyItemRemoved(pos);
        }



    }

    public void setOptListener(CommitOptLostener optListener) {
        this.optListener = optListener;
    }

    public interface CommitOptLostener {
        void onRerply(Commit commit, Commit.SubCommit subCommit);

        void onDelete(Commit commit, Commit.SubCommit subCommit);
    }
}
