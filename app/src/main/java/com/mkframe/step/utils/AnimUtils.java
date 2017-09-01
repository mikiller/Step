package com.mkframe.step.utils;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Mikiller on 2017/7/18.
 */

public class AnimUtils {

    public static void startObjectAnim(View view, String field, float startValue, float endValue, int duration){
        startObjectAnim(view, field, startValue, endValue, duration, null);
    }

    public static void startObjectAnim(View view, String field, float startValue, float endValue, int duration, ValueAnimator.AnimatorUpdateListener listener){
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, field, startValue, endValue);
        oa.setInterpolator(new DecelerateInterpolator());
        oa.setEvaluator(new FloatEvaluator());
        oa.setDuration(duration);
        oa.setRepeatCount(0);
        if(listener != null)
            oa.addUpdateListener(listener);
        oa.start();
    }

    public static void startAlphaAnim(final View view, final float startV, final float endV, int duration){
        AlphaAnimation alphaAnim = new AlphaAnimation(startV, endV);
        alphaAnim.setDuration(duration);
        alphaAnim.setInterpolator(new DecelerateInterpolator());
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(startV == 0.0f)
                    view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(endV == 0.0f)
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnim);
    }
}
