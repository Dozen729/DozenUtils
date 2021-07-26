package com.dozen.commonbase.video;

import android.content.Context;
import android.util.AttributeSet;

import com.dozen.commonbase.R;
import com.dozen.commonbase.utils.MyLog;

import cn.jzvd.JzvdStd;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/7/13
 */
public class MyJzvdStdNoTitleNoClarity extends JzvdStd {

    private boolean isAssets = false;
    public static boolean isMusicState = true;
    public boolean isPause = false;

    public MyJzvdStdNoTitleNoClarity(Context context) {
        super(context);
    }

    public MyJzvdStdNoTitleNoClarity(Context context, AttributeSet attrs) {
        super(context, attrs);
        //关闭wifi弹框检查
        WIFI_TIP_DIALOG_SHOWED = true;
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        bottomContainer.setVisibility(GONE);
        bottomProgressBar.setVisibility(GONE);
    }

    @Override
    public void startPreloading() {
        super.startPreloading();
        loadingProgressBar.setVisibility(GONE);
    }

    @Override
    public void onStatePause() {
        bottomContainer.setVisibility(GONE);
        bottomProgressBar.setVisibility(GONE);
        isPause = true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_jzstd_notitle;
    }

    @Override
    public void startVideo() {
        super.startVideo();
        isPause = false;
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        MyLog.d("onAutoCompletion");
        this.startVideo();
        loadingProgressBar.setVisibility(GONE);
    }

    @Override
    public void onPrepared() {
        loadingProgressBar.setVisibility(GONE);
        if (isAssets) {
            state = STATE_PREPARED;
            if (!preloading) {
                mediaInterface.start();
                preloading = false;
            }
            onStatePlaying();
        } else {
            super.onPrepared();
        }
        setMusic(isMusicState);
    }

    public void setAssets(boolean assets) {
        isAssets = assets;
    }

    public void setMusic(boolean state) {
        isMusicState = state;
        if (state) {
            mediaInterface.setVolume(1f, 1f);
        } else {
            mediaInterface.setVolume(0f, 0f);
        }
    }
}
