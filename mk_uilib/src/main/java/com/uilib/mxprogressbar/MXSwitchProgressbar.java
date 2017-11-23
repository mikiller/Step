package com.uilib.mxprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodSubtype;

import com.uilib.R;

/**
 * Created by Mikiller on 2016/7/29.
 */
public class MXSwitchProgressbar extends View {
    private final int maxProgress = 100;
    private int progress = 0;

    private RectF ovalPgs;
    private Paint pgsPaint;

    private Bitmap pauseBmp, playBmp;

    private int width, height;
    int strokeWidth = 4;
    boolean isRunning = false;

    public MXSwitchProgressbar(Context context) {
        super(context);
        initParams(context, null, 0);
    }

    public MXSwitchProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs, 0);
    }

    public MXSwitchProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context,attrs, defStyleAttr);
    }

    private void initParams(Context context, AttributeSet attrs, int defStyleAttr){
        ovalPgs = new RectF();
        pgsPaint = new Paint();

        Resources res = getResources();
        pauseBmp = BitmapFactory.decodeResource(res, R.drawable.pause);
        playBmp = BitmapFactory.decodeResource(res, R.drawable.play);

//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSwitchClicked(v);
//            }
//        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = pauseBmp.getWidth() + strokeWidth * 2;
        height = pauseBmp.getHeight() + strokeWidth * 2;
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST){
            setMeasuredDimension(width, height);
        }else {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width != height){
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        pgsPaint.setAntiAlias(true);
        pgsPaint.setColor(Color.TRANSPARENT);
        pgsPaint.setStrokeWidth(strokeWidth);
        pgsPaint.setStyle(Paint.Style.STROKE);

        ovalPgs.left = strokeWidth / 2;
        ovalPgs.top = strokeWidth / 2;
        ovalPgs.right = width - ovalPgs.left;
        ovalPgs.bottom = height - ovalPgs.top;

//        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(isRunning ? pauseBmp : playBmp, strokeWidth, strokeWidth, null);
        canvas.drawArc(ovalPgs, -90, 360, false, pgsPaint);



        pgsPaint.setColor(Color.WHITE);
        canvas.drawArc(ovalPgs, -90, ((float) progress / maxProgress) * 360, false, pgsPaint);
    }

    public void setProgress(int progress){
        this.progress = progress;
        this.invalidate();
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
        this.invalidate();
    }

    public void onSwitchClicked(View v){
        isRunning = !isRunning;
        v.invalidate();
    }
}
