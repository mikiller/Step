package com.uilib.mxprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.uilib.R;

/**
 * Created by Mikiller on 2017/11/23.
 */

public class MXCircleProgressBar extends View {
    private final int maxProgress = 100;
    private float progress = 0;
    private int radius, startPos = -90, endPos = 360;
    private float borderWidth;
    private int bgColor, pgsColor, txtColor, txtSize;

    private RectF ovalPgs;
    private Paint circlePaint, pgsPaint, txtPaint;

    public MXCircleProgressBar(Context context) {
        this(context, null, 0);
    }

    public MXCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MXCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MXCircleProgressBar);
            startPos = ta.getInt(R.styleable.MXCircleProgressBar_startPos, 0);
            endPos = ta.getInt(R.styleable.MXCircleProgressBar_endPos, 360);
            bgColor = ta.getColor(R.styleable.MXCircleProgressBar_backPgsColor, getResources().getColor(R.color.pgs_back_color));
            pgsColor = ta.getColor(R.styleable.MXCircleProgressBar_frontPgsColor, getResources().getColor(R.color.pgs_front_color));
            txtColor = ta.getColor(R.styleable.MXCircleProgressBar_txtPgsColor, getResources().getColor(R.color.pgs_text_color));
            borderWidth = ta.getDimensionPixelSize(R.styleable.MXCircleProgressBar_borderWidth, 6);
            ta.recycle();
        }

        circlePaint = createPaint(Paint.Style.STROKE, borderWidth, bgColor);
        pgsPaint = createPaint(Paint.Style.STROKE, borderWidth, pgsColor);

    }

    //    paint.setTypeface(Typeface.DEFAULT_BOLD);
    private Paint createPaint(Paint.Style style, float width, int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        radius = Math.min(width, height);
        ovalPgs = new RectF();
        ovalPgs.left = (borderWidth + width - radius) / 2;
        ovalPgs.top = (borderWidth + height - radius) / 2;
        ovalPgs.right = radius + ovalPgs.left - borderWidth;
        ovalPgs.bottom = radius + ovalPgs.top - borderWidth;
        txtPaint = createPaint(Paint.Style.FILL, 0, txtColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawArc(ovalPgs, startPos, endPos, false, circlePaint);
        canvas.drawArc(ovalPgs, startPos, progress / maxProgress * endPos, false, pgsPaint);
        float txtY = ovalPgs.centerY() - txtSize/2;
        String pgs = String.valueOf(progress);
        String[] nums = pgs.split("\\.");


        float[] tmp = new float[pgs.length()];
        int txtCounts = txtPaint.getTextWidths(nums[0], tmp);
        Log.e("pgs" + getId(), "length：" + txtCounts);
        int txtWidth = 0;
        for(int i = 0; i < txtCounts; i++){
            txtWidth += tmp[i];
        }
        txtPaint.getTextWidths(".", tmp);
        float pointWidth = tmp[0];

        txtPaint.setTextSize((ovalPgs.height() / 4));
        canvas.drawText(".", ovalPgs.centerX(), txtY, txtPaint);
        canvas.drawText(nums[0], ovalPgs.centerX() - txtWidth - pointWidth, txtY, txtPaint);
        txtPaint.setTextSize(ovalPgs.height() / 8);
        canvas.drawText(nums[1].concat("%"), ovalPgs.centerX() + pointWidth * 2, txtY, txtPaint);
    }

    public void setProgress(float pgs) {
        progress = pgs;
        invalidate();
    }
}
