package com.dozen.login.net;

import com.dozen.commonbase.http.HttpConstant;

public class LoginUserUrl {

    //拼接
    public static final String SPLICING_INDEX = "/";
    public static final String SPLICING_STATIC = "/static/";

    //使用条款
    public static final String terms_for_use = "https://mpa.52ck.club/UseTerms_Dingweixia.html";

    //隐私政策
    public static final String user_agreement = "https://mpa.52ck.club/PrivacyPolicy_Dingweixia.html";

    //付费会员协议
    public static final String user_pay_agreement = "https://mpa.52ck.club/Membershi_Dingweixia.html";

    //轮播图
    public static final String get_banner_pic = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "ad";

    //获取渠道号
    public static final String get_channel = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "channel";

    //用户信息
    public static final String get_userinfo = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "userinfo";

    //注册
    public static final String register = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "register";

    //获取验证码
    public static final String send_sms = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "sendSms";
    //登录手机
    public static final String mobile_login = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "login";
    //退出手机登录
    public static final String exit_login = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "loginOut";
    //重置密码
    public static final String re_password = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "resetPwd";

    //h5支付
    public static String html_pay = HttpConstant.MPYS_API_BASE_URL + SPLICING_STATIC + "txuna.html";
    // 获取预付订单信息
    public static final String get_order_info = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "order";
    //下单
    public static final String pull_goods = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "purchasePay";
    //商品
    public static final String goods = HttpConstant.MPYS_API_BASE_URL + SPLICING_INDEX + "goods";

}
