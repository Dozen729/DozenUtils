package com.dozen.commonbase.bean;

import java.io.Serializable;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/27
 */
public class GuideBean implements Serializable {
    private int id;
    private boolean isComplete;
    private String title;
    private String detail;
    private String confirmName;
    private int picture;
    private int bgPicture;

    public GuideBean(int id, boolean isComplete, String title, String detail, String confirmName, int picture, int bgPicture) {
        this.id = id;
        this.isComplete = isComplete;
        this.title = title;
        this.detail = detail;
        this.confirmName = confirmName;
        this.picture = picture;
        this.bgPicture = bgPicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getConfirmName() {
        return confirmName;
    }

    public void setConfirmName(String confirmName) {
        this.confirmName = confirmName;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getBgPicture() {
        return bgPicture;
    }

    public void setBgPicture(int bgPicture) {
        this.bgPicture = bgPicture;
    }
}
