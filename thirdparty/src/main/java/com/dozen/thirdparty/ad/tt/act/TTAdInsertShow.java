package com.dozen.thirdparty.ad.tt.act;

import android.app.Activity;

import com.dozen.thirdparty.ad.TPConstant;
import com.dozen.thirdparty.ad.tt.TTInsertMode;
import com.dozen.thirdparty.ad.tt.config.TTADCallBack;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/6/7
 */
public class TTAdInsertShow {

    public static boolean isBack = false;//设置后台返回为true

    private static boolean oneShow = false;//控制只展示一个插屏广告
    private static boolean twoShow = false;//控制只展示一个插屏广告

    //后台播放插屏广告
    public static void backPlayInsert(Activity activity) {
        if (!TPConstant.ttAdInsertShow){
            return;
        }
        if (oneShow || !isBack) {
            return;
        }
        isBack = false;
        oneShow = true;
        TTInsertMode ttInsertMode = new TTInsertMode(activity);
        ttInsertMode.setInsertCallback(new TTADCallBack() {
            @Override
            public void isFail(String error) {
                oneShow = false;
                ttInsertMode.closeAd();
            }

            @Override
            public void downloadFinish(String fileName, String appName) {

            }

            @Override
            public void loadSuccess() {
                ttInsertMode.showAd();
            }

            @Override
            public void playComplete() {
                oneShow = false;
            }
        });
        ttInsertMode.setViewAndCode(TPConstant.TT_AD_INSERT_VIDEO, 300, 500);
    }

    //播放插屏广告
    public static void playInsertTTAD(Activity activity) {
        if (!TPConstant.ttAdInsertShow){
            return;
        }
        if (twoShow) {
            return;
        }
        twoShow = true;
        TTInsertMode ttInsertMode = new TTInsertMode(activity);
        ttInsertMode.setInsertCallback(new TTADCallBack() {
            @Override
            public void isFail(String error) {
                twoShow = false;
                ttInsertMode.closeAd();
            }

            @Override
            public void downloadFinish(String fileName, String appName) {

            }

            @Override
            public void loadSuccess() {
                ttInsertMode.showAd();
            }

            @Override
            public void playComplete() {
                twoShow = false;
            }
        });
        ttInsertMode.setViewAndCode(TPConstant.TT_AD_INSERT_VIDEO_TWO, 300, 300);
    }
}
