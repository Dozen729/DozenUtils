package com.dozen.utils.bean;

import com.daimajia.androidanimations.library.Techniques;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/19
 */
public class CommonBean {

    private int id;
    private String name;
    private String tip;
    private Class cls;
    private Techniques techniques;
    private boolean show;

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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public Techniques getTechniques() {
        return techniques;
    }

    public void setTechniques(Techniques techniques) {
        this.techniques = techniques;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
