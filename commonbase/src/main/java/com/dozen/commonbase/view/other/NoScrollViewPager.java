package com.dozen.commonbase.view.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/23
 */
public class NoScrollViewPager extends ViewPager {
    // the sliding page switch
    private boolean isSlidingEnable = true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写此函数
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return  this.isSlidingEnable;
    }
    //重写此函数
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isSlidingEnable;
    }

    public void setSlidingEnable(boolean slidingEnable) {
        isSlidingEnable = slidingEnable;
    }
}
