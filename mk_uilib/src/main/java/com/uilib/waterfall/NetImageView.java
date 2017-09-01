package com.uilib.waterfall;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


@Deprecated
public class NetImageView extends ImageView {
	private int defaultImageRes;
	private String url;
	private String urlBig;
//	private DisplayImageOptions options;
	private boolean isLoad;
	private Bitmap bitmap;

	public NetImageView(Context context) {
		super(context);
		init();
	}

	public NetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
//		options = new DisplayImageOptions.Builder()
		// 缓存到sd卡
//				.cacheOnDisc()
				// 缓存到内存，慎用，如果手动控制bitmap回收，会出问题
				// .cacheInMemory()
//				.build();
	}

	public void setDefault(int resId) {
		defaultImageRes = resId;
	}

	public void setUrl(String imageUrl) {
		url = imageUrl;
	}

	public void setUrlBig(String imageUrl) {
		urlBig = imageUrl;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (bm != null) {
			bitmap = bm;
		}
		super.setImageBitmap(bitmap);
	}

	public void recycle(boolean showDefault) {
//		ImageLoader.getInstance().cancelDisplayTask(this);
		if (showDefault) {
			setImageResource(defaultImageRes);
		}
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		isLoad = false;
	}

	public void reload(final boolean loadBig) {
		if (!isLoad && !TextUtils.isEmpty(url)) {
			isLoad = true;
			setImageResource(defaultImageRes);
//			ImageLoader.getInstance().displayImage(url, this, options,
//					new ImageLoadingListener() {
//
//						@Override
//						public void onLoadingStarted(String imageUri, View view) {
//							// TODO Auto-generated method stub
//
//						}
//
//						@Override
//						public void onLoadingFailed(String imageUri, View view,
//								FailReason failReason) {
//							// TODO Auto-generated method stub
//
//						}
//
//						@Override
//						public void onLoadingComplete(String imageUri,
//								View view, Bitmap loadedImage) {
//							if (loadBig && !TextUtils.isEmpty(urlBig)) {
//								ImageLoader.getInstance().displayImage(urlBig,
//										NetImageView.this, options, null);
//							}
//
//						}
//
//						@Override
//						public void onLoadingCancelled(String imageUri,
//								View view) {
//							// TODO Auto-generated method stub
//
//						}
//					});
		}
	}

}
