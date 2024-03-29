package com.dozen.thirdparty.ad.tt.act;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.act.BaseFragment;
import com.dozen.thirdparty.R;
import com.dozen.thirdparty.ad.TPConstant;
import com.dozen.thirdparty.ad.tt.DislikeDialog;
import com.dozen.thirdparty.ad.tt.config.TTAdManagerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/6/7
 */
public class AdBannerFragment extends BaseFragment {

    public static final String KEY_TEXT = "banner";

    private Activity activity;

    public Context getContext() {
        if (activity == null) {
            return APPBase.getApplication();
        }
        return activity;
    }

    public static AdBannerFragment newInstance(String text) {
        AdBannerFragment adBannerFragment = new AdBannerFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        adBannerFragment.setArguments(args);
        return adBannerFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_banner_ad;
    }

    @Override
    protected void setUpView(Bundle savedInstanceState) {

    }

    @Override
    protected void setUpData() {
        if (!TPConstant.ttAdBannerShow){
            return;
        }
        initListView();
    }


    private TTAdNative mTTAdNative;
    private FrameLayout mExpressContainer;
    private TTNativeExpressAd mTTAd;
    private List<AdSizeModel> mBannerAdSizeModelList;
    private long startTime = 0;
    private boolean mHasShowDownloadActive = false;

    private void initListView() {

        mExpressContainer = (FrameLayout) getContentView().findViewById(R.id.express_container);

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(getActivity());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getActivity());

        loadExpressAd(TPConstant.TT_AD_BANNER,320,75);
    }

    private void initData() {
        mBannerAdSizeModelList = new ArrayList<>();
        mBannerAdSizeModelList.add(new AdSizeModel("600*90", 300, 45, "901121246"));
        mBannerAdSizeModelList.add(new AdSizeModel("600*150", 300, 75, "901121700"));
        mBannerAdSizeModelList.add(new AdSizeModel("600*260", 300, 130, "901121148"));
        mBannerAdSizeModelList.add(new AdSizeModel("600*300", 300, 150, "945509757"));
        mBannerAdSizeModelList.add(new AdSizeModel("600*400", 300, 200, "945509751"));
        mBannerAdSizeModelList.add(new AdSizeModel("640*100", 320, 50, "901121223"));
        mBannerAdSizeModelList.add(new AdSizeModel("690*388", 345, 194, "945509738"));
        mBannerAdSizeModelList.add(new AdSizeModel("600*500", 300, 250, "945509744"));
    }

    private void loadExpressAd(String codeId, int expressViewWidth, int expressViewHeight) {
        mExpressContainer.removeAllViews();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(BannerExpressActivity.this, "load error : " + code + ", " + message);
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
//                TToast.show(mContext,"load success!");
            }
        });
    }

    public void onClickShowBanner() {
        if (mTTAd != null) {
            mTTAd.render();
        } else {
//            TToast.show(mContext, "请先加载广告..");
        }
    }


    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
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
//                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
//                TToast.show(mContext, msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
//                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
//                TToast.show(mContext, "渲染成功");
                mExpressContainer.removeAllViews();
                mExpressContainer.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                TToast.show(BannerExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
//                    TToast.show(BannerExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
            }
        });

        onClickShowBanner();
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getDislikeInfo().getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(getActivity(), words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
//                    TToast.show(mContext, "点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    mExpressContainer.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {

            @Override
            public void onShow() {

            }

            @Override
            public void onSelected(int i, String s, boolean b) {
//                TToast.show(mContext, "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onCancel() {
//                TToast.show(mContext, "点击取消 ");
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }


    public static class AdSizeModel {
        public AdSizeModel(String adSizeName, int width, int height, String codeId) {
            this.adSizeName = adSizeName;
            this.width = width;
            this.height = height;
            this.codeId = codeId;
        }

        public String adSizeName;
        public int width;
        public int height;
        public String codeId;
    }

}
