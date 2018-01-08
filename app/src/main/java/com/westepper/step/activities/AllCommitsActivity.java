package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.westepper.step.R;
import com.westepper.step.adapters.CommentDetailAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.CommitLogic;
import com.westepper.step.logics.GetCommitDetailLogic;
import com.westepper.step.models.CommitModel;
import com.westepper.step.models.DisBase;
import com.westepper.step.responses.Commit;
import com.westepper.step.responses.CommitList;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/10/10.
 */

public class AllCommitsActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rcv_commitList)
    RecyclerView rcv_commitList;
    @BindView(R.id.commitInput)
    CommitEditView commitInput;

//    DisDetailRcvAdapter adapter;
    CommentDetailAdapter adapter;
    private String disId, disUserId;
    private int disKind;
    private String commitId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            disId = savedInstanceState.getString(Constants.DIS_ID);
            disKind = savedInstanceState.getInt(Constants.DIS_KIND);
            disUserId = savedInstanceState.getString(Constants.DISUSER_ID);
        }else if(getIntent() != null){
            disId = getIntent().getStringExtra(Constants.DIS_ID);
            disKind = getIntent().getIntExtra(Constants.DIS_KIND, 1);
            disUserId = getIntent().getStringExtra(Constants.DISUSER_ID);
            commitId = getIntent().getStringExtra(Constants.COMMIT_ID);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitlist);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DIS_ID, disId);
        outState.putInt(Constants.DIS_KIND, disKind);
        outState.putString(Constants.DISUSER_ID, disUserId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }
        });
        rcv_commitList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv_commitList.setAdapter(adapter = new CommentDetailAdapter(disUserId));
        adapter.setOptListener(new CommentDetailAdapter.CommitOptLostener() {
            @Override
            public void onRerply(Commit commit, Commit.SubCommit subCommit) {
                showCommitEdit(commit, subCommit);
            }

            @Override
            public void onDelete(Commit commit, Commit.SubCommit subCommit) {

            }
        });
    }

    private void showCommitEdit(final Commit commit, final Commit.SubCommit subCommit){
        commitInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                commitInput.setHint("回复:".concat(subCommit == null ? commit.getUserInfo().getNickName() : subCommit.getFrom_user_info().getNickName()));
                showInputMethod(commitInput);
                commitInput.setOnSendListener(new CommitEditView.OnSendListener() {
                    @Override
                    public void onSend(View focuseView, String txt) {
                        CommitModel model = new CommitModel(commit.getComment_blog_id(), commit.getComment_blog_kind());
                        model.setCommentId(commit.getCommentId());
                        model.setCommitUserId(subCommit == null ? commit.getUserInfo().getUserId() : subCommit.getFrom_user_info().getUserId());
                        if (subCommit != null)
                            model.setResponseId(subCommit.getId());
                        model.setMsg(txt);
                        model.setTime(System.currentTimeMillis());
                        CommitLogic logic = new CommitLogic(AllCommitsActivity.this, model).setCallbackObject(commitInput, adapter);
                        logic.setCallback(new BaseLogic.LogicCallback<Commit>() {
                            @Override
                            public void onSuccess(Commit response) {

                            }

                            @Override
                            public void onFailed(String code, String msg, Commit localData) {

                            }
                        });
                        logic.sendRequest();
                    }
                });
            }
        }, 200);

    }

    @Override
    protected void initData() {
        getAllComments();
    }

    private void getAllComments(){
        GetCommitDetailLogic logic = new GetCommitDetailLogic(this, new DisBase(disId, disKind));
        logic.setCallback(new BaseLogic.LogicCallback<CommitList>() {
            @Override
            public void onSuccess(CommitList response) {
                adapter.setCommits(response.getComment_list());

            }

            @Override
            public void onFailed(String code, String msg, CommitList localData) {

            }
        });
        logic.sendRequest();
    }
}
