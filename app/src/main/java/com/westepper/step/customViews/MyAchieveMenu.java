package com.westepper.step.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.westepper.step.R;

/**
 * Created by Mikiller on 2017/10/20.
 */

public class MyAchieveMenu extends RelativeLayout {
    private TextView menu_title, tv_subAchTitle, tv_pgs;
    private ProgressBar pgs;
    private ImageButton btn_next;
    private ImageView menu_icon;
    public MyAchieveMenu(Context context) {
        this(context, null, 0);
    }

    public MyAchieveMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAchieveMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_myach_menu, this);
        menu_title = (TextView) findViewById(R.id.menu_title);
        menu_icon = (ImageView) findViewById(R.id.menu_icon);
        tv_subAchTitle = (TextView) findViewById(R.id.tv_subAchTitle);
        tv_pgs = (TextView) findViewById(R.id.tv_pgs);
        pgs = (ProgressBar) findViewById(R.id.pgs);
        btn_next = (ImageButton) findViewById(R.id.btn_next);

        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyAchieveMenu);
            if(!TextUtils.isEmpty(ta.getString(R.styleable.MyAchieveMenu_achMenuTitle)))
                setTitle(ta.getString(R.styleable.MyAchieveMenu_achMenuTitle));
            if(!TextUtils.isEmpty(ta.getString(R.styleable.MyAchieveMenu_achMenuSubTitle)))
                setSubTitle(ta.getString(R.styleable.MyAchieveMenu_achMenuSubTitle));
            ta.recycle();
        }

        setPgs(0);
    }

    public void setTitle(String title){
        menu_title.setText(title);
    }

    public void setSubTitle(String sub){
        tv_subAchTitle.setText(sub);
    }

    public void setNeedSubTitle(boolean isNeed){
        tv_subAchTitle.setVisibility(isNeed ? VISIBLE : INVISIBLE);
    }

    public void setNeedIcon(boolean isNeed){
        menu_icon.setVisibility(isNeed ? VISIBLE : GONE);
        menu_title.setVisibility(isNeed ? GONE : VISIBLE);
    }

    public void setNeedNext(boolean isNeed){
        btn_next.setVisibility(isNeed ? VISIBLE : GONE);
    }

    public void setMenu_icon(int resId){
        setNeedIcon(true);
        menu_icon.setImageResource(resId);
    }

    public void setPgs(int percent){
        pgs.setProgress(percent);
        tv_pgs.setText(percent + "%");
        tv_pgs.setTextColor(percent > 0 ? getResources().getColor(R.color.text_color_black) : getResources().getColor(R.color.discovery_kind_unchecked));
    }

    public void setAchMenuClickListener(OnClickListener listener){
        btn_next.setOnClickListener(listener);
    }
}
