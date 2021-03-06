package com.dozen.thirdparty.ad.game;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.cmcm.cmgame.IImageLoader;

/**
 * Created by lingyunxiao on 2019-03-04
 *
 * 这个类是 sdk 将 ImageLoader 的外置的接口实现。
 * 因为 sdk 使用的 Glide 版本较老，为了避免与接入方使用不同版本导致的冲突，所以将这个功能延迟到 sdk 外面来提供。
 * 如果不存在冲突问题，使用这个默认的实现即可。GlideDrawableImageViewTarget(imageView)
 */
public class CmGameImageLoader implements IImageLoader {
    @Override
    public void loadImage(Context context, String imageUrl, final ImageView imageView, int defRsid) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(defRsid)
                .error(defRsid)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(new ImageViewTarget<Drawable>(imageView) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        imageView.setImageDrawable(resource);
                    }
                });

//        建议加上placeHolder方法，否则新安装启动会出现闪现的情况，新版本没有placeHolder的可以这么写
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(defRsid);
//        Glide.with(context).load(imageUrl).apply(requestOptions).into(imageView);
    }
}
