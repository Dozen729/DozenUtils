package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

import java.util.List;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/9/25
 */
public class BannerPicResult extends HttpResult {

    public Data data;

    public class Data{
        public List<BannerPicBean> bottom;
        public List<BannerPicBean> middle;
    }

    public class BannerPicBean{
        public int id;
        public String url;
        public String img;
        public String link;
        public int type;
        public String name;
        public String device;
        public String project;
    }
}
