package com.uilib.netresdisplaylayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.uilib.R;
import com.uilib.mxselectreslayout.NoAlphaAnimator;
import com.uilib.mxselectreslayout.ResRcvAdapter;
import com.uilib.mxselectreslayout.WrapLinearLayoutManager;

import java.util.List;

/**
 * Created by Mikiller on 2016/8/24.
 */
public class NetResDisplayLayout extends LinearLayout {
    RecyclerView rcv_res;
    public NetResDisplayLayout(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public NetResDisplayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public NetResDisplayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.net_res_display_layout, this, true);
        rcv_res = (RecyclerView) findViewById(R.id.rcv_res);
        WrapLinearLayoutManager layoutMgr = new WrapLinearLayoutManager(context);
        layoutMgr.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutMgr.setAutoMeasureEnabled(true);
        rcv_res.setLayoutManager(layoutMgr);
        rcv_res.setHasFixedSize(true);
        NetResAdapter adapter = new NetResAdapter(context, null);
        rcv_res.setAdapter(adapter);
        rcv_res.setItemAnimator(new NoAlphaAnimator());
    }

    public void setItemWidth(int width){
        ((NetResAdapter)rcv_res.getAdapter()).setItemWidth(width);
    }

    public int getItemWidth(){
        return ((NetResAdapter)rcv_res.getAdapter()).getItemWidth();
    }

    public void setData(List<String> list){
        ((NetResAdapter)rcv_res.getAdapter()).setData(list);
    }

    public List<String> getData(){
        return ((NetResAdapter)rcv_res.getAdapter()).getData();
    }

    public void setOnItemClickListener(NetResAdapter.OnItemClickListener listener){
        ((NetResAdapter)rcv_res.getAdapter()).setItemClickListener(listener);
    }

    public void recyler(){
        ((NetResAdapter)rcv_res.getAdapter()).recyler();
    }
}
