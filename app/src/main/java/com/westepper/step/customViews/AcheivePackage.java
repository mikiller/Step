package com.westepper.step.customViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;
import com.westepper.step.responses.Achieve;
import com.westepper.step.responses.AchieveArea;
import com.westepper.step.utils.AnimUtils;

import java.util.List;

/**
 * Created by Mikiller on 2017/10/12.
 */

public class AcheivePackage extends LinearLayout {
    private LinearLayout ll_ach_package, ll_ach_items;
    private SelectableRoundedImageView iv_ach;
    private TextView tv_ach_title;
    private ImageView iv_next;

    private Achieve achieve;
    private AchieveClickListener achieveClickListener;
    private int itemsHeight = 0;
    public AcheivePackage(Context context) {
        this(context, null, 0);
    }

    public AcheivePackage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AcheivePackage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_ach_package, this);
        ll_ach_package = (LinearLayout) findViewById(R.id.ll_ach_package);
        ll_ach_items = (LinearLayout) findViewById(R.id.ll_ach_items);
        iv_ach = (SelectableRoundedImageView) findViewById(R.id.iv_ach);
        tv_ach_title = (TextView) findViewById(R.id.tv_ach_title);
        iv_next = (ImageView) findViewById(R.id.iv_next);

        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AcheivePackage);
            setAchIcon(ta.getDrawable(R.styleable.AcheivePackage_icon));
            setAchTitle(ta.getString(R.styleable.AcheivePackage_achTitle));
            ta.recycle();
        }

        ll_ach_package.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(achieveClickListener != null)
                    achieveClickListener.onPackageClick(AcheivePackage.this);

            }
        });

    }

    public void toggleAchieveItems(){
        if(ll_ach_items.getVisibility() == GONE) {
            showAchieveItems();
        }else{
            hideAchieveItems();
        }
    }

    public void showAchieveItems(){
        if(ll_ach_items.getVisibility() == VISIBLE)
            return;
        ll_ach_items.setVisibility(VISIBLE);
        final LinearLayout.LayoutParams lp = (LayoutParams) getLayoutParams();
        AnimUtils.startValueAnim(ll_ach_package.getMeasuredHeight(), ll_ach_package.getMeasuredHeight() + itemsHeight, 300, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                lp.height = (int)value;
                setLayoutParams(lp);
            }
        });
        AnimUtils.startRotateAnim(iv_next, 0, -90, 300);
    }

    public void hideAchieveItems(){
        if(ll_ach_items.getVisibility() == GONE)
            return;
        final LinearLayout.LayoutParams lp = (LayoutParams) getLayoutParams();
        AnimUtils.startValueAnim(ll_ach_package.getMeasuredHeight() + itemsHeight, ll_ach_package.getMeasuredHeight(), 300, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                lp.height = (int)value;
                setLayoutParams(lp);
                if(value == ll_ach_package.getMeasuredHeight())
                    ll_ach_items.setVisibility(GONE);
            }
        });
        AnimUtils.startRotateAnim(iv_next, -90, 0, 300);
    }

    public  void setAchIcon(Drawable drawable){
        iv_ach.setImageDrawable(drawable);
    }

    public void setAchTitle(String title){
        if(!TextUtils.isEmpty(title))
            tv_ach_title.setText(title);
    }

    public String getTitle(){
        return achieve.getAchieveKind();
    }

    public void setAchieve(Achieve achieve){
        this.achieve = achieve;
        setAchTitle(achieve.getAchieveKind());
        addAchieveAreas(achieve.getAchieveAreaList());
    }

    public void addAchieveAreas(List<AchieveArea> achieveAreas){
        for(AchieveArea area: achieveAreas){
            addAchieveArea(area);
        }
    }

    public void addAchieveArea(final AchieveArea achieveArea){
        final RadioButton text = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.item_ach, null);
        text.setText(achieveArea.getAchieveAreaName());
        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(achieveClickListener != null)
                    achieveClickListener.onAchieveAreaClick(text, achieveArea.getAreaIds(), achieveArea.getAchieveAreaName());
            }
        });
        ll_ach_items.addView(text);
        ll_ach_items.measure(0,0);
        ll_ach_items.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                itemsHeight = ll_ach_items.getMeasuredHeight();
                ll_ach_items.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

    }

    public AchieveClickListener getAchieveClickListener() {
        return achieveClickListener;
    }

    public void setAchieveClickListener(AchieveClickListener achieveClickListener) {
        this.achieveClickListener = achieveClickListener;
    }

    public interface AchieveClickListener{
        void onPackageClick(AcheivePackage pkg);
        void onAchieveAreaClick(RadioButton item, String[] areaId, String achieveKind);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AcheivePackage)
            return getTitle().equals(((AcheivePackage)obj).getTitle());
        else
            return false;
    }
}
