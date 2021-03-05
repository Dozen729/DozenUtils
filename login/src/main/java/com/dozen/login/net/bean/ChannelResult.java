package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/9/25
 */
public class ChannelResult extends HttpResult {

    public Data data;

    //toutiao vivo yingyongbao xiaomi oppo huawei
    public class Data{
        public CSwitch toutiao;
        public CSwitch vivo;
        public CSwitch yingyongbao;
        public CSwitch xiaomi;
        public CSwitch oppo;
        public CSwitch huawei;
        public CSwitch common;
    }

    public class CSwitch{
        public String zf;
        public String ad;
    }
}
