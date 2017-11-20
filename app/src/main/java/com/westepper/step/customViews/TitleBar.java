package com.westepper.step.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.utils.AnimUtils;

/**
 * Created by Mikiller on 2017/9/12.
 */

public class TitleBar extends RelativeLayout {
    public final static int FORK = 1, ARROW = 2, NORMAL = 0, MENU = 1, HEADER = 2, NONE = 0, IMG = 1, TXT = 2;
    private View view_bg;
    private ImageButton btn_back, btn_more;
    private TextView tv_menu, tv_act_title, tv_act_sure;
    private LinearLayout ll_menu, ll_title;
    private ImageView iv_menu;
    private SelectableRoundedImageView iv_header;

    private TitleListener listener;

    private int backStyle = ARROW, titleStyle = NORMAL, subStyle = NONE;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_title, this, true);
        view_bg = findViewById(R.id.view_bg);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_more = (ImageButton) findViewById(R.id.btn_more);
        tv_menu = (TextView) findViewById(R.id.tv_menu);
        tv_act_title = (TextView) findViewById(R.id.tv_act_title);
        tv_act_sure = (TextView) findViewById(R.id.tv_act_sure);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_header = (SelectableRoundedImageView) findViewById(R.id.iv_header);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        if(ta != null){
            setBgColor(ta.getColor(R.styleable.TitleBar_titleBgColor, getResources().getColor(R.color.colorPrimary)));
//            view_bg.setBackgroundColor(ta.getColor(R.styleable.TitleBar_titleBgColor, getResources().getColor(R.color.colorPrimary)));
            backStyle = ta.getInt(R.styleable.TitleBar_backStyle, ARROW);
            btn_back.setVisibility(backStyle == NONE ? GONE : VISIBLE);
            btn_back.setImageResource(backStyle == ARROW ? R.mipmap.ic_back : R.mipmap.ic_close1);
            setSubStyle(ta.getInt(R.styleable.TitleBar_subStyle, NONE));
            titleStyle = ta.getInt(R.styleable.TitleBar_titleStyle, NORMAL);
            ll_menu.setVisibility(titleStyle == MENU ? VISIBLE : GONE);
            ll_title.setVisibility(titleStyle == MENU ? GONE : VISIBLE);
            iv_header.setVisibility(titleStyle == HEADER ? VISIBLE : GONE);

            setTitle(ta.getString(R.styleable.TitleBar_titleTxt));
            setSubTxt(ta.getString(R.styleable.TitleBar_subTxt));

            ta.recycle();
        }

        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onBackClicked();
            }
        });

        btn_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onMoreClicked();
            }
        });

        tv_act_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onSubClicked();
            }
        });

        ll_menu.setOnClickListener(new OnClickListener() {
            float fromRotat = iv_menu.getRotation(), toRotat = (fromRotat + 180) % 360;
            @Override
            public void onClick(View v) {
                AnimUtils.startObjectAnim(iv_menu, "rotation", fromRotat, toRotat, 500);
                if(listener != null)
                    listener.onMenuChecked(fromRotat < toRotat);
                float tmp = fromRotat;
                fromRotat = toRotat;
                toRotat = tmp;
            }
        });
    }

    public void setTitle(String title){
        if(!TextUtils.isEmpty(title))
            tv_act_title.setText(title);
    }

    public void setSubStyle(int style){
        subStyle = style;
        btn_more.setVisibility(subStyle == IMG ? VISIBLE : GONE);
        tv_act_sure.setVisibility(subStyle == TXT ? VISIBLE : GONE);

    }

    public void setSubTxt(String subTxt){
        if(!TextUtils.isEmpty(subTxt))
            tv_act_sure.setText(subTxt);
    }

    public void setBackImg(int resId){
        btn_back.setImageResource(resId);
    }

    public void setSubImg(int resId){
        btn_more.setImageResource(resId);
    }

    public void setSubTxtEnabled(boolean enabled){
        tv_act_sure.setEnabled(enabled);
    }

    public void setHeader(String path){
        GlideImageLoader.getInstance().loadImage(getContext(), path, R.mipmap.ic_default_head, iv_header, 0);
    }

    public void setBgColor(int color){
        view_bg.setBackgroundColor(color);
    }

    public void setBgAlpha(float alpha){
        view_bg.setAlpha(alpha);
    }

    public float getBgAlpha(){
        return view_bg.getAlpha();
    }

    public View getTitleBg(){
        return view_bg;
    }

    public void setTitleListener(TitleListener listener) {
        this.listener = listener;
    }

    public void callMenuCheck(){
        ll_menu.performClick();
    }

    public static abstract class TitleListener{
        protected void onBackClicked(){}
        protected void onSubClicked(){}
        protected void onMoreClicked(){}
        protected void onMenuChecked(boolean isChecked){}
    }
}
