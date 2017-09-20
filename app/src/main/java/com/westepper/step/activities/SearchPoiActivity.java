package com.westepper.step.activities;

import android.content.Intent;
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

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.googlecode.mp4parser.authoring.Edit;
import com.westepper.step.R;
import com.westepper.step.adapters.PoiRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.fragments.MapFragment;
import com.westepper.step.utils.ActivityManager;
import com.westepper.step.utils.MapUtils;

import java.util.List;
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
    PoiSearch.OnPoiSearchListener searchListener;
    PoiItem poiItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            poiItem = savedInstanceState.getParcelable(Constants.POIITEM);
        else if(getIntent() != null){
            poiItem = getIntent().getParcelableExtra(Constants.POIITEM);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.POIITEM, poiItem);
        super.onSaveInstanceState(outState);
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
                mapUtils.searchPoi(s.toString(), poiItem.getCityName(), searchListener);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rcv_poi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PoiRcvAdapter();
        adapter.setChoosedItem(poiItem);
        adapter.setItemClickListener(new PoiRcvAdapter.onItemClickListener() {
            @Override
            public void onItemClick(PoiItem item) {
                Intent intent = new Intent();
                intent.putExtra(Constants.POIITEM, item);
                setResult(RESULT_OK, intent);
                back();
            }
        });
        rcv_poi.setAdapter(adapter);

        searchListener = new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int rstCode) {
                if(rstCode != AMapException.CODE_AMAP_SUCCESS)
                    return;
                poiResult.getPois().add(0, new PoiItem("default", null, "上海市", ""));
                adapter.setDatas(poiResult.getPois());
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        };
    }

    @Override
    protected void initData() {
        mapUtils.searchPoi("", poiItem == null ? "上海市" : poiItem.getCityName(), searchListener);
    }
}
