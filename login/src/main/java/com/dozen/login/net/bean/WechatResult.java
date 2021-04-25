package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/31
 */
public class WechatResult extends HttpResult {

    public Data data;

    public static class Data{
        public String sign;
    }

}
