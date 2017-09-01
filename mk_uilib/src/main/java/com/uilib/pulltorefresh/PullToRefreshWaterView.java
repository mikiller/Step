package com.uilib.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;

import com.uilib.waterfall.WaterView;


public class PullToRefreshWaterView extends PullToRefreshBase<WaterView> {

    public PullToRefreshWaterView(Context context) {
        super(context);
    }

    public PullToRefreshWaterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshWaterView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshWaterView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }
    
	@Override
	protected WaterView createRefreshableView(Context context, AttributeSet attrs) {
		WaterView waterView = new WaterView(context, attrs);
		return waterView;
	}

	

	public void setOnResetViewDataListener(WaterView.OnResetViewDataListener listener) {
		mRefreshableView.setOnResetViewDataListener(listener);
	}

	public void setAdapter(Adapter adapter) {
		mRefreshableView.setAdapter(adapter);
	}

	/**
	 * activity onPause的时候调用此方法，以释放所有占用的资源
	 */
	public void onPause() {
		mRefreshableView.onPause();
	}

	/**
	 * activity onPause的时候调用此方法，以加载需要加载的资源
	 */
	public void onResume() {
		mRefreshableView.onResume();
	}

    
    @Override
    protected boolean isReadyForPullEnd() {
        return mRefreshableView.getScrollY() >= (mRefreshableView.getMaxCloumnHeight() - mRefreshableView.getHeight());
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

}
