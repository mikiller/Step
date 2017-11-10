package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RadioGroup;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.adapters.PaihangRcvAdapter;
import com.westepper.step.base.BaseLogic;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.PaihangTextView;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.RankListLogic;
import com.westepper.step.models.RankModel;
import com.westepper.step.responses.RankList;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Mikiller on 2017/9/6.
 */

public class PaihangActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rdg_title)
    RadioGroup rdg_title;
    @BindViews({R.id.iv_first, R.id.iv_second, R.id.iv_third})
    SelectableRoundedImageView[] iv_headers;
    @BindViews({R.id.tv_first, R.id.tv_second, R.id.tv_third})
    PaihangTextView[] tv_headers;
    @BindView(R.id.rcv_paihang)
    RecyclerView rcv_paihang;

    PaihangRcvAdapter adapter;
    RankList allRankList, friendRankList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            allRankList = (RankList) savedInstanceState.getSerializable(Constants.RANKLIST2);
            friendRankList = (RankList) savedInstanceState.getSerializable(Constants.RANKLIST1);
        } else if (getIntent() != null) {
            allRankList = (RankList) getIntent().getSerializableExtra(Constants.RANKLIST2);
            friendRankList = (RankList) getIntent().getSerializableExtra(Constants.RANKLIST1);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paihang);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.RANKLIST2, allRankList);
        outState.putSerializable(Constants.RANKLIST1, friendRankList);
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

        adapter = new PaihangRcvAdapter();
        rcv_paihang.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv_paihang.setAdapter(adapter);
        updateRankList(friendRankList);
        rdg_title.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rdb_all) {
                    if(allRankList == null)
                        getRankListLogic(Constants.RANK_ALL);
                    else
                        updateRankList(allRankList);

                }else{
                    updateRankList(friendRankList);
                }

            }
        });
    }

    private void updateRankList(RankList rankList) {
        int size = rankList.getRankList().size();
        for (int i = 0; i < 3; i++) {
            tv_headers[i].setName(i < size ? rankList.getRankList().get(i).getNickName() : "虚位以待");
            tv_headers[i].setDisNum(i < size ? Long.parseLong(rankList.getRankList().get(i).getReachecNum()) : 0);
            tv_headers[i].setAchNum(i < size ? Long.parseLong(rankList.getRankList().get(i).getAchievedNum()) : 0);
            if (i < size) {
                GlideImageLoader.getInstance().loadImage(this, rankList.getRankList().get(i).getHeadUrl(), R.mipmap.ic_default_head, iv_headers[i], 0);
                Log.e(TAG, "i: " + i + ", url: " + rankList.getRankList().get(i).getHeadUrl());
            } else {
                iv_headers[i].setImageResource(R.mipmap.ic_default_head);
            }
        }
        adapter.setDatas(rankList.getUserRank(), rankList.getRankList());
    }

    private void getRankListLogic(int type) {
        RankListLogic logic = new RankListLogic(this, new RankModel(type));
        logic.setCallback(new BaseLogic.LogicCallback<RankList>() {
            @Override
            public void onSuccess(RankList response) {
                allRankList = response;
                updateRankList(allRankList);
            }

            @Override
            public void onFailed(String code, String msg, RankList localData) {

            }
        });
        logic.sendRequest();
    }

    @Override
    protected void initData() {

    }
}
