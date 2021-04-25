package com.dozen.thirdparty.ad.tt;

import android.app.Activity;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
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
public class TTRewardVideoMode {

    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mIsExpress = true; //是否请求模板广告
    private boolean mIsLoaded = false; //视频是否加载完成
    private boolean isPlayComplete = false; //视频是否播放完成

    private Activity mContext;

    private RewardDetailCallBack rewardVideoCallBack;

    public void setRewardVideoCallBack(RewardDetailCallBack rewardVideoCallBack) {
        this.rewardVideoCallBack = rewardVideoCallBack;
    }

    public TTRewardVideoMode(Activity mContext) {
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
        if (mttRewardVideoAd != null&&mIsLoaded) {
            //step6:在获取到广告后展示,强烈建议在onRewardVideoCached回调后，展示广告，提升播放体验
            //该方法直接展示广告
//                    mttRewardVideoAd.showRewardVideoAd(RewardVideoActivity.this);

            //展示广告，并传入广告展示的场景
            mttRewardVideoAd.showRewardVideoAd(mContext, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
            mttRewardVideoAd = null;
        } else {
            StyleToastUtil.info("请先加载广告");
        }
    }


    private boolean mHasShowDownloadActive = false;

    private void loadAd(final String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot;
        if (mIsExpress) {
            //个性化模板广告需要传入期望广告view的宽、高，单位dp，
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
                    .setExpressViewAcceptedSize(500,500)
                    .build();
        } else {
            //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
            adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .build();
        }
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                MyLog.e("Callback --> onError: " + code + ", " + String.valueOf(message));
                rewardVideoCallBack.isFail(message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                MyLog.e("Callback --> onRewardVideoCached");
                mIsLoaded = true;
                rewardVideoCallBack.loadSuccess();
                StyleToastUtil.info("观看完整视频后可免费使用", 2000);
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                MyLog.e("Callback --> onRewardVideoAdLoad");

//                MyLog.d( "rewardVideoAd loaded 广告类型：" + getAdType(ad.getRewardVideoAdType()));
                mIsLoaded = false;
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        MyLog.i("Callback --> rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        MyLog.i("Callback --> rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        MyLog.i("Callback --> rewardVideoAd close");
                        if (isPlayComplete){
                            rewardVideoCallBack.playComplete();
                        }else {
                            rewardVideoCallBack.isFail("播放未完成");
                        }
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        MyLog.i("Callback --> rewardVideoAd complete");
                        isPlayComplete=true;
                    }

                    @Override
                    public void onVideoError() {
                        MyLog.e("Callback --> rewardVideoAd error");
                        rewardVideoCallBack.isFail("播放失败");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int errorCode, String errorMsg) {
                        String logString = "verify:" + rewardVerify + " amount:" + rewardAmount +
                                " name:" + rewardName + " errorCode:" + errorCode + " errorMsg:" + errorMsg;
                        MyLog.e("Callback --> " + logString);
                        rewardVideoCallBack.onRewardVerify(rewardVerify,rewardAmount,rewardName,errorCode,errorMsg);
                    }

                    @Override
                    public void onSkippedVideo() {
                        MyLog.e("Callback --> rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        MyLog.d("DML"+ "onDownloadActive==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);

                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            MyLog.d( "下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        MyLog.d("DML"+ "onDownloadPaused===totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        rewardVideoCallBack.isFail("下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        MyLog.d("DML"+ "onDownloadFailed==totalBytes=" + totalBytes + ",currBytes=" + currBytes + ",fileName=" + fileName + ",appName=" + appName);
                        rewardVideoCallBack.isFail("下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        MyLog.d("DML"+ "onDownloadFinished==totalBytes=" + totalBytes + ",fileName=" + fileName + ",appName=" + appName);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        MyLog.d("DML"+ "onInstalled==" + ",fileName=" + fileName + ",appName=" + appName);
                        rewardVideoCallBack.downloadFinish(fileName,appName);
                    }
                });
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

    public interface RewardDetailCallBack extends TTADCallBack{
        @Override
        void isFail(String error);

        @Override
        void downloadFinish(String fileName, String appName);

        @Override
        void loadSuccess();

        @Override
        void playComplete();

        void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int errorCode, String errorMsg);
    }

}
