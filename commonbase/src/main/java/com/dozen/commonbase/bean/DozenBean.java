package com.dozen.commonbase.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/4/19
 */
public class DozenBean extends BaseBean{

    public DozenBean() {
    }

    public DozenBean(int id, String name, String detail, String picture, int icon, Drawable drawable, Bitmap bitmap, boolean isOpen, boolean isShow) {
        super(id, name, detail, picture, icon, drawable, bitmap, isOpen, isShow);
    }

    @Override
    public boolean isHeader() {
        return false;
    }
}
