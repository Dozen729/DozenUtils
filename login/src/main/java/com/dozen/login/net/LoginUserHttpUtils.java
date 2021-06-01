package com.dozen.login.net;

import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.HttpResult;
import com.dozen.commonbase.http.HttpUtils;
import com.dozen.commonbase.utils.AppUtils;

import com.dozen.login.LoginConstant;
import com.dozen.login.net.bean.BannerPicResult;
import com.dozen.login.net.bean.ChannelResult;
import com.dozen.login.net.bean.GetOrderInfoResult;
import com.dozen.login.net.bean.GoodsListResult;
import com.dozen.login.net.bean.PullGoodsResult;
import com.dozen.login.net.bean.UserLoginResult;
import com.dozen.login.net.bean.VipInfoResult;

public class LoginUserHttpUtils {

    //获取渠道号
    public static void getChannel(CallBack callBack, Object tag) {
        HttpUtils httpUtils = new HttpUtils(LoginUserUrl.get_channel, callBack, ChannelResult.class, tag);
        httpUtils.get();
    }

    //轮播图
    public static void getBannerPic(CallBack callBack, Object tag) {
        String url = LoginUserUrl.get_banner_pic;
        HttpUtils httpUtils = new HttpUtils(url, callBack, BannerPicResult.class, tag);
        httpUtils.get();
    }

    //注册手机
    public static void register(String uuid, String mobile, String smsCode, String password, CallBack callBack, Object tag) {
        String url = LoginUserUrl.register + "?uuid=" + uuid + "&channel=" + LoginConstant.channel + "&mobile=" + mobile + "&sms_code=" + smsCode + "&password=" + password;
        HttpUtils httpUtils = new HttpUtils(url, callBack, HttpResult.class, tag);
        httpUtils.post();
    }

    //注册
    public static void register(String uuid, CallBack callBack, Object tag) {
        String url = LoginUserUrl.register + "?uuid=" + uuid;
        HttpUtils httpUtils = new HttpUtils(url, callBack, UserLoginResult.class, tag);
        httpUtils.post();
    }

    //获取个人信息
    public static void get_userInfo(String uuid, CallBack callBack, Object tag) {
        String url = LoginUserUrl.get_userinfo + "?uuid=" + uuid;
        HttpUtils httpUtils = new HttpUtils(url, callBack, VipInfoResult.class, tag);
        httpUtils.get();
    }

    //登录手机
    public static void loginMobile(String account, String password, CallBack callBack, Object tag) {
        String url = LoginUserUrl.mobile_login + "?mobile=" + account + "&password=" + password + "&uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, UserLoginResult.class, tag);
        httpUtils.post();
    }

    //重置密码
    public static void reSetPassword(String mobile, String password, String smsCode, CallBack callBack, Object tag) {
        String url = LoginUserUrl.re_password + "?mobile=" + mobile + "&password=" + password + "&sms_code=" + smsCode + "&uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, HttpResult.class, tag);
        httpUtils.put();
    }

    //退出登录
    public static void exitLogin(CallBack callBack, Object tag) {
        String url = LoginUserUrl.exit_login;
        HttpUtils httpUtils = new HttpUtils(url, callBack, HttpResult.class, tag);
        httpUtils.get();
    }

    //发送短信    //1登录 2注册 3身份验证 4修改密码
    public static void sendCode(String mobile, String type, CallBack callBack, Object tag) {
        String url = LoginUserUrl.send_sms + "?mobile=" + mobile + "&type=" + type + "&uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, HttpResult.class, tag);
        httpUtils.get();
    }

    //h5支付
    public static String h5Pay(String uuid, String version, String channel, String user_id) {
        return LoginUserUrl.html_pay + "?uuid=" + uuid + "&type=1" + "&version=" + version + "&channel=" + channel + "&device=" + AppUtils.getPhoneBrand() + "&user_id=" + user_id;
    }

    /**
     * 获取订单信息（验证支付成功）
     */
    public static void getOrderInfoVerify(String order_id, CallBack callBack, Object tag) {
        String url = LoginUserUrl.get_order_info + "?order_id=" + order_id + "&uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, GetOrderInfoResult.class, tag);
        httpUtils.get();
    }

    //h5下单
    public static void pullGoodsData(String goods_id, CallBack callBack, Object tag) {
        String url = LoginUserUrl.pull_goods + "?goods_id=" + goods_id + "&uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, PullGoodsResult.class, tag);
        httpUtils.post();
    }

    //商品列表
    public static void queryGoods(CallBack callBack, Object tag) {
        String url = LoginUserUrl.goods +"?uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, GoodsListResult.class, tag);
        httpUtils.get();
    }

    //微信登录
    public static void wxLogin(String code, CallBack callBack, Object tag) {
        String url = LoginUserUrl.wx_login + "?code=" + code + "&uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, UserLoginResult.class, tag);
        httpUtils.post();
    }

    //绑定手机
    public static void bindPhone(String sign, String mobile, String sms_code, CallBack callBack, Object tag) {
        String url = LoginUserUrl.bind_phone + "?sign=" + sign + "&mobile=" + mobile + "&sms_code=" + sms_code + "&uuid=" + LoginConstant.GET_UUID();
        HttpUtils httpUtils = new HttpUtils(url, callBack, UserLoginResult.class, tag);
        httpUtils.post();
    }

    //反馈
    public static void feedback(String mobile, String content,String type, CallBack callBack, Object tag) {
        String url = LoginUserUrl.feedback + "?uuid=" + LoginConstant.GET_UUID() + "&mobile=" + mobile + "&content=" + content+ "&type=" + type;
        HttpUtils httpUtils = new HttpUtils(url, callBack, HttpResult.class, tag);
        httpUtils.post();
    }
}
