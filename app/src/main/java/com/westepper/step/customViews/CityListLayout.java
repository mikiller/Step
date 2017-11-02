package com.westepper.step.customViews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.westepper.step.R;
import com.westepper.step.adapters.CityAdapter;
import com.westepper.step.models.CityBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/2.
 */

public class CityListLayout extends LinearLayout {
    RadioGroup rdg_mainCity;
    RecyclerView rcv_city;
    IndexBar indexBar;

    List<CityBean> mDatas;
    CityAdapter adapter;
    LinearLayoutManager llMgr;

    onSelectedCityListener listener;

    public CityListLayout(Context context) {
        super(context, null, 0);
    }

    public CityListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CityListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_citylist, this);
        rdg_mainCity = (RadioGroup) findViewById(R.id.rdg_mainCity);
        rcv_city = (RecyclerView) findViewById(R.id.rcv_city);
        indexBar = (IndexBar) findViewById(R.id.indexBar);
        rdg_mainCity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(listener != null)
                    listener.onCitySelected(((RadioButton)group.findViewById(checkedId)).getText().toString());
            }
        });

        rcv_city.setLayoutManager(llMgr = new LinearLayoutManager(context));

        getCitysFromFile();
    }

    private void getCitysFromFile(){
        try {
            InputStream is = getContext().getAssets().open("city.txt");
            byte[] txt = new byte[is.available()];
            is.read(txt);
            String cityjson = new String(txt);
            mDatas = new Gson().fromJson(cityjson, new TypeToken<List<CityBean>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        indexBar.setmLayoutManager(llMgr).setNeedRealIndex(true).setmSourceDatas(mDatas).invalidate();
        String lastTag = "";
        for(CityBean city : mDatas){
            if(!city.getBaseIndexTag().equals(lastTag)){
                city.setTop(true);
                lastTag = city.getBaseIndexTag();
            }
        }
        adapter = new CityAdapter(getContext(), mDatas);
        adapter.setListener(new CityAdapter.CityClickListener() {
            @Override
            public void onCityClick(String city) {
                if(listener != null)
                    listener.onCitySelected(city);
            }
        });
        rcv_city.setAdapter(adapter);
    }

    public void setOnSelectedCityListener(onSelectedCityListener listener){
        this.listener = listener;
    }

    public interface onSelectedCityListener{
        void onCitySelected(String city);
    }
}
