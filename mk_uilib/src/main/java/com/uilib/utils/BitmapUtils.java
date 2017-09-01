package com.uilib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * Created by Mikiller on 2016/8/5.
 */
public class BitmapUtils {

    public static Bitmap decodeSampleBmpFromFile(String filePath, int dscWidth, int dscHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = getSampleSize(options, dscWidth, dscHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeSampleBmpFromRes(Resources res, int resId, int dscWidth, int dscHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = getSampleSize(options, dscWidth, dscHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int getSampleSize(BitmapFactory.Options options, float dscWidth, float dscHeight){
        float width = (float) options.outWidth;
        float height = (float) options.outHeight;
        if(dscWidth >= width || dscHeight >= height)
            return 1;
        return Math.round((width > height) ? (height / dscHeight) : (width / dscWidth));
    }

    public static Bitmap getCenterSquareBmp(Bitmap src, int squareWidth, int squareHeight){
        int top, left;
        top = (src.getHeight() - squareHeight) / 2;
        left = (src.getWidth() - squareWidth) / 2;
        Bitmap bmp = Bitmap.createBitmap(src, left, top, squareWidth, squareHeight);
        return bmp;
    }

    public static Matrix getScaleMatrix(float width, float height, float dscWidth, float dscHeight){
        Matrix matrix = new Matrix();
        matrix.preScale(dscWidth / width, dscHeight / height, 0, 0);
        return matrix;
    }

    public static Bitmap drawRoundBmp(Bitmap src, float corner){
        if(src == null)
            return null;
        else if(src.isRecycled())
            return src;
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0,0,src.getWidth(), src.getHeight());
        RectF rectF = new RectF(0,0,src.getWidth(), src.getHeight());
        canvas.drawRoundRect(rectF, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        src.recycle();
        return output;
    }

    /**
     * bitmap 高斯模糊算法
     * */
    public static void blur(Context context, Bitmap bmp, ImageView view){
        float radius = 3;
        float scale = 8.0f;
        Bitmap overlay = Bitmap.createBitmap((int) (bmp.getWidth()/ scale), (int) (bmp.getHeight() / scale), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scale, -view.getTop() / scale);
        canvas.scale(1/ scale, 1/ scale);
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bmp, 0, 0, paint);

        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);

        view.setImageDrawable(new BitmapDrawable(context.getResources(), overlay));
        rs.destroy();
    }

    public static void releaseImageView(ImageView imageView){
        if(imageView == null)
            return;
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if(drawable != null) {
            Bitmap bmp = drawable.getBitmap();
            if(bmp != null && !bmp.isRecycled()){
                bmp.recycle();
                bmp = null;
            }
            drawable.setCallback(null);
            drawable = null;
        }
        imageView.setImageBitmap(null);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成 bitmap
    {
        Bitmap bitmap;
        int width = drawable.getIntrinsicWidth();   // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        if(width<=0||height<=0)
            return null;
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;         // 取 drawable 的颜色格式
        bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应 bitmap
        Canvas canvas = new Canvas(bitmap);         // 建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);      // 把 drawable 内容画到画布中
        return bitmap;
    }

    public static Bitmap scaleBmp(Bitmap bmp, int w, int h)
    {
//        Bitmap newbmp;
        int width = bmp.getWidth();
        int height= bmp.getHeight();
        if(width<=0||height<=0)
            return null;
        //oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
        Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象
        float scaleWidth = ((float)w / width);   // 计算缩放比例
        float scaleHeight = ((float)h / height);
        matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);       // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图

//        BitmapDrawable bitmapDrawable = new BitmapDrawable(newbmp);
//        return bitmapDrawable;       // 把 bitmap 转换成 drawable 并返回
    }

    public static Bitmap doRotate(String path_name) {
        int degree = readPictureDegree(path_name);
        Bitmap cbitmap= BitmapFactory.decodeFile(path_name);
        /**
         * 把图片旋转为正的方向
         */
        Bitmap newbitmap = rotaingImageView(degree, cbitmap);
        return newbitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    Log.v("degree:",degree+"");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    Log.v("degree:",degree+"");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    Log.v("degree:",degree+"");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    Log.v("degree:",degree+"");
                    break;
                default:Log.v("degree:",degree+"");degree = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static BitmapFactory.Options getCustomOption(int inSampleSize){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize < 1 ? 1 : inSampleSize;
        options.inDither=false;
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }

    public static void setBmpwithOption(BitmapFactory.Options option, Resources res, View view, int resId, boolean isRawRes){
        Bitmap bmp;
        try {
        if(isRawRes){
            InputStream inputStream = res.openRawResource(resId);
            bmp = BitmapFactory.decodeStream(inputStream, null, option);
            view.setBackgroundDrawable(new BitmapDrawable(bmp));
            inputStream.close();
        }else{
            bmp = BitmapFactory.decodeResource(res, resId, option);
            view.setBackgroundDrawable(new BitmapDrawable());
        }
        }catch (IOException e){

        }catch (Resources.NotFoundException e) {
        }
    }

//    public static Bitmap getBmpFromUrl(final String urlStr){
//        Executors.newCachedThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL(urlStr);
//                    Bitmap bmp = BitmapFactory.decodeStream(url.openStream());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
