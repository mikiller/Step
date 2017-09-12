package com.uilib.mxgallery.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.uilib.R;
import com.uilib.mxgallery.models.MimeType;
import com.uilib.mxgallery.models.ReportResModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mikiller on 2017/4/24.
 */

public class CameraGalleryUtils {
    private final static String TAG = CameraGalleryUtils.class.getSimpleName();
    public final static int CAMERA_CAPTURE = 1, CROP_PIC = 2, VIDEO = 3, FILES = 4, CROP_VIDEO = 5;
    public final static String THUMB_FILE = "thumbfile";
    private Context mContext;
    public static String thumbDir = Environment.getExternalStorageDirectory().getPath().concat(File.separator).concat("DCIM/Camera/");
    private List<ReportResModel> thumbList;
    public ReportResModel tmpFile;

    public List<ReportResModel> getThumbList() {
        return thumbList;
    }

    public void addThumbModel(ReportResModel model) {
        thumbList.add(model);
        updateGallery(model);
    }

    public void addAllThumbModels(List<File> files) {
        for (File file : files) {
            ReportResModel model = createReportResModel(file);
            thumbList.add(model);
        }
    }

    public ReportResModel createReportResModel(File file) {
        String type = getFileMimeType(file.getPath());
        ReportResModel model = new ReportResModel(createUUID(), MimeType.getMimeTypeWithTypeName(type), file);
        return model;

    }

    public String createUUID() {
        String id = UUID.randomUUID().toString();
        return id.replaceAll("-", "");
    }

    private CameraGalleryUtils() {
        thumbList = new ArrayList<>();
    }

    private static class CameraGalleryUtilsFactory {
        private static CameraGalleryUtils instance = new CameraGalleryUtils();
    }

    public static CameraGalleryUtils getInstance(Context context) {
        CameraGalleryUtilsFactory.instance.mContext = context;
        return getInstance();
    }

    public static CameraGalleryUtils getInstance() {
        return CameraGalleryUtilsFactory.instance;
    }

    public void openVideoEditor(ReportResModel file) {
        Map<String, Object> args = new HashMap<>();
        args.put(THUMB_FILE, file);
        //ActivityManager.startActivityforResult((Activity) mContext, VideoEditorActivity.class, CROP_VIDEO, args);
    }

    public void openSysCamera() {
        startSysActivity(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    public void openSysVideo() {
        startSysActivity(MediaStore.ACTION_VIDEO_CAPTURE);
    }

    private void startSysActivity(String action) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileName = "xnews_" + System.currentTimeMillis();
            int requestCode = 0;
            if (action.equals(MediaStore.ACTION_IMAGE_CAPTURE)) {
                fileName = fileName.concat(".jpg");
                requestCode = CAMERA_CAPTURE;
                tmpFile = new ReportResModel(createUUID(), MimeType.JPEG, new File(thumbDir, fileName));
            } else if (action.equals(MediaStore.ACTION_VIDEO_CAPTURE)) {
                fileName = fileName.concat(".3gp");
                requestCode = VIDEO;
                tmpFile = new ReportResModel(createUUID(), MimeType.THREEGPP, new File(thumbDir, fileName));
            }
            try {
                if (!tmpFile.getResFile().getParentFile().exists())
                    tmpFile.getResFile().getParentFile().mkdirs();
                tmpFile.getResFile().createNewFile();
                ((Activity) mContext).startActivityForResult(new Intent(action).putExtra(MediaStore.EXTRA_OUTPUT, getFileUri(tmpFile.getResFile())), requestCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "cannot get photo save path", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getFileUri(File file) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) ? Uri.fromFile(file) : FileProvider.getUriForFile(mContext, mContext.getString(R.string.file_provider), file);
    }

    public String getFileMimeType(String path) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path.trim()));
    }

    public void openSysGallery() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT, null);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
        ((Activity) mContext).startActivityForResult(i, FILES);
    }

    public void updateGallery(final ReportResModel model) {
        MediaScannerConnection.scanFile(mContext, new String[]{model.getResFile().getAbsolutePath()}, null, null);
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, getFileUri(model.getResFile())));
    }

    public void release() {
        thumbList.clear();
        mContext = null;
    }

    public void cropPhoto(Uri thumbUri) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(thumbUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, thumbUri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ((Activity) mContext).startActivityForResult(intent, CROP_PIC);
    }

    public Bitmap getThumbImg(Uri uri) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri), new Rect(), options);
        Log.e(TAG, "height: " + options.outHeight + ", width: " + options.outWidth);
        int scaleHeight = Math.round(options.outHeight / 210);
        int scaleWidth = Math.round(options.outWidth / 280);
        int scale = scaleHeight < scaleWidth ? scaleHeight : scaleWidth;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri), new Rect(), options).copy(Bitmap.Config.RGB_565, false);
        return bmp;
    }
}
