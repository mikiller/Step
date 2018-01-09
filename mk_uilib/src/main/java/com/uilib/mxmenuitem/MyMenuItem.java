package com.uilib.mxmenuitem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uilib.R;


/**
 * Created by Mikiller on 2016/9/12.
 */
public class MyMenuItem extends RelativeLayout {
    private RelativeLayout menu_bg;
    private ImageView iv_menu_icon;
    private TextView tv_menu_name;
    private View bottom_line;
    public TextView tv_info;
    private ImageView iv_next;
    private View iv_notify;

    private Drawable menuIcon;
    private String menuName;
    private boolean needLine, needSubText, needNext;
    private int bgColor;

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isNeedLine() {
        return needLine;
    }

    public void setNeedLine(boolean needLine) {
        this.needLine = needLine;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
        if(!TextUtils.isEmpty(menuName))
            tv_menu_name.setText(menuName);
    }

    public Drawable getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(Drawable menuIcon) {
        this.menuIcon = menuIcon;
        iv_menu_icon.setImageDrawable(menuIcon);
    }

    public boolean isNeedNext() {
        return needNext;
    }

    public void setNeedNext(boolean needNext) {
        this.needNext = needNext;
        iv_next.setVisibility(needNext ? VISIBLE : INVISIBLE);
    }

    public boolean isNeedSubText() {
        return needSubText;
    }

    public TextView getSubTextView(){
        return tv_info;
    }

    public void setNeedSubText(boolean needSubText) {
        this.needSubText = needSubText;
        tv_info.setVisibility(needSubText ? VISIBLE : GONE);
    }

    public MyMenuItem(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MyMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MyMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.item_my_menu, this, true);
        tv_info = (TextView) findViewById(R.id.tv_info);
        iv_menu_icon = (ImageView) findViewById(R.id.iv_menu_icon);
        tv_menu_name = (TextView) findViewById(R.id.tv_menu_name);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        menu_bg = (RelativeLayout) findViewById(R.id.menu_bg);
        bottom_line = findViewById(R.id.bottom_line);
        iv_notify = findViewById(R.id.iv_notify);

        if(attrs == null)
            return;
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.MyMenuItem);
        menuIcon = type.getDrawable(R.styleable.MyMenuItem_menuIcon);
        menuName = type.getString(R.styleable.MyMenuItem_menuTitle);
        bgColor = type.getColor(R.styleable.MyMenuItem_bgColor, context.getResources().getColor(android.R.color.white));
        needLine = type.getBoolean(R.styleable.MyMenuItem_needLine, true);
        needNext = type.getBoolean(R.styleable.MyMenuItem_needNext, false);
        needSubText = type.getBoolean(R.styleable.MyMenuItem_needSubText, false);

        if(menuIcon!= null)
            iv_menu_icon.setImageDrawable(menuIcon);
        else
            iv_menu_icon.setVisibility(GONE);
        setMenuName(menuName);
        bottom_line.setVisibility(needLine ? VISIBLE : GONE);
        tv_info.setVisibility(needSubText ? VISIBLE : GONE);
        iv_next.setVisibility(needNext ? VISIBLE : INVISIBLE);
    }

    public void setSubText(String text){
        tv_info.setText(text);
    }

    public void setNeedNotify(final boolean isNeed){
        iv_notify.post(new Runnable() {
            @Override
            public void run() {
                iv_notify.setVisibility(isNeed ? VISIBLE : GONE);
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
