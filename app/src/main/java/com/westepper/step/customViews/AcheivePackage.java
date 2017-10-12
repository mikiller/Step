package com.westepper.step.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;

import java.util.List;

/**
 * Created by Mikiller on 2017/10/12.
 */

public class AcheivePackage extends RelativeLayout {
    private LinearLayout ll_ach_package, ll_ach_items;
    private SelectableRoundedImageView iv_ach;
    private TextView tv_ach_title;
    private ImageView iv_next;
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
        tv_ach_title = (TextView) findViewById(R.id.tv_act_title);
        iv_next = (ImageView) findViewById(R.id.iv_next);

        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AcheivePackage);
            setAchIcon(ta.getDrawable(R.styleable.AcheivePackage_icon));
            setAchTitle(ta.getString(R.styleable.AcheivePackage_achTitle));
            ta.recycle();
        }

        iv_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_ach_items.setVisibility(ll_ach_items.getVisibility() == GONE ? VISIBLE : GONE);
            }
        });

    }

    public  void setAchIcon(Drawable drawable){
        iv_ach.setImageDrawable(drawable);
    }

    public void setAchTitle(String title){
        if(!TextUtils.isEmpty(title))
            tv_ach_title.setText(title);
    }

    public void addItems(List<String> items){
        for(String item : items){
            addItem(item);
        }
    }

    public void addItem(String item){
        TextView text = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_ach, null);
        text.setText(item);
        ll_ach_items.addView(text);
    }
}
