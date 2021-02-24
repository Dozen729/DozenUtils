package com.dozen.utils.bean;

import androidx.fragment.app.Fragment;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class MainItemBean {
    private int id;
    private int type;
    private String name;
    private String tip;
    private String detail;
    private Fragment fragment;
    private int picture;
    private boolean check;
    private boolean show;

    public MainItemBean() {
    }

    public MainItemBean(int id, int type, String name, String tip, String detail, Fragment fragment, int picture, boolean check, boolean show) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.tip = tip;
        this.detail = detail;
        this.fragment = fragment;
        this.picture = picture;
        this.check = check;
        this.show = show;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
