package com.dozen.commonbase.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author: Dozen
 * @description: 时间工具
 * @time: 2020/11/16
 */
public class TimeUtil {

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /*
     * 转换时间格式
     * */
    public static String getTimeForLong(long longTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date(longTime * 1000);
        //格式long型转成Date型，再转成String：  1464710394 -> ltime2*1000 -> 2016-05-31 23:59:54

        return dataFormat.format(date);
    }

    /*
     * 转换时间格式20201124
     * */
    public static String getTimeForSpecialFormatFirst() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /*
     * 转换时间格式HH-mm-ss
     * */
    public static String getTimeForSpecialFormatSecond() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm-ss");// HH-mm-ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /*
     * 获取当前剩余时间
     * */
    @SuppressLint("SimpleDateFormat")
    public static String getDaySurplusTime() {

        Date d1 = new Date(System.currentTimeMillis());

        long totalTime = 86400000;
        long nowTime = getTodaySurplusMillis();
        long time = d1.getTime() - nowTime;
        long surplusTime = time + (totalTime - nowTime);

        SimpleDateFormat format1 = new SimpleDateFormat(" hh 时 mm 分 ss 秒");

        return format1.format(surplusTime);
    }

    /*
     * 转换当前剩余时间
     * nowTime当天过去的毫秒数
     * */
    @SuppressLint("SimpleDateFormat")
    public static String getSurplusTimeToHMSTwo(long nowTime) {

        Date d1 = new Date(System.currentTimeMillis());

        long totalTime = 86400000;
        long time = d1.getTime() - nowTime;
        long surplusTime = time + (totalTime - nowTime);

        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");

        return format1.format(surplusTime);
    }

    /*
     * 转换当前剩余时间
     * nowTime当天过去的毫秒数
     * */
    @SuppressLint("SimpleDateFormat")
    public static String getSurplusTimeToHMS(long nowTime) {

        Date d1 = new Date(System.currentTimeMillis());

        long totalTime = 86400000;
        long time = d1.getTime() - nowTime;
        long surplusTime = time + (totalTime - nowTime);

        SimpleDateFormat format1 = new SimpleDateFormat(" HH 时 mm 分 ss 秒");

        return format1.format(surplusTime);
    }

    /*
     * 转换当前剩余时间 毫秒数
     * */
    @SuppressLint("SimpleDateFormat")
    public static String getUserSurplusTimeToHMS(long nowTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        // time为转换格式后的字符串
        String time = dateFormat.format(new Date(nowTime));

        return time;
    }

    /*
     * 获取当天过去的毫秒数
     * */
    public static long getTodaySurplusMillis() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date d1 = new Date(System.currentTimeMillis());
        String da = format.format(d1);

        String[] dl = da.split(":");

        int h = Integer.parseInt(dl[0]);
        int m = Integer.parseInt(dl[1]);
        int s = Integer.parseInt(dl[2]);

        long nowTime = (h * 60 * 60 + m * 60 + s) * 1000;
        return nowTime;
    }

    /**
     * 将时间戳解析成日期
     *
     * @param timeInMillis
     * @return 年月日
     */
    public static String parseDate(long timeInMillis) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        return formatDate.format(date);
    }

    /**
     * 将时间戳解析成日期
     *
     * @param timeInMillis
     * @return 年月日 时分秒
     */
    public static String parseDateTime(long timeInMillis) {
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        return formatDateTime.format(date);
    }
}
