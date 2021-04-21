package com.dozen.commonbase.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.chad.library.adapter.base.entity.JSectionEntity;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/24
 */
public abstract class BaseBean extends JSectionEntity {

    private int id;
    private String name;
    private String detail;
    private String picture;
    private int icon;
    private Drawable drawable;
    private Bitmap bitmap;
    private boolean isOpen;
    private boolean isShow;

    public BaseBean() {
    }

    public BaseBean(int id, String name, String detail, String picture, int icon, Drawable drawable, Bitmap bitmap, boolean isOpen, boolean isShow) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.picture = picture;
        this.icon = icon;
        this.drawable = drawable;
        this.bitmap = bitmap;
        this.isOpen = isOpen;
        this.isShow = isShow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
