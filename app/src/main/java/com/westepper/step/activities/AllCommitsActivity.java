package com.westepper.step.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.westepper.step.R;
import com.westepper.step.adapters.DisDetailRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.CommitEditView;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.CommitLogic;
import com.westepper.step.models.CommitModel;
import com.westepper.step.widgets.CommitGlobalLayoutListener;
import com.westepper.step.responses.Commit;

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

    DisDetailRcvAdapter adapter;
    List<Commit> commitList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            commitList = (List<Commit>) savedInstanceState.getSerializable(Constants.COMMIT_LIST);
        else if(getIntent() != null)
            commitList = (List<Commit>) getIntent().getSerializableExtra(Constants.COMMIT_LIST);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitlist);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.COMMIT_LIST, (Serializable) commitList);
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
        adapter = new DisDetailRcvAdapter(this, rcv_commitList, false);
        rcv_commitList.setAdapter(adapter);
        adapter.setCommits(commitList);
        adapter.setCommitListener(new DisDetailRcvAdapter.OnCommitListener() {
            @Override
            public void onCommit(final String id, String nickName) {
                if (!commitInput.isVisible()) {
                    commitInput.setHint(String.format("回复: %1$s", nickName));
                    showInputMethod(commitInput);
                    commitInput.setOnSendListener(new CommitEditView.OnSendListener() {
                        @Override
                        public void onSend(View focuseView, String txt) {
                            //send logic
                            CommitLogic logic = new CommitLogic(AllCommitsActivity.this, new CommitModel(id, getIntent().getStringExtra(Constants.DIS_ID), getIntent().getIntExtra(Constants.DIS_KIND, 1), txt));
                            logic.setCallbackObject(commitInput, adapter).sendRequest();
                            hideInputMethod(commitInput);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}
