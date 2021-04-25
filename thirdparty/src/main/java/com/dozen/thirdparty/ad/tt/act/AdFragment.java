package com.dozen.thirdparty.ad.tt.act;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.thirdparty.R;
import com.dozen.thirdparty.ad.TPConstant;
import com.dozen.thirdparty.ad.tt.adapter.LoadMoreListView;
import com.dozen.thirdparty.ad.tt.adapter.TTFeedAdapter;
import com.dozen.thirdparty.ad.tt.config.TTAdManagerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/4/3
 */
public class AdFragment extends BaseFragment {

    public static final String KEY_TEXT = "mine";

    private Activity activity;

    public Context getContext() {
        if (activity == null) {
            return APPBase.getApplication();
        }
        return activity;
    }

    public static AdFragment newInstance(String text) {
        AdFragment mineFragment = new AdFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_list_ad;
    }

    @Override
    protected void setUpView() {
    }

    @Override
    protected void setUpData() {
        initListView();
    }

    //广告
    private LoadMoreListView mListView;
    private TTAdNative mTTAdNative;
    private List<TTNativeExpressAd> mData;
    private TTFeedAdapter ttFeedAdapter;
    private static final int LIST_ITEM_COUNT = 30;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    //广告加载
    @SuppressWarnings("RedundantCast")
    private void initListView() {
        MyLog.d("addadshow");
        mListView = getContentView().findViewById(R.id.lmrv_mine_ad_show);

        mTTAdNative = TTAdManagerHolder.get().createAdNative(getActivity());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getActivity());

        mData = new ArrayList<>();
        ttFeedAdapter = new TTFeedAdapter(getActivity(), mData);
        mListView.setAdapter(ttFeedAdapter);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadListAd();
            }
        }, 0);
    }

    /**
     * 加载feed广告
     */
    private void loadListAd() {
        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(TPConstant.TT_AD_EXPRESS_LIST)
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(300, 400) //期望模板广告view的size,单位dp
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        //step5:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                if (mListView != null) {
                    mListView.setLoadingFinish();
                }
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (mListView != null) {
                    mListView.setLoadingFinish();
                }
                if (ads == null || ads.isEmpty()) {
                    return;
                }
                bindAdListener(ads);
            }
        });
    }

    private void bindAdListener(final List<TTNativeExpressAd> ads) {
        final int count = mData.size();
        for (TTNativeExpressAd ad : ads) {
            final TTNativeExpressAd adTmp = ad;
            int random = (int) (Math.random() * LIST_ITEM_COUNT) + count - LIST_ITEM_COUNT;
            mData.add(ad);

            ttFeedAdapter.notifyDataSetChanged();

            adTmp.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                @Override
                public void onAdClicked(View view, int type) {
//                    TToast.show(NativeExpressListActivity.this, "广告被点击");
                }

                @Override
                public void onAdShow(View view, int type) {
//                    TToast.show(NativeExpressListActivity.this, "广告展示");
                    if (adCallBack!=null){
                        adCallBack.success();
                    }
                }

                @Override
                public void onRenderFail(View view, String msg, int code) {
//                    TToast.show(NativeExpressListActivity.this, msg + " code:" + code);
                }

                @Override
                public void onRenderSuccess(View view, float width, float height) {
                    //返回view的宽高 单位 dp
//                    TToast.show(NativeExpressListActivity.this, "渲染成功");
                    ttFeedAdapter.notifyDataSetChanged();
                }
            });
            ad.render();

        }

    }

    private AdCallBack adCallBack;

    public void setAdCallBack(AdCallBack adCallBack) {
        this.adCallBack = adCallBack;
    }

    public interface AdCallBack{
        void success();
        void fail();
    }
}
