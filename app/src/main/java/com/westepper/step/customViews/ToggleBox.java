package com.westepper.step.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by Mikiller on 2017/10/11.
 */

public class ToggleBox extends CheckBox {
    Paint btnPaint, shadowPaint;
    int btnX;
    public ToggleBox(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ToggleBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public ToggleBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        btnPaint = new Paint();
        btnPaint.setAntiAlias(true);
        btnPaint.setColor(Color.WHITE);
        btnPaint.setStyle(Paint.Style.FILL);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.parseColor("#33000000"));
        shadowPaint.setAlpha(20);
        shadowPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        btnX = isChecked() ? getMeasuredWidth() - getMeasuredHeight() / 2 : getMeasuredHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(btnX, getMeasuredHeight() / 2, getMeasuredHeight() / 2, shadowPaint);
        canvas.drawCircle(btnX, getMeasuredHeight() / 2, getMeasuredHeight() / 2 - 4, btnPaint);
        if(isChecked()) {
            if(btnX < getMeasuredWidth() - getMeasuredHeight() / 2) {
                btnX += 5;
                invalidate();
            }
        }else{
            if(btnX > getMeasuredHeight() / 2){
                btnX -= 5;
                invalidate();
            }
        }
    }

}
