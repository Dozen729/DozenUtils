package com.dozen.commonbase.utils;

import android.os.SystemClock;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/3
 */
public class CommonUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_MIN_CLICK_DELAY_TIME = 200;
    private static final int MIN_CLICK_DELAY_TIME = 300;
    private static final int CENTER_CLICK_DELAY_TIME = 500;
    private static final int LONG_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime = 0;
    private static final long DURATION = 3 * 1000;//规定有效时间
    private static long[] mHits = new long[5];//连续点击5次
    private static long[] mHits2 = new long[2];//连续点击2次

    public static boolean isFastFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isCenterFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= CENTER_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isLongFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= LONG_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    //连续点击,mHits次数
    public static boolean isContinueFiveClick() {
        /**
         * 实现连续点击方法
         * src 拷贝的源数组
         * srcPos 从源数组的那个位置开始拷贝.
         * dst 目标数组
         * dstPos 从目标数组的那个位子开始写数据
         * length 拷贝的元素的个数
         */
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        return mHits[0] >= (SystemClock.uptimeMillis() - DURATION);
    }

    //连续点击,mHits次数
    public static boolean isContinueTwoClick() {
        System.arraycopy(mHits2, 1, mHits2, 0, mHits2.length - 1);
        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
        mHits2[mHits2.length - 1] = SystemClock.uptimeMillis();
        return mHits2[0] >= (SystemClock.uptimeMillis() - DURATION);
    }
}
