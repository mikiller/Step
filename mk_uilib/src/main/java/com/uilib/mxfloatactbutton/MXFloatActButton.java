package com.uilib.mxfloatactbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.uilib.R;
import com.uilib.utils.DisplayUtil;

/**
 * Created by Mikiller on 2016/8/12.
 */
public class MXFloatActButton extends FloatingActionButton{
    private final int LEFT_TOP = 0, RIGHT_TOP = 1, LEFT_BOTTOM = 2, RIGHT_BOTTOM = 3;

    private String srcText;
    private int textSize;
    private int textMargin;
    private int textColor;
    private int textGravity;
    private boolean isSelected;

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
//        invalidate();
    }

    public String getSrcText() {
        return srcText;
    }

    public void setSrcText(String srcText) {
        this.srcText = srcText;
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextMargin() {
        return textMargin;
    }

    public void setTextMargin(int textMargin) {
        this.textMargin = textMargin;
    }

    public MXFloatActButton(Context context) {
        super(context);
        initParams(context, null, 0);
    }

    public MXFloatActButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs, 0);
    }

    public MXFloatActButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs, defStyleAttr);
    }

    private void initParams(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MXFloatActButton);
        if(typedArray != null){
            srcText = typedArray.getString(R.styleable.MXFloatActButton_srcText);
            textSize = (int) typedArray.getDimension(R.styleable.MXFloatActButton_txtSize, 40);
            textMargin = DisplayUtil.px2dip(context, typedArray.getDimension(R.styleable.MXFloatActButton_textMargin, 10));
            textColor = typedArray.getColor(R.styleable.MXFloatActButton_txtColor, getResources().getColor(R.color.tagTextBlue));
            textGravity = typedArray.getInt(R.styleable.MXFloatActButton_textGravity, RIGHT_BOTTOM);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(TextUtils.isEmpty(srcText))
            return;
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        if(srcText.equals("0"))
            srcText = "";
        switch (textGravity){
            case LEFT_TOP:
                canvas.drawText(srcText, getMeasuredWidth() / 2 - textMargin, getMeasuredHeight() / 2 + (textSize - textMargin), paint);
                break;
            case RIGHT_TOP:
                canvas.drawText(srcText, getMeasuredWidth() / 2 + textMargin, getMeasuredHeight() / 2 - (textSize + textMargin), paint);
                break;
            case LEFT_BOTTOM:
                canvas.drawText(srcText, getMeasuredWidth() / 2 - textMargin, getMeasuredHeight() / 2 + (textSize + textMargin), paint);
                break;
            case RIGHT_BOTTOM:
                canvas.drawText(srcText, getMeasuredWidth() / 2 + textMargin, getMeasuredHeight() / 2 + (textSize + textMargin), paint);
                break;
        }
    }

}
