package com.dozen.commonbase.mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Chronometer;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Dozen
 * @description:倒计时通用类
 * @time: 2021/1/22
 */
@SuppressLint({"ViewConstructor", "SimpleDateFormat"})
public class TextCountDown extends Chronometer {

    private long mTime;
    private long mNextTime;
    private OnTimeListener mListener;
    private SimpleDateFormat mTimeFormat;
    private String unit;
    private String starText;
    private String endText;
    private Statue statue;
    private String prefix;

    private String runningTime;

    private boolean FirstZero;

    private int TickBackgroundResource;

    private int FinishBackgroundResource;

    private int TickTextColor;

    private int FinishTextColor;

    private int TickBackgroundColor;

    private int FinishBackgroundColor;
    private boolean clickable;

    public enum Statue {
        star, running, finish
    }

    public TextCountDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOnChronometerTickListener(listener);
    }

    //初始化计时时间，单位s
    public TextCountDown init(long _time_s) {
        mTime = mNextTime = _time_s;
        statue = Statue.star;
        return this;
    }

    private void updateTimeText() {
        if (statue == Statue.star) {
            setEmptyTextView(starText);
        } else if (statue == Statue.finish) {
            setEmptyTextView(endText);
        } else {
            String time = "0";
            if (mTimeFormat!=null)
                time = mTimeFormat.format(new Date(mNextTime * 1000));
            if (!FirstZero) {
                if (time.length() > 1) {
                    if (time.indexOf("0") == 0) {
                        runningTime = time.substring(1, time.length());
                    } else {
                        runningTime = time;
                    }
                } else {
                    runningTime = time;
                }
            } else {
                runningTime = time;
            }
            if (TextUtils.isEmpty(prefix)) prefix = "";
            if (TextUtils.isEmpty(unit)) unit = "";
            setText(prefix + runningTime + unit);
        }
    }


    private void setEmptyTextView(String Text){
        if (!TextUtils.isEmpty(Text)) {
            setText(Text);
        } else {
            setText("");
        }
    }

    public void Bulid() {
        updateTimeText();
    }

    //设置计时之前的文本
    public TextCountDown setStarText(String starText) {
        this.starText = starText;
        return this;
    }

    //设置计时完成的文本
    public TextCountDown setEndText(String endText) {
        this.endText = endText;
        return this;
    }

    //设置计时中的数字后面的单位，例如‘秒’,如有特殊需求也可设置其他文本
    public TextCountDown setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    //设置计时中的数字前面的文本
    public TextCountDown setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    //设置当计时数字首位为0时是否去掉0
    public TextCountDown setFirstZero(boolean FirstZero) {
        this.FirstZero = FirstZero;
        return this;
    }

    //设置计时中的背景
    public TextCountDown setTickBackgroundResource(@DrawableRes int resid) {
        this.TickBackgroundResource = resid;
        TickBackgroundColor = 0;
        return this;
    }


    //设置计时结束的背景
    public TextCountDown setFinishBackgroundResource(@DrawableRes int resid) {
        this.FinishBackgroundResource = resid;
        FinishBackgroundColor = 0;
        return this;
    }

    //设置计时中的背景颜色
    public TextCountDown setTickBackgroundColor(@ColorInt int resid) {
        this.TickBackgroundColor = resid;
        TickBackgroundResource = 0;
        return this;
    }


    //设置计时结束的背景颜色
    public TextCountDown setFinishBackgroundColor(@ColorInt int resid) {
        this.FinishBackgroundColor = resid;
        FinishBackgroundResource = 0;
        return this;
    }

    //设置计时中的字体颜色
    public TextCountDown setTickTextColor(@ColorInt int color) {
        this.TickTextColor = color;
        return this;
    }

    //设置计时结束后的字体颜色
    public TextCountDown setFinishTextColor(@ColorInt int color) {
        this.FinishTextColor = color;
        return this;
    }

    //设置在计时中时是否可点击
    public TextCountDown setTickClickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    public void reStart(long time) {
        if (time == -1) {
            mNextTime = mTime;
        } else {
            mTime = mNextTime = time;
        }
        this.start();
    }

    public void reStart() {
        reStart(-1);
    }

    public void onStart() {
        this.start();
    }

    public void onResume() {
        this.start();
    }

    public void onPause() {
        this.stop();
    }

    //设置计时数字格式，可选‘SECONDS’或‘MINUTES’
    public TextCountDown setTimeFormat(String pattern) {
        mTimeFormat = new SimpleDateFormat(pattern);
        return this;
    }

    //设置计时中和计时完成后的事件
    public TextCountDown setOnTimeListener(OnTimeListener l) {
        mListener = l;
        return this;
    }

    //设置点击事件
    public TextCountDown setClickListener(OnClickListener l) {
        setOnClickListener(l);
        return this;
    }


    OnChronometerTickListener listener = new OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            if (mNextTime <= 0) {    //finish
                mNextTime = 0;
                stop();
                if (null != mListener) mListener.onTimeComplete();
                statue = Statue.finish;
                if (FinishBackgroundResource != 0) setBackgroundResource(FinishBackgroundResource);
                if (FinishBackgroundColor != 0) setBackgroundColor(FinishBackgroundColor);
                if (FinishTextColor != 0) setTextColor(FinishTextColor);
                if (!clickable) setClickable(true);
            }
            mNextTime--;
            if (mNextTime > 0) {      //Tick
                statue = Statue.running;
                if (TickBackgroundResource != 0) setBackgroundResource(TickBackgroundResource);
                if (TickBackgroundColor != 0) setBackgroundColor(TickBackgroundColor);
                if (TickTextColor != 0) setBackgroundColor(TickTextColor);
                if (null != mListener) mListener.onTimeRunning(mNextTime);
                setClickable(clickable);
            }
            updateTimeText();
        }
    };


    public static final String SECONDS = "ss";
    public static final String MINUTES = "mm";


    interface OnTimeListener {
        void onTimeComplete();

        void onTimeRunning(long mNextTime);
    }

    /**********自定义方法区,可根据项目具体修改定制*************/
    public TextCountDown buildSmsDefault(long time) {
        init(time).setTimeFormat(SECONDS)
                .setStarText("发送验证码")
                .setEndText("重新发送")
                .setUnit("秒")
                .setTickBackgroundColor(Color.GRAY)
                .setFinishBackgroundColor(Color.BLUE)
                .setTickClickable(false)
                .Bulid();
        return this;
    }

    public TextCountDown buildSkipDefault(long time){
        init(time).setTimeFormat(TextCountDown.SECONDS)
                .setPrefix("跳过(")
                .setUnit(")")
                .setFinishBackgroundColor(Color.BLUE)
                .Bulid();
        return this;
    }

}