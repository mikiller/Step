package com.uilib.mxgallery.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.uilib.R;
import com.uilib.mxgallery.listeners.GalleryTabListener;

import java.util.ArrayList;

/**
 * Created by Mikiller on 2017/5/11.
 */

public class GalleryTabGroup extends RelativeLayout {
    private RadioGroup rdg_tab;
    private View tabLine;

    private ArrayList<String> tabNames;
    private int screenWidth, lastId = 0;

    private GalleryTabListener tabListener;

    public GalleryTabGroup(Context context) {
        super(context);
        initView(context, null);
    }

    public GalleryTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.layout_tab_group, this, true);
        rdg_tab = (RadioGroup) findViewById(R.id.rdg_tab);
        rdg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = rdg_tab.getChildCount() == 0 ? 1 : rdg_tab.getChildCount();
                int lastPos = screenWidth / count * lastId,
                        currentPos = screenWidth / count * checkedId;
                anim(lastPos, currentPos);
                lastId = checkedId;
                if(tabListener != null && group.getCheckedRadioButtonId() == checkedId){
                    tabListener.onTabChecked((RadioButton) group.findViewById(checkedId), checkedId);
                }
            }
        });
        tabLine = findViewById(R.id.view_tabline);
    }

    public void setTabNames(GalleryTabListener listener, String... names){
        if(names == null || names.length == 0) {
            return;
        }
        tabNames = new ArrayList<>();
        rdg_tab.removeAllViews();
        addTabToGroup(listener, lastId, names);
    }

    public void addTabNames(GalleryTabListener listener, String... names){
        if(names == null || names.length == 0)
            return;
        addTabToGroup(listener, rdg_tab.getChildCount(), names);
    }

    private void addTabToGroup(GalleryTabListener listener, int id, String... names){
        for(String name : names){
            tabNames.add(name);
            RadioButton rdbTab = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.layout_tab, null);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            rdbTab.setLayoutParams(lp);
            rdbTab.setText(name);
            rdbTab.setId(id++);
            rdg_tab.addView(rdbTab);
        }
        RelativeLayout.LayoutParams lp = (LayoutParams) tabLine.getLayoutParams();
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        int count = rdg_tab.getChildCount() == 0 ? 1 : rdg_tab.getChildCount();
        lp.width = screenWidth / count;
        lp.leftMargin = screenWidth / count * rdg_tab.getCheckedRadioButtonId();
        tabLine.setLayoutParams(lp);
        rdg_tab.check(lastId = id - 1);
        tabListener = listener;
    }

    public void setTabName(int id, String name){
        ((RadioButton)rdg_tab.findViewById(id)).setText(name);
    }

    public void updateTab(int itemCount){
        if(tabListener != null)
            tabListener.onTabUpdated(this, rdg_tab.getCheckedRadioButtonId(), itemCount);
    }

    public void checkTab(int tab){
        rdg_tab.check(tab);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void anim(int last, int current){
        ValueAnimator marginAnim = ValueAnimator.ofInt(last, current).setDuration(300);
        marginAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        marginAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams lp = (LayoutParams) tabLine.getLayoutParams();
                lp.leftMargin = (int) animation.getAnimatedValue();
                tabLine.setLayoutParams(lp);
            }
        });
        marginAnim.start();
    }

}
