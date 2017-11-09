package com.uilib.uploadimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.utils.BitmapUtils;
import com.uilib.R;
import com.uilib.mxprogressbar.MXProgressbar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Mikiller on 2016/8/4.
 */
public class MXProgressImageView extends RelativeLayout implements View.OnClickListener {
    public enum ImageState {
        STOP, PAUSE, START, FAILED, SUCCESS
    }

    public final static int LINE = 0;
    public final static int CIRCLE = 1;

    private RelativeLayout rl_background;
    private ProgressBar linePgsBar;
    private MXProgressbar circlePgsBar;
    private View mask;
    private TextView tv_wait;
    private LinearLayout ll_refresh;
    private int pgbType;
    private int bgImage;
    private Bitmap bgBmp;
    private int radio;
    private Matrix matrix;
    private boolean needMatrix = false;
    private boolean needUpdate;
    private boolean isAuto = false;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    private String filePath = null;

    private ImageState uploadState;

    private boolean isRunning = false;

    private onViewStateListener listener;

    public MXProgressImageView(Context context) {
        super(context);
        initParams(context, null, 0);
    }

    public MXProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs, 0);
    }

    public MXProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs, defStyleAttr);
    }

    private void initParams(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MXProgressImageView);
        if (typedArray != null) {
            pgbType = typedArray.getInt(R.styleable.MXProgressImageView_pgbType, -1);
            bgImage = typedArray.getResourceId(R.styleable.MXProgressImageView_bgImage, NO_ID);
            radio = (int) typedArray.getDimension(R.styleable.MXProgressImageView_radio, 0f);
        }
        setWillNotDraw(false);
        LayoutInflater.from(context).inflate(R.layout.mxupload_imageview_layout, this, true);
        rl_background = (RelativeLayout) findViewById(R.id.rl_background);
        rl_background.setOnClickListener(this);
        linePgsBar = (ProgressBar) findViewById(R.id.pgb_line);
        circlePgsBar = (MXProgressbar) findViewById(R.id.pgb_circle);
        circlePgsBar.setOnClickListener(this);
        mask = findViewById(R.id.mask);
        tv_wait = (TextView) findViewById(R.id.tv_wait);
        tv_wait.setOnClickListener(this);
        ll_refresh = (LinearLayout) findViewById(R.id.ll_refresh);
        ll_refresh.setOnClickListener(this);

        onStop();
    }

    public int getPgbType() {
        return pgbType;
    }

    public void setPgbType(int pgbType) {
        this.pgbType = pgbType;
    }

    public int getBgImage() {
        return bgImage;
    }

    public void setBgImage(final int bgImage) {
        this.bgImage = bgImage;
        filePath = null;
        needUpdate = true;
    }

    public Bitmap getBgBmp() {
        return bgBmp;
    }

    public void setBgBmp(Bitmap bgBmp) {
        this.bgBmp = bgBmp;
        needUpdate = true;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
        bgImage = NO_ID;
        needUpdate = true;
    }

    public ImageState getUploadState() {
        return uploadState;
    }

    public synchronized void setUploadState(ImageState uploadState) {
//        if(this.uploadState == ImageState.PAUSE) {
//            return;
//        }
        this.uploadState = uploadState;
    }

    public onViewStateListener getListener() {
        return listener;
    }

    public void setListener(onViewStateListener listener) {
        this.listener = listener;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
        isRunning = isAuto;
    }

    private void updateBgBmp() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(filePath)) {
                    if (filePath.endsWith(".3gp") || filePath.endsWith(".mp4"))
                        bgBmp = getVidioBitmap(filePath, getMeasuredWidth(), getMeasuredHeight(), MediaStore.Images.Thumbnails.MICRO_KIND);
                    else
                        bgBmp = BitmapUtils.decodeSampleBmpFromFile(filePath, getMeasuredWidth(), getMeasuredHeight());
                } else if (bgImage != NO_ID) {
                    bgBmp = BitmapUtils.decodeSampleBmpFromRes(getResources(), bgImage, getMeasuredWidth(), getMeasuredHeight());
                }
                if (bgBmp == null) {
                    bgImage = R.mipmap.default_icon;
                    bgBmp = BitmapUtils.decodeSampleBmpFromRes(getResources(), bgImage, getMeasuredWidth(), getMeasuredHeight());
                }
                if (getMeasuredWidth() >= bgBmp.getWidth() && getMeasuredHeight() >= bgBmp.getHeight()) {
                    matrix = BitmapUtils.getScaleMatrix(bgBmp.getWidth(), bgBmp.getHeight(), getMeasuredWidth(), getMeasuredHeight());
                    bgBmp = BitmapUtils.drawRoundBmp(bgBmp, radio);
                    needMatrix = true;
                } else {
                    if (bgBmp.getWidth() < getMeasuredWidth() || bgBmp.getHeight() < getMeasuredHeight()) {
                        bgBmp = Bitmap.createScaledBitmap(bgBmp, getMeasuredWidth(), getMeasuredHeight(), false);
                    }
                    bgBmp = BitmapUtils.drawRoundBmp(BitmapUtils.getCenterSquareBmp(bgBmp, getMeasuredWidth(), getMeasuredHeight()), radio);
                    needMatrix = false;
                }
                needUpdate = false;
                setTag(Bitmap.createBitmap(bgBmp));
                MXProgressImageView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
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
        setPgsBarVisibility();
        tv_wait.setVisibility(uploadState == ImageState.STOP ? VISIBLE : GONE);
        ll_refresh.setVisibility(uploadState == ImageState.FAILED ? VISIBLE : GONE);
        mask.setVisibility(uploadState == ImageState.SUCCESS ? GONE : VISIBLE);
        if (needUpdate) {
            GradientDrawable gd = (GradientDrawable) mask.getBackground();
            gd.setCornerRadius(radio);
            mask.setBackgroundDrawable(gd);
            updateBgBmp();
        }
    }



    private void setPgsBarVisibility() {
        switch (pgbType) {
            case 0:
                linePgsBar.setVisibility((uploadState == ImageState.START || uploadState == ImageState.PAUSE) ? VISIBLE : GONE);
                break;
            case 1:
                circlePgsBar.setVisibility((uploadState == ImageState.START || uploadState == ImageState.PAUSE) ? VISIBLE : GONE);
                circlePgsBar.setRunning(uploadState == ImageState.START);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

//            if (needUpdate) {
//                updateBgBmp();
//            } else {
        if (bgBmp == null || bgBmp.isRecycled())
            return;

        if (needMatrix) {
            canvas.drawBitmap(bgBmp, matrix, null);
        } else
            canvas.drawBitmap(bgBmp, 0, 0, null);


//        if (bgBmp != null && !bgBmp.isRecycled()) {
////            updateBgBmp(canvas);
//            if (needMatrix) {
//                canvas.drawBitmap(bgBmp, matrix, null);
//            } else
//                canvas.drawBitmap(bgBmp, 0, 0, null);
////            if (getWidth() >= bgBmp.getWidth() && getHeight() >= bgBmp.getHeight()) {
////                Matrix matrix = BitmapUtils.getScaleMatrix(bgBmp.getWidth(), bgBmp.getHeight(), getWidth(), getHeight());
////                canvas.drawBitmap(BitmapUtils.drawRoundBmp(bgBmp, radio), matrix, null);
////                return;
////            }
////
////            if (bgBmp.getWidth() < getWidth() || bgBmp.getHeight() < getHeight()) {
////                bgBmp = Bitmap.createScaledBitmap(bgBmp, getWidth(), getHeight(), false);
////            }
////            canvas.drawBitmap(BitmapUtils.drawRoundBmp(BitmapUtils.getCenterSquareBmp(bgBmp, getWidth(), getHeight()), radio), 0, 0, null);
//
//        } else {
//            updateBgBmp();
////            post(new Runnable() {
////                @Override
////                public void run() {
//////
//////                    if (!TextUtils.isEmpty(filePath)) {
//////                        if (filePath.endsWith(".3gp") || filePath.endsWith(".mp4"))
//////                            bgBmp = getVidioBitmap(filePath, 150, 150, MediaStore.Images.Thumbnails.MICRO_KIND);
//////                        else
//////                            bgBmp = BitmapUtils.decodeSampleBmpFromFile(filePath, getWidth(), getHeight());
//////                    } else if (bgImage != NO_ID) {
//////                        bgBmp = BitmapUtils.decodeSampleBmpFromRes(getResources(), bgImage, getWidth(), getHeight());
//////                    }
//////
////
////                    invalidate();
////                }
////            });
//        }
    }

    private Bitmap getVidioBitmap(String path, int width, int height, int microKind) {
        // 定義一個Bitmap對象bitmap；
        Bitmap bitmap = null;

        // ThumbnailUtils類的截取的圖片是保持原始比例的，但是本人發現顯示在ImageView控件上有时候有部分沒顯示出來；
        // 調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        bitmap = ThumbnailUtils.createVideoThumbnail(path, microKind);

        // 調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        // 最後一個參數的具體含義我也不太清楚，因為是閉源的；
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        // 放回bitmap对象；
        return bitmap;

    }

    public void setProgress(int progress) {
        switch (pgbType) {
            case 0:
                linePgsBar.setProgress(progress);
                break;
            case 1:
                circlePgsBar.setProgress(progress);
                break;
            default:
                break;
        }
        if (progress == 100) {
            onSuccess();
        } else if (progress == -1) {
            onFailed();
        }
    }


    public boolean isRunning() {
        return circlePgsBar.isRunning();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_refresh) {
            onRestart(v);
        } else if (v.getId() == R.id.tv_wait) {
            if(!isAuto)
                onStart(v);
        } else if (v.getId() == R.id.pgb_circle) {
            circlePgsBar.onSwitchClicked(v);
            if (isRunning()) {
                onContinue();
            } else {
                onPause();
            }
        } else if (v.getId() == R.id.rl_background) {
            //show preview
        }
    }

    protected void onStop() {
        uploadState = ImageState.STOP;
        tv_wait.setVisibility(VISIBLE);
        setProgress(0);
//        circlePgsBar.setRunning(false);
        setPgsBarVisibility();
        ll_refresh.setVisibility(GONE);
        if (listener != null)
            listener.onStop();
    }

    protected void onStart(View v) {
        uploadState = ImageState.START;
        v.setVisibility(GONE);
        setPgsBarVisibility();
        circlePgsBar.setRunning(true);
        if (listener != null)
            listener.onStart();
    }

    protected void onRestart(View v) {
        setProgress(0);
        onStart(v);
    }

    protected void onPause() {
        uploadState = ImageState.PAUSE;
        if (listener != null)
            listener.onPause();
    }

    protected void onContinue() {
        uploadState = ImageState.START;
        if (listener != null)
            listener.onStart();
    }

    protected void onFailed() {
        uploadState = ImageState.FAILED;
        ll_refresh.setVisibility(VISIBLE);
        setProgress(0);
        setPgsBarVisibility();
        if (listener != null) {
            listener.onFailed();
        }
    }

    protected void onSuccess() {
        uploadState = ImageState.SUCCESS;
        setPgsBarVisibility();
        if (listener != null)
            listener.onSuccess();
    }

    public interface onViewStateListener {
        void onStop();

        void onStart();

        void onPause();

        void onFailed();

        void onSuccess();
    }
}
