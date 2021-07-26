package com.dozen.commonbase.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.dozen.commonbase.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: Dozen
 * @description:短信接收倒计时工具
 * @time: 2020/11/2
 */
public class SMSSuperCodeUtil {

    private TextView codeTV;
    private int offIcon = R.drawable.bg_sms_code_select;
    private int onIcon = R.drawable.bg_sms_code_un_select;
    private Timer timer;
    private TimerTask timerTask;
    private Activity activity;
    private int second;
    private static final int DEF_TIME_LENGTH = 60;
    private int timeLength = DEF_TIME_LENGTH;
    private String defStr = "获取验证码";

    /**
     * 验证码跳秒。默认60秒结束，停止时文字为 "获取验证码"
     *
     * @param activity
     * @param tv       按钮
     */
    public SMSSuperCodeUtil(Activity activity, TextView tv) {
        codeTV = tv;
        this.activity = activity;
    }

    /**
     * 验证码跳秒。默认60秒结束，停止时文字为 "获取验证码"
     *
     * @param activity
     * @param offIcon  跳秒时的背景
     * @param onIcon   没有跳秒时的背景
     * @param tv       按钮
     */
    public SMSSuperCodeUtil(Activity activity, int offIcon, int onIcon, TextView tv) {
        codeTV = tv;
        this.offIcon = offIcon;
        this.onIcon = onIcon;
        this.activity = activity;
    }

    /**
     * 验证码跳秒。停止时文字为 "获取验证码"
     *
     * @param activity
     * @param offIcon    跳秒时的背景
     * @param onIcon     没有跳秒时的背景
     * @param tv         按钮
     * @param timeLength 倒计时时长（秒）
     */
    public SMSSuperCodeUtil(Activity activity, int offIcon, int onIcon, TextView tv, int timeLength) {
        this(activity, offIcon, onIcon, tv);
        this.timeLength = timeLength;
    }

    /**
     * 验证码跳秒。
     *
     * @param activity
     * @param offIcon    跳秒时的背景
     * @param onIcon     没有跳秒时的背景
     * @param tv         按钮
     * @param timeLength 倒计时时长（秒）
     * @param defStr     停止时的文字
     */
    public SMSSuperCodeUtil(Activity activity, int offIcon, int onIcon, TextView tv, int timeLength, String defStr) {
        this(activity, offIcon, onIcon, tv, timeLength);
    }

    /**
     * 开始
     */
    public void start() {
        stopTimer();
        second = timeLength;
        codeTV.setBackgroundResource(onIcon);
        codeTV.setTextColor(Color.WHITE);
        codeTV.setClickable(false);
        timer = new Timer();
        timerTask = new CodeTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    /**
     * 停止
     */
    public void stop() {
        second = 0;
        stopTimer();
        stopSendCode();//测试
    }

    private void stopSendCode() {
        Log.d("test", "stopSendCode()");
        stopTimer();
        codeTV.setTextColor(Color.WHITE);
        codeTV.setText(defStr);
        codeTV.setClickable(true);
        codeTV.setBackgroundResource(offIcon);
    }


    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    class CodeTimerTask extends TimerTask {

        @Override
        public void run() {
            if (second <= 0) {
                stop();
            } else {
                onWaiting();
            }
        }

        private void stop() {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    stopSendCode();
                }
            });
        }

        private void onWaiting() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    codeTV.setText(String.format("%s秒后重试", second + ""));
                    codeTV.setBackgroundResource(onIcon);
                    second--;
                }
            });
        }
    }

}

