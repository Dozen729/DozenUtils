package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/11
 */
public class UserLoginResult extends HttpResult {

    public Data data;

    public static class Data{

        public UserToken tokeninfo;
        public VipInfoResult.Data userinfo;

        public static class UserToken{
            public String token;
            public long expir_time;
        }
    }
}
