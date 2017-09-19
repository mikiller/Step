package com.westepper.step.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.googlecode.mp4parser.authoring.Edit;
import com.westepper.step.R;
import com.westepper.step.adapters.PoiRcvAdapter;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.fragments.MapFragment;
import com.westepper.step.utils.MapUtils;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/19.
 */

public class SearchPoiActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tv_search_hint)
    TextView tv_search_hint;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.rcv_poi)
    RecyclerView rcv_poi;

    MapUtils mapUtils;
    PoiRcvAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
    }

    @Override
    protected void initView() {
        mapUtils = MapUtils.getInstance();
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_search_hint.setVisibility(TextUtils.isEmpty(s.toString()) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rcv_poi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PoiRcvAdapter();
        rcv_poi.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        mapUtils.searchPoi(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                adapter.setDatas(poiResult.getPois());
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
    }
}
