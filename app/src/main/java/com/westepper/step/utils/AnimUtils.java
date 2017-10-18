package com.westepper.step.utils;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;

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

    public static void startValueAnim(float startValue, float endValue, int duration, ValueAnimator.AnimatorUpdateListener listener){
        ValueAnimator va = ValueAnimator.ofFloat(startValue, endValue);
        va.setDuration(duration);
        va.setInterpolator(new DecelerateInterpolator());
        if(listener != null)
            va.addUpdateListener(listener);
        va.start();
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

    public static void startRotateAnim(final View view, float startD, float endD, int duration){
        RotateAnimation rotateAnim = new RotateAnimation(startD, endD, view.getPivotX(), view.getPivotY());
        rotateAnim.setDuration(duration);
        rotateAnim.setFillAfter(true);
        rotateAnim.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(rotateAnim);
    }
}
