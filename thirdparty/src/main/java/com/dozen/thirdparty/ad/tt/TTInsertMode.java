package com.dozen.thirdparty.ad.tt;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.dozen.thirdparty.ad.TPConstant;
import com.dozen.thirdparty.ad.tt.config.TTADCallBack;
import com.dozen.thirdparty.ad.tt.config.TTAdManagerHolder;

import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/19
 */
public class TTInsertMode {
    private TTAdNative mTTAdNative;
    //是否强制跳转到主页面

    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3000;
    private String mCodeId = TPConstant.TT_AD_INSERT_VIDEO;
    private boolean mIsExpress = false; //是否请求模板广告

    private Activity mContext;

    private TTAdDislike mTTAdDislike;
    private TTNativeExpressAd mTTAd;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;


    private TTADCallBack insertCallback;

    public void setInsertCallback(TTADCallBack insertCallback) {
        this.insertCallback = insertCallback;
    }

    public TTInsertMode(Activity mContext) {
        this.mContext = mContext;
        initSplash();
    }

    private void initSplash() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        mIsExpress = TPConstant.TT_AD_IS_EXPRESS;
    }

    public void setViewAndCode(String codeID) {
        this.mCodeId = codeID;
        loadExpressAd(mCodeId, 300, 300);
    }

    public void setViewAndCode(String codeID, int width, int height) {
        this.mCodeId = codeID;
        loadExpressAd(mCodeId, width, height);
    }

    private void loadExpressAd(String codeId, int expressViewWidth, int expressViewHeight) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(InteractionExpressActivity.this, "load error : " + code + ", " + message);
                insertCallback.isFail(message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
//                TToast.show(mContext, "load success !");
                insertCallback.loadSuccess();
            }
        });
    }

    public void showAd() {
        if (mTTAd != null) {
            mTTAd.render();
        } else {
//            TToast.show(mContext,"请先加载广告");
        }
    }


    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
//                TToast.show(mContext, "广告关闭");
                insertCallback.isFail("关闭");
            }

            @Override
            public void onAdClicked(View view, int type) {
//                TToast.show(mContext, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
//                TToast.show(mContext, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
//                TToast.show(mContext, msg + " code:" + code);
                insertCallback.isFail(msg);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
//                TToast.show(mContext, "渲染成功");
                mTTAd.showInteractionExpressAd(mContext);

            }
        });
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                TToast.show(InteractionExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
                mHasShowDownloadActive = false;
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
//                    TToast.show(InteractionExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
                insertCallback.isFail("download fail");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(mContext, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
//                    TToast.show(mContext, "点击 " + filterWord.getName());
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(mContext, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
//                TToast.show(mContext, "\t\t\t\t\t\t\t感谢您的反馈!\t\t\t\t\t\t\n我们将为您带来更优质的广告体验", 3);
            }

            @Override
            public void onCancel() {
//                TToast.show(mContext, "点击取消 ");
            }

            @Override
            public void onRefuse() {
//                TToast.show(mContext, "您已成功提交反馈，请勿重复提交哦！", 3);
            }

        });
    }

    public void closeAd() {
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }

}
