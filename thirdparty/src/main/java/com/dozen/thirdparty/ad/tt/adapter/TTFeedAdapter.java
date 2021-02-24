package com.dozen.thirdparty.ad.tt.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.dozen.thirdparty.R;
import com.dozen.thirdparty.ad.tt.DislikeDialog;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/12/1
 */
public class TTFeedAdapter extends BaseAdapter {
    private static final int ITEM_VIEW_TYPE_NORMAL = 0;
    private static final int ITEM_VIEW_TYPE_GROUP_PIC_AD = 1;
    private static final int ITEM_VIEW_TYPE_SMALL_PIC_AD = 2;
    private static final int ITEM_VIEW_TYPE_LARGE_PIC_AD = 3;
    private static final int ITEM_VIEW_TYPE_VIDEO = 4;
    private static final int ITEM_VIEW_TYPE_VERTICAL_IMG = 5;//竖版图片
    private static final int ITEM_VIEW_TYPE_VIDEO_VERTICAL = 6;//竖版视频

    private int mVideoCount = 0;


    private List<TTNativeExpressAd> mData;
    private Context mContext;

    private Map<AdViewHolder, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();

    public TTFeedAdapter(Context context, List<TTNativeExpressAd> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size(); // for test
    }

    @Override
    public TTNativeExpressAd getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //信息流广告的样式，有大图、小图、组图和视频，通过ad.getImageMode()来判断
    @Override
    public int getItemViewType(int position) {
        TTNativeExpressAd ad = getItem(position);
        if (ad == null) {
            return ITEM_VIEW_TYPE_NORMAL;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_SMALL_IMG) {
            return ITEM_VIEW_TYPE_SMALL_PIC_AD;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_LARGE_IMG) {
            return ITEM_VIEW_TYPE_LARGE_PIC_AD;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_GROUP_IMG) {
            return ITEM_VIEW_TYPE_GROUP_PIC_AD;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VIDEO) {
            return ITEM_VIEW_TYPE_VIDEO;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VERTICAL_IMG) {
            return ITEM_VIEW_TYPE_VERTICAL_IMG;
        } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL) {
            return ITEM_VIEW_TYPE_VIDEO_VERTICAL;
        } else {
//            TToast.show(mContext, "图片展示样式错误");
            return ITEM_VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 7;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TTNativeExpressAd ad = getItem(position);
        switch (getItemViewType(position)) {
            case ITEM_VIEW_TYPE_SMALL_PIC_AD:
            case ITEM_VIEW_TYPE_LARGE_PIC_AD:
            case ITEM_VIEW_TYPE_GROUP_PIC_AD:
            case ITEM_VIEW_TYPE_VERTICAL_IMG:
            case ITEM_VIEW_TYPE_VIDEO:
            case ITEM_VIEW_TYPE_VIDEO_VERTICAL:
                return getVideoView(convertView, parent, ad);
            default:
                return getNormalView(convertView, parent, position);
        }
    }

    //渲染视频广告，以视频广告为例，以下说明
    @SuppressWarnings("RedundantCast")
    private View getVideoView(View convertView, ViewGroup parent, @NonNull final TTNativeExpressAd ad) {
        final AdViewHolder adViewHolder;
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_native_express, parent, false);
                adViewHolder = new AdViewHolder();
                adViewHolder.videoView = (FrameLayout) convertView.findViewById(R.id.iv_listitem_express);
                convertView.setTag(adViewHolder);
            } else {
                adViewHolder = (AdViewHolder) convertView.getTag();
            }

            //绑定广告数据、设置交互回调
            bindData(convertView, adViewHolder, ad);
            if (adViewHolder.videoView != null) {
                //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
                View video = ad.getExpressAdView();
                if (video != null) {
                    adViewHolder.videoView.removeAllViews();
                    if (video.getParent() == null) {
                        adViewHolder.videoView.addView(video);
//                            ad.render();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    /**
     * 非广告list
     *
     * @param convertView
     * @param parent
     * @param position
     * @return
     */
    @SuppressWarnings("RedundantCast")
    @SuppressLint("SetTextI18n")
    private View getNormalView(View convertView, ViewGroup parent, int position) {
        NormalViewHolder normalViewHolder;
        if (convertView == null) {
            normalViewHolder = new NormalViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_normal, parent, false);
            normalViewHolder.idle = (TextView) convertView.findViewById(R.id.text_idle);
            convertView.setTag(normalViewHolder);
        } else {
            normalViewHolder = (NormalViewHolder) convertView.getTag();
        }
        normalViewHolder.idle.setText("ListView item " + position);
        return convertView;
    }

    /**
     * 设置广告的不喜欢，注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(final TTNativeExpressAd ad, boolean customStyle) {
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
                    //用户选择不喜欢原因后，移除广告展示
                    mData.remove(ad);
                    notifyDataSetChanged();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback((Activity) mContext, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
//                TToast.show(mContext, "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                mData.remove(ad);
                notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
//                TToast.show(mContext, "点击取消 ");
            }

            @Override
            public void onRefuse() {

            }
        });
    }

    private void bindData(View convertView, final AdViewHolder adViewHolder, TTNativeExpressAd ad) {
        //设置dislike弹窗，这里展示自定义的dialog
        bindDislike(ad, false);
        switch (ad.getInteractionType()) {
            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                bindDownloadListener(adViewHolder, ad);
                break;
        }
    }


    private void bindDownloadListener(final AdViewHolder adViewHolder, TTNativeExpressAd ad) {
        TTAppDownloadListener downloadListener = new TTAppDownloadListener() {
            private boolean mHasShowDownloadActive = false;

            @Override
            public void onIdle() {
                if (!isValid()) {
                    return;
                }
//                TToast.show(mContext, "点击广告开始下载");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
//                    TToast.show(mContext, appName + " 下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
//                TToast.show(mContext, appName + " 下载暂停", Toast.LENGTH_LONG);

            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
//                TToast.show(mContext, appName + " 下载失败，重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
//                TToast.show(mContext, appName + " 安装完成，点击打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
//                TToast.show(mContext, appName + " 下载成功，点击安装", Toast.LENGTH_LONG);

            }

            @SuppressWarnings("BooleanMethodIsAlwaysInverted")
            private boolean isValid() {
                return mTTAppDownloadListenerMap.get(adViewHolder) == this;
            }
        };
        //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
        ad.setDownloadListener(downloadListener); // 注册下载监听器
        mTTAppDownloadListenerMap.put(adViewHolder, downloadListener);
    }


    private static class AdViewHolder {
        FrameLayout videoView;
    }

    private static class NormalViewHolder {
        TextView idle;
    }
}
