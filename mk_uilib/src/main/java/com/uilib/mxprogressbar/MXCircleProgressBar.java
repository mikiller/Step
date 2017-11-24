package com.uilib.mxprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.uilib.R;

/**
 * Created by Mikiller on 2017/11/23.
 */

public class MXCircleProgressBar extends View {
    private final int maxProgress = 100;
    private double progress = 0;
    private int radius, startPos = -90, endPos = 360;
    private float borderWidth, txtSize, txtY;
    private int bgColor, pgsColor, txtColor;

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

    private Paint createPaint(Paint.Style style, float width, int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeCap(Paint.Cap.ROUND);
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
        txtSize = (ovalPgs.height() / 4);
        txtY = ovalPgs.centerY() + txtSize / 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawArc(ovalPgs, startPos, endPos, false, circlePaint);
        canvas.drawArc(ovalPgs, startPos, (float) (progress / maxProgress * endPos), false, pgsPaint);

        String pgs = String.valueOf(progress).concat("%");
        String[] nums = pgs.split("\\.");
        float[] tmp = new float[pgs.length()];
        canvas.drawText(nums[0], getTxtX(txtSize, nums[0], tmp, 0.5f), txtY, txtPaint);
        canvas.drawText(".", ovalPgs.centerX(), txtY, txtPaint);
        canvas.drawText(nums[1], getTxtX(txtSize*2.f/3.f, nums[1], tmp, -2.f / 3.f), txtY, txtPaint);
    }

    private float getTxtX(float txtSize, String txt, float[] tmp, float offsetX){
        txtPaint.setTextSize(txtSize);
        int txtCounts = txtPaint.getTextWidths(txt, tmp);
        float txtWidth = txtCounts * tmp[0] * offsetX;
        return ovalPgs.centerX() - txtWidth;
    }

    public void setProgress(double pgs) {
        progress = pgs;
        invalidate();
    }
}
