package com.dozen.utils.bean;

import org.litepal.crud.DataSupport;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/6/3
 */
public class SharesBean extends DataSupport {

    private int id;
    private int sid;
    private String time;
    private String code;
    private String name;
    private float price;
    private float upup;
    private int ranking;
    private String detail;
    private String type;

    public SharesBean() {
    }

    public SharesBean(int id, int sid, String time, String code, String name, float price, float upup, int ranking, String detail, String type) {
        this.id = id;
        this.sid = sid;
        this.time = time;
        this.code = code;
        this.name = name;
        this.price = price;
        this.upup = upup;
        this.ranking = ranking;
        this.detail = detail;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getUpup() {
        return upup;
    }

    public void setUpup(float upup) {
        this.upup = upup;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
