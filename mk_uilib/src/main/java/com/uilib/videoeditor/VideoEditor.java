package com.uilib.videoeditor;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.uilib.R;
import com.uilib.videoeditor.listeners.EditorSliderSeekListener;
import com.uilib.videoeditor.utils.ExtractFrameWorkThread;
import com.uilib.videoeditor.utils.ExtractVideoInfoUtil;

/**
 * Created by Mikiller on 2017/5/3.
 */

public class VideoEditor extends FrameLayout {
    private Context mContext;
    private RecyclerView rcv_thumb;
    private ImageView iv_cursor;
    private RangeSeekBar seekBar;
    private VideoEditAdapter videoEditAdapter;

    int thumbnailMargin = 0;
    private int maxThumbnailCount = 10;
    private long leftProgress, rightProgress;
    private boolean isSeeking = false;
    private ValueAnimator animator;
    private ExtractFrameWorkThread mExtractFrameWorkThread;
    private ExtractVideoInfoUtil extractVideoInfoUtil;

    public VideoEditor(Context context) {
        super(context);
        initView(context, null);
    }

    public VideoEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_video_seekbar, this);
        rcv_thumb = (RecyclerView) findViewById(R.id.rcv_thumb);
        rcv_thumb.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
        seekBar = (RangeSeekBar) findViewById(R.id.seekBar);

        if (attrs == null)
            return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VideoEditor);
        if (ta.getDrawable(R.styleable.VideoEditor_leftSlider) != null) {
            setLeftThumb(ta.getDrawable(R.styleable.VideoEditor_leftSlider));
        }
        if (ta.getDrawable(R.styleable.VideoEditor_rightSlider) != null) {
            setRightThumb(ta.getDrawable(R.styleable.VideoEditor_rightSlider));
        }
        setMinTrimMs(ta.getInteger(R.styleable.VideoEditor_minTrimMs, 3000));
        setThumbWidth(ta.getDimension(R.styleable.VideoEditor_sliderWidth, 64.0f));
        setBorderColor(ta.getColor(R.styleable.VideoEditor_borderColor, Color.parseColor("#00befa")));
    }

    public void setMaxTrimMs(long duration) {
        seekBar.setMaxTrimMs(duration);
    }

    public void setMinTrimMs(long min) {
        seekBar.setMinTrimMs(min);
    }

    public void setThumbWidth(double thumbWidth) {
        seekBar.setSliderWidth(thumbWidth);
    }

    public void setLeftThumb(Drawable drawable) {
        seekBar.setLeftSlider(drawable);
    }

    public void setRightThumb(Drawable drawable) {
        seekBar.setRightSlider(drawable);
    }

    public void setBorderColor(int color){
        seekBar.setBorderColor(color);
    }

    public void setFilePath(String path) {
        extractVideoInfoUtil = new ExtractVideoInfoUtil(path, new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    videoEditAdapter.addItemVideoInfo((Bitmap) msg.obj);
                }
            }
        });

    }

    public void setEditorSliderSeekListener(final EditorSliderSeekListener listener){
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, long minValue, long maxValue, int action, int pressedThumb) {
                leftProgress = minValue;
                rightProgress = maxValue;
                if(listener != null){
                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            isSeeking = true;
                            listener.onSliderClicked();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            listener.onSliderMoved(pressedThumb == RangeSeekBar.MIN ? minValue : maxValue);
                            break;
                        case MotionEvent.ACTION_UP:
                            isSeeking = false;
                            listener.onSliderUp(minValue);
                            break;
                    }
                }
            }
        });
    }

    public void setThumbnailMargin(int margin){
        thumbnailMargin = margin;
    }

    public long getLeftProgress() {
        return leftProgress;
    }

    public long getRightProgress() {
        return rightProgress;
    }

    public long getDuration(){
        return extractVideoInfoUtil.getVideoLength();
    }

    public boolean isSeeking(){
        return isSeeking;
    }

    public int getSliderWidth(){
        return seekBar.getSliderWidth();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        double extractW = (getMeasuredWidth() - 2*thumbnailMargin) / maxThumbnailCount ;
        double extractH = getMeasuredHeight();
        ((LayoutParams)rcv_thumb.getLayoutParams()).leftMargin = thumbnailMargin;
        ((LayoutParams)rcv_thumb.getLayoutParams()).rightMargin = thumbnailMargin;
        videoEditAdapter = new VideoEditAdapter(mContext,
                (int)extractW, (int)extractH);
        rcv_thumb.setAdapter(videoEditAdapter);
        if(extractVideoInfoUtil != null) {
            seekBar.setMaxTrimMs(extractVideoInfoUtil.getVideoLength());
            seekBar.setThubmPos(RangeSeekBar.MIN, 0);
            seekBar.setThubmPos(RangeSeekBar.MAX, extractVideoInfoUtil.getVideoLength());
            rightProgress = extractVideoInfoUtil.getVideoLength();
            if (mExtractFrameWorkThread == null) {
                mExtractFrameWorkThread = new ExtractFrameWorkThread(extractVideoInfoUtil, extractW, extractH, maxThumbnailCount);
                mExtractFrameWorkThread.start();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 195;
        if (MeasureSpec.AT_MOST != MeasureSpec.getMode(heightMeasureSpec)) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);

    }

    public void startVideo() {
        iv_cursor.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        anim();
    }

    public void pauseVideo() {
        if (iv_cursor.getVisibility() == View.VISIBLE) {
            iv_cursor.setVisibility(View.GONE);
        }
        iv_cursor.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    public void restartVideo() {
        iv_cursor.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        anim();
    }

    private void anim() {
        if (iv_cursor.getVisibility() == View.GONE) {
            iv_cursor.setVisibility(View.VISIBLE);
        }
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv_cursor.getLayoutParams();
        animator = ValueAnimator
                .ofInt((int)seekBar.getCursorCoordValue(true), (int)seekBar.getCursorCoordValue(false))
                .setDuration(rightProgress - leftProgress);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                iv_cursor.setLayoutParams(params);
            }
        });
        animator.start();
    }

    public void release() {
        extractVideoInfoUtil.release();
        extractVideoInfoUtil = null;
        if (animator != null) {
            animator.cancel();
            iv_cursor.clearAnimation();
        }
        seekBar.release();
    }

}
