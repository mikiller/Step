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
import com.westepper.step.customViews.TitleBar;
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
    @BindView(R.id.rl_commitInput)
    RelativeLayout rl_commitInput;
    @BindView(R.id.edt_commit)
    EditText edt_commit;
    @BindView(R.id.btn_send)
    Button btn_send;

    CommitGlobalLayoutListener glListener;

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
        adapter = new DisDetailRcvAdapter(this, false);
        rcv_commitList.setAdapter(adapter);
        adapter.setCommits(commitList);
        adapter.setCommitListener(new DisDetailRcvAdapter.OnCommitListener() {
            @Override
            public void onCommit(String id, String nickName) {
                if (rl_commitInput.getVisibility() == View.GONE) {
                    glListener.setCommitHint(nickName);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(glListener = new CommitGlobalLayoutListener(this, rl_commitInput, edt_commit, btn_send));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(glListener);
        } else {
            getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(glListener);
        }
        super.onDestroy();
    }
}
