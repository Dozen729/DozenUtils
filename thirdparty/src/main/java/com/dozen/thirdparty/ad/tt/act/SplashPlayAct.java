package com.dozen.thirdparty.ad.tt.act;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.dozen.commonbase.act.CommonActivity;
import com.dozen.thirdparty.R;
import com.dozen.thirdparty.ad.TPConstant;
import com.dozen.thirdparty.ad.tt.TTSplashMode;
import com.dozen.thirdparty.ad.tt.config.TTADCallBack;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/12/18
 */
public class SplashPlayAct extends CommonActivity {

    private FrameLayout splashContainer;

    @Override
    protected int setLayout() {
        return R.layout.activity_splash_ad;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        splashContainer = findViewById(R.id.al_tt_ad_show);
    }

    @Override
    protected void initData() {
        if (!TPConstant.ttAdSplashShow){
            finish();
            return;
        }
        openADAct();
    }

    //跳转广告
    private void openADAct() {
        TTSplashMode ttSplashMode = new TTSplashMode(this);
        ttSplashMode.setSplashCallback(new TTADCallBack() {
            @Override
            public void isFail(String error) {
                finish();
            }

            @Override
            public void downloadFinish(String fileName, String appName) {

            }

            @Override
            public void loadSuccess() {

            }

            @Override
            public void playComplete() {
                finish();
            }
        });
        ttSplashMode.setViewAndCode(splashContainer, TPConstant.TT_AD_SPLASH);
    }
}
