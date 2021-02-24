package com.dozen.commonbase.utils;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/12/4
 */
public class TextUtils {


    //一个TextView显示三种不同颜色的字（开始，中间，结束）
    public static SpannableStringBuilder chargeTextCenterColor(String startText, String centerText, String endText, String startColor, String centerColor, String endColor) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(startText);
        SpannableStringBuilder ssb2 = new SpannableStringBuilder(centerText);
        SpannableStringBuilder ssb3 = new SpannableStringBuilder(endText);
        ForegroundColorSpan fcs1 = new ForegroundColorSpan(Color.parseColor(startColor));
        ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.parseColor(centerColor));
        ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.parseColor(endColor));

        ssb.setSpan(fcs1, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb2.setSpan(fcs2, 0, ssb2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb3.setSpan(fcs3, 0, ssb3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ssb2).append(ssb3);
        return ssb;
    }

    public static String intToWan(int data){
        String result=data+"";
        if (data>=10000){
            result=(float)data/10000+"万";
        }
        return result;
    }

}
