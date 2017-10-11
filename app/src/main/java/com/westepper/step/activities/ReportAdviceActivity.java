package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.mp4parser.authoring.Edit;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/28.
 */

public class ReportAdviceActivity extends SuperActivity {
    public final static int REPORT = 1, ADVICE = 0, FEEDBACK = 2;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rl_advice)
    RelativeLayout rl_advice;
    @BindView(R.id.ll_report)
    LinearLayout ll_report;
    @BindView(R.id.edt_advice)
    EditText edt_advice;
    @BindView(R.id.tv_adviceNum)
    TextView tv_adviceNum;
    @BindView(R.id.rdg_reasons)
    RadioGroup rdg_reasons;
    @BindView(R.id.edt_reason)
    EditText edt_reason;
    @BindView(R.id.btn_commit)
    Button btn_commit;

    int kind, txtNum = 140;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            kind = savedInstanceState.getInt(Constants.ISREPORT);
        }else if(getIntent() != null)
            kind = getIntent().getIntExtra(Constants.ISREPORT, REPORT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_advice);
    }

    @Override
    protected void initView() {
        rl_advice.setVisibility(kind != REPORT ? View.VISIBLE : View.GONE);
        ll_report.setVisibility(kind == REPORT ? View.VISIBLE : View.GONE);
        titleBar.setTitle(kind == REPORT ? "举报" : (kind == ADVICE ? "报错反馈" : "意见反馈"));
        titleBar.setSubStyle(kind == REPORT ? TitleBar.NONE : TitleBar.TXT);
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onSubClicked() {

            }
        });

        edt_advice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                titleBar.setSubTxtEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_adviceNum.setText(String.valueOf(txtNum - s.length()));
            }
        });
    }

    @Override
    protected void initData() {

    }
}
