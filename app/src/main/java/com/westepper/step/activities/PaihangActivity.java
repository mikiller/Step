package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.adapters.PaihangRcvAdapter;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.PaihangTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Mikiller on 2017/9/6.
 */

public class PaihangActivity extends SuperActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindViews({R.id.iv_second, R.id.iv_first, R.id.iv_third})
    SelectableRoundedImageView[] iv_headers;
    @BindViews({R.id.tv_second, R.id.tv_first, R.id.tv_third})
    PaihangTextView[] tv_headers;
    @BindView(R.id.rcv_paihang)
    RecyclerView rcv_paihang;

    PaihangRcvAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paihang);
    }

    @Override
    protected void initView() {
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        tv_headers[0].setName("一只猫");
        tv_headers[0].setDisNum(1002030);
        tv_headers[0].setAchNum(23030203);


        List<String> tmp = new ArrayList<>();
        for(int i = 0; i < 20 ; i++)
            tmp.add("");

        adapter = new PaihangRcvAdapter(tmp);
        rcv_paihang.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv_paihang.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }
}
