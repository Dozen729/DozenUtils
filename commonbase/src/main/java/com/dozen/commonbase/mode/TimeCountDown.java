package com.dozen.commonbase.mode;

import android.app.Activity;

import com.dozen.commonbase.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: Dozen
 * @description:计算已经过去的时间的毫秒
 * @time: 2020/11/24
 */
public class TimeCountDown {

    private Activity activity;

    private Timer timer;
    private TimerTask timerTask;
    private long nowTime = 1;
    private int stepTimeLength = 1000;//毫秒

    private int maxTimeLimit = 86400000;//一天时间的毫秒
    private long addTimeTotal = stepTimeLength;

    public TimeCountDown(Activity activity) {
        this.activity = activity;
        nowTime=TimeUtil.getTodaySurplusMillis();
    }

    /**
     * 开始
     */
    public void start() {
        stopTimer();

        maxTimeLimit -= TimeUtil.getTodaySurplusMillis();

        timer = new Timer();
        timerTask = new TimeCountDown.CodeTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, stepTimeLength);
    }

    /**
     * 停止
     */
    public void stop() {
        stopTimer();
        stopSendCode();//测试
    }

    private void stopSendCode() {
        stopTimer();
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
            if (addTimeTotal >= maxTimeLimit) {
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
                    addTimeTotal += stepTimeLength;
                    nowTime+=stepTimeLength;
                    if (randomCallback != null) {
                        randomCallback.numberTotal(nowTime);
                    }
                }
            });
        }
    }

    private RandomCallback randomCallback;

    public void setRandomCallback(RandomCallback randomCallback) {
        this.randomCallback = randomCallback;
    }

    public interface RandomCallback {
        void numberTotal(long total);
    }
}
