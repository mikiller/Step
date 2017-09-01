package com.uilib.checkabletag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.uilib.R;

/**
 * Created by Mikiller on 2016/8/8.
 */
public class CheckableTag extends LinearLayout implements View.OnClickListener{
    private boolean isChecked;
    private boolean needClose;
    private String text;

    private LinearLayout ll_background;
    private TextView tv_tag;
    private ImageView iv_close;

    private onCloseListener listener;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isNeedClose() {
        return needClose;
    }

    public void setNeedClose(boolean needClose) {
        this.needClose = needClose;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
        invalidate();
    }

    public onCloseListener getListener() {
        return listener;
    }

    public void setListener(onCloseListener listener) {
        this.listener = listener;
    }

    public CheckableTag(Context context) {
        super(context);
        initParam(context, null, 0);
    }

    public CheckableTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParam(context, attrs, 0);
    }

    public CheckableTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParam(context, attrs, defStyleAttr);
    }

    private void initParam(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableTag);
        if(typedArray != null){
            isChecked = typedArray.getBoolean(R.styleable.CheckableTag_isChecked, false);
            needClose = typedArray.getBoolean(R.styleable.CheckableTag_needClose, true);
            text = typedArray.getString(R.styleable.CheckableTag_text);
        }
        setWillNotDraw(false);
        LayoutInflater.from(context).inflate(R.layout.tag_textview_layout, this, true);
        ll_background = (LinearLayout) findViewById(R.id.ll_tag);
        if(isClickable())
            ll_background.setOnClickListener(this);
        tv_tag = (TextView) findViewById(R.id.tv_tag);
        if(!TextUtils.isEmpty(text))
            tv_tag.setText(text);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!TextUtils.isEmpty(text)) {
            tv_tag.setText(text);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ll_background.setBackgroundResource(isChecked ? R.drawable.shape_tag_textview_blue : R.drawable.shape_tag_textview_gray);
        tv_tag.setTextColor(getResources().getColor(isChecked ? R.color.tagTextBlue : R.color.tagBgGray));
        iv_close.setVisibility(needClose ? VISIBLE : GONE);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ll_tag){
            setChecked(!isChecked);
        }else if(v.getId() == R.id.iv_close){
            if(listener != null)
                listener.onClose(this);
        }
    }

    public interface onCloseListener{
        void onClose(View v);
    }

    public static class CheckableTagBuilder{
        Context mContext;
        boolean isChecked;
        boolean needClose;
        String text;
        onCloseListener listener;
        ViewGroup.MarginLayoutParams layoutParams;

        public CheckableTagBuilder(Context context) {
            mContext = context;
        }

        public CheckableTagBuilder setIsChecked(boolean checked){
            isChecked = checked;
            return this;
        }

        public CheckableTagBuilder setNeedClose(boolean needClose){
            this.needClose = needClose;
            return this;
        }

        public CheckableTagBuilder setText(String text){
            this.text = text;
            return this;
        }

        public CheckableTagBuilder setOnCloseListener(onCloseListener listener){
            this.listener = listener;
            return this;
        }

        public CheckableTagBuilder setLayoutParams(ViewGroup.MarginLayoutParams lp){
            layoutParams = lp;
            return this;
        }

        public CheckableTag build(){
            CheckableTag tag = new CheckableTag(mContext);
            tag.setChecked(isChecked);
            tag.setNeedClose(needClose);
            tag.setText(text);
            tag.setTag(text);
            if(listener != null)
                tag.setListener(listener);
            if(layoutParams == null) {
                layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(4, 4, 4, 4);
            }
            tag.setLayoutParams(layoutParams);
            return tag;
        }
    }

}
