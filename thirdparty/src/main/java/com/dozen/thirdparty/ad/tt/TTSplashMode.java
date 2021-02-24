package com.dozen.thirdparty.ad.tt;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.MainThread;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.thirdparty.ad.TPConstant;
import com.dozen.thirdparty.ad.tt.config.TTADCallBack;
import com.dozen.thirdparty.ad.tt.config.TTAdManagerHolder;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/19
 */
public class TTSplashMode {
    private TTAdNative mTTAdNative;
    private FrameLayout mSplashContainer;
    //是否强制跳转到主页面

    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3000;
    private String mCodeId = "887403081";
    private boolean mIsExpress = false; //是否请求模板广告

    private Activity mContext;

    private TTADCallBack splashCallback;

    public void setSplashCallback(TTADCallBack splashCallback) {
        this.splashCallback = splashCallback;
    }

    public TTSplashMode(Activity mContext) {
        this.mContext = mContext;
        initSplash();
    }

    private void initSplash() {
        mTTAdNative= TTAdManagerHolder.get().createAdNative(mContext);
        mIsExpress= TPConstant.TT_AD_IS_EXPRESS;
    }

    public void setViewAndCode(FrameLayout mSplashContainer,String codeID){
        this.mSplashContainer=mSplashContainer;
        this.mCodeId=codeID;
        loadSplashAd();
    }

    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = null;
        if (mIsExpress) {
            //个性化模板广告需要传入期望广告view的宽、高，单位dp，请传入实际需要的大小，
            //比如：广告下方拼接logo、适配刘海屏等，需要考虑实际广告大小
            //float expressViewWidth = UIUtils.getScreenWidthDp(this);
            //float expressViewHeight = UIUtils.getHeight(this);
            adSlot = new AdSlot.Builder()
                    .setCodeId(mCodeId)
                    //模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
                    //view宽高等于图片的宽高
                    .setExpressViewAcceptedSize(1080,1920)
                    .build();
        } else {
            adSlot = new AdSlot.Builder()
                    .setCodeId(mCodeId)
                    .setImageAcceptedSize(1080, 1920)
                    .build();
        }

        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                MyLog.d( String.valueOf(message));
                splashCallback.isFail(message);
            }

            @Override
            @MainThread
            public void onTimeout() {
                splashCallback.isFail("开屏广告加载超时");
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                MyLog.d( "开屏广告请求成功");
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null && mSplashContainer != null && mContext!=null) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                }else {
                    splashCallback.isFail("初始化失败");
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        MyLog.d( "onAdClicked");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        MyLog.d( "onAdShow");
                    }

                    @Override
                    public void onAdSkip() {
                        MyLog.d( "onAdSkip");
                        splashCallback.playComplete();
                    }

                    @Override
                    public void onAdTimeOver() {
                        MyLog.d( "onAdTimeOver");
                        splashCallback.playComplete();
                    }
                });
                if(ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                MyLog.d("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            splashCallback.isFail("下载暂停");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            splashCallback.isFail("下载失败");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                            MyLog.d("下载完成...");
                            splashCallback.downloadFinish(fileName,appName);
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                            MyLog.d("安装完成...");
                            splashCallback.playComplete();
                        }
                    });
                }
            }
        }, AD_TIME_OUT);
    }
}
