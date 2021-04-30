package com.dozen.commonbase.view.other;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.dozen.commonbase.R;

import java.io.File;

import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/4/1
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)//
                .load(Uri.fromFile(new File(path)))//
                .placeholder(R.mipmap.girl_guide)//
                .error(R.mipmap.girl_guide)//
                .centerInside()//
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}