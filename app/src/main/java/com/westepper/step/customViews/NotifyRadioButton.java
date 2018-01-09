package com.westepper.step.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.westepper.step.base.Constants;
import com.westepper.step.utils.MXPreferenceUtils;

/**
 * Created by Mikiller on 2018/1/9.
 */

public class NotifyRadioButton extends RadioButton {
    private boolean hasNotify = false;
    private Paint paint;
    public NotifyRadioButton(Context context) {
        super(context);
        init();
    }

    public NotifyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotifyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        hasNotify = MXPreferenceUtils.getInstance().getBoolean(Constants.HAS_NOTIFY);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (hasNotify){
            int left = getWidth() / 2 + 40, top = getTop();
            canvas.drawOval(new RectF(left, top, left + 30, top - 30), paint);
        }
        super.onDraw(canvas);
    }

    public void setHasNotify(boolean hasNotify){
        this.hasNotify = hasNotify;
        postInvalidate();
    }
}
