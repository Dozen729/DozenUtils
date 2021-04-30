package com.dozen.commonbase.view.other;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dozen.commonbase.view.RoundImageView;
import com.youth.banner.loader.ImageLoader;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/12
 */
public class RoundImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    /**
     * 自定义圆角类
     * @param context
     * @return
     */
    @Override
    public ImageView createImageView(Context context) {
        RoundImageView img = new RoundImageView(context);
        return img;

    }
}
