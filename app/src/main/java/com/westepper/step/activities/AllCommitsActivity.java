package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.westepper.step.R;
import com.westepper.step.adapters.CommentDetailAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.GetCommitDetailLogic;
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
    private String disId;
    private int disKind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            disId = savedInstanceState.getString(Constants.DIS_ID);
            disKind = savedInstanceState.getInt(Constants.DIS_KIND);
        }else if(getIntent() != null){
            disId = getIntent().getStringExtra(Constants.DIS_ID);
            disKind = getIntent().getIntExtra(Constants.DIS_KIND, 1);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitlist);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.DIS_ID, disId);
        outState.putInt(Constants.DIS_KIND, disKind);
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
        //adapter = new DisDetailRcvAdapter(this, rcv_commitList, false);

//        adapter.setCommits(commitList);
//        adapter.setCommitListener(new DisDetailRcvAdapter.OnCommitListener() {
//            @Override
//            public void onCommit(final String id, String nickName) {
//                if (!commitInput.isVisible()) {
//                    commitInput.setHint(String.format("回复: %1$s", nickName));
//                    showInputMethod(commitInput);
//                    commitInput.setOnSendListener(new CommitEditView.OnSendListener() {
//                        @Override
//                        public void onSend(View focuseView, String txt) {
//                            //send logic
//                            CommitLogic logic = new CommitLogic(AllCommitsActivity.this, new CommitModel(id, getIntent().getStringExtra(Constants.DIS_ID), getIntent().getIntExtra(Constants.DIS_KIND, 1), txt));
//                            logic.setCallbackObject(commitInput, adapter).sendRequest();
//                            hideInputMethod(commitInput);
//                        }
//                    });
//                }
//            }
//        });
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
                adapter = new CommentDetailAdapter(response.getComment_list());
                rcv_commitList.setAdapter(adapter);
            }

            @Override
            public void onFailed(String code, String msg, CommitList localData) {

            }
        });
        logic.sendRequest();
    }
}
