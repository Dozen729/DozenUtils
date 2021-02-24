package com.dozen.commonbase.utils;

import android.animation.ValueAnimator;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * @author: Dozen
 * @description:数值增加动画
 * @time: 2020/12/18
 */
public class ValueChangeUtils {


    //int数值增加动画
    public static void intValueChange(final TextView tvChange, int time, int startValue, int endValue){
        ValueAnimator animator = ValueAnimator.ofInt(startValue, endValue);
        animator.setDuration(time);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tvChange.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

    //float数值增加动画
    public static void doubleValueChange(final TextView tvChange, int time, float startValue, float endValue){
        ValueAnimator animator = ValueAnimator.ofFloat(startValue, endValue);
        animator.setDuration(time);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DecimalFormat decimalFormat=new DecimalFormat("#,##0.00");
                tvChange.setText(decimalFormat.format(animation.getAnimatedValue()));
            }
        });
        animator.start();
    }
}
