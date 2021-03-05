package com.dozen.login.net.bean;

import com.dozen.commonbase.http.HttpResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/10/15
 */
public class VipInfoResult extends HttpResult {

    public Data data;

    public static class Data{
        public int is_vip; //1为vip
        public String id;
        public String uuid;
        public String user_id;
        public String register_time;
        public String last_login_time;
        public String last_login_ip;
        public String vip_expire_time;
        public String channel;
        public String device;
        public String version;
        public String mobile;
        public String user_account;
        public String password;
        public String openid;//微信是否绑定
        public String received;//18元奖励是否获取
        public String headimgurl;
        public String code;
        public String user_nickname;
        public String is_login;

        public String ip;
        public String is_reg;//1不可以上报，0可以上报
    }
}
