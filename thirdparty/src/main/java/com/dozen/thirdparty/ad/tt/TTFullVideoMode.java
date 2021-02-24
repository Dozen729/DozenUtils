package com.dozen.thirdparty.ad.tt;

import android.app.Activity;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.thirdparty.ad.TPConstant;
import com.dozen.thirdparty.ad.tt.config.TTADCallBack;
import com.dozen.thirdparty.ad.tt.config.TTAdManagerHolder;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/19
 */
public class TTFullVideoMode {

    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;
    private boolean mIsExpress = true; //是否请求模板广告
    private boolean mIsLoaded = false; //视频是否加载完成
    private boolean isPlayComplete = false; //视频是否播放完成

    private Activity mContext;

    private TTADCallBack fullVideoCallBack;

    public void setFullVideoCallBack(TTADCallBack fullVideoCallBack) {
        this.fullVideoCallBack = fullVideoCallBack;
    }

    public TTFullVideoMode(Activity mContext) {
        this.mContext = mContext;
        initMode();
    }

    private void initMode() {
        mIsExpress= TPConstant.TT_AD_IS_EXPRESS;
        mTTAdNative= TTAdManagerHolder.get().createAdNative(mContext);
    }

    public void loadVerticalAD(String mVerticalCodeId){
        loadAd(mVerticalCodeId, TTAdConstant.VERTICAL);
    }

    public void loadHorizontalAD(String mHorizontalCodeId){
        loadAd(mHorizontalCodeId,TTAdConstant.HORIZONTAL);
    }

    public void showFullAD(){
        if (mttFullVideoAd != null&&mIsLoaded) {
            //step6:在获取到广告后展示
            //该方法直接展示广告
            //mttFullVideoAd.showFullScreenVideoAd(FullScreenVideoActivity.this);

            //展示广告，并传入广告展示的场景
            mttFullVideoAd.showFullScreenVideoAd(mContext, TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
            mttFullVideoAd = null;
        } else {
            StyleToastUtil.info("请先加载广告");
        }
    }


    private boolean mHasShowDownloadActive = false;

    @SuppressWarnings("SameParameterValue")
    private void loadAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot;
        if (mIsExpress) {
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
                    .setExpressViewAcceptedSize(500,500)
                    .build();

        } else {
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .build();
        }
        //step5:请求广告
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                MyLog.e( "Callback --> onError: " + code + ", " + String.valueOf(message));
                StyleToastUtil.info(message);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                MyLog.e( "Callback --> onFullScreenVideoAdLoad");

//                StyleToastUtil.info( "FullVideoAd loaded  广告类型：" + getAdType(ad.getFullVideoAdType()));
                mttFullVideoAd = ad;
                mIsLoaded = false;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        MyLog.d( "Callback --> FullVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        MyLog.d( "Callback --> FullVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        MyLog.d( "Callback --> FullVideoAd close");
                        if (isPlayComplete){
                            fullVideoCallBack.playComplete();
                        }else {
                            fullVideoCallBack.isFail("视频播放未完成");
                        }
                    }

                    @Override
                    public void onVideoComplete() {
                        MyLog.d( "Callback --> FullVideoAd complete");
                        isPlayComplete=true;
                    }

                    @Override
                    public void onSkippedVideo() {
                        MyLog.d( "Callback --> FullVideoAd skipped");
//                        fullVideoCallBack.isFail("FullVideoAd skipped");
                    }

                });


                ad.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        MyLog.d( "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
//                            StyleToastUtil.info( "下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        MyLog.d( "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        fullVideoCallBack.isFail("下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        MyLog.d( "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        fullVideoCallBack.isFail("下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        MyLog.d( "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        MyLog.d( "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                        fullVideoCallBack.downloadFinish(fileName,appName);
                    }
                });
            }

            @Override
            public void onFullScreenVideoCached() {
                MyLog.e( "Callback --> onFullScreenVideoCached");
                mIsLoaded = true;
                fullVideoCallBack.loadSuccess();
            }
        });
    }

    private String getAdType(int type) {
        switch (type) {
            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable全屏视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;
        }

        return "未知类型+type=" + type;
    }

}
