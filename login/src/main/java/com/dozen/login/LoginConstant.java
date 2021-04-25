package com.dozen.login;

import android.content.Context;
import android.content.Intent;

import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.id.OnlyAndroidID;
import com.dozen.login.act.H5Act;
import com.dozen.login.net.DataSaveMode;
import com.dozen.login.net.LoginUserHttpUtils;
import com.dozen.login.net.LoginUserUrl;
import com.dozen.login.net.bean.ChannelResult;
import com.dozen.login.net.bean.UserLoginResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/1
 */
public class LoginConstant {

    /*
     * 必须重新赋值
     * vvv
     * vvv
     * */
    public static String channel = LoginConstant.VIVO;//渠道 toutiao vivo yingyongbao xiaomi oppo huawei 通用common

    //toutiao支付完成需要埋点
    public static String H5Pay_UMENG = LoginConstant.TOUTIAO;

    /*
     * ^^^
     * ^^^
     *  必须重新赋值
     * */


    //uuid
    public static String UUID = "uuid";

    public static String GET_UUID() {
        String uuid = SPUtils.getString(APPBase.getApplication(), UUID, "");
        if (uuid == null || uuid.equals("") || uuid.equals("null")) {
            uuid = OnlyAndroidID.getUniquePsuedoIDtoMD5() + "_" + System.currentTimeMillis();
            String finalUuid = uuid;
            LoginUserHttpUtils.register(uuid, new CallBack() {
                @Override
                public void onRequested(ResultInfo info, Object tag) {
                    if (info.isSucceed()&&tag.equals("register")){
                        SET_UUID(finalUuid);
                        SPUtils.setString(APPBase.getApplication(), LoginConstant.PAY_UUID, finalUuid);
                        DataSaveMode.saveUserData((UserLoginResult) info);
                        UserLoginResult userLoginResult = (UserLoginResult) info;
                        SPUtils.setString(APPBase.getApplication(), LoginConstant.PAY_UUID_TOKEN, userLoginResult.data.tokeninfo.token);
                        MyLog.d("register_uuid");
                    }else {
                        if (info.getCode().equals(CommonConstant.TOKEN_INVALID_CODE)){
                            DataSaveMode.clearUserInfo();
                            SET_UUID("");
                        }
                    }
                }
            }, "register");
        }
        return uuid;
    }

    public static void SET_UUID(String uuid) {
        SPUtils.setString(APPBase.getApplication(), UUID, uuid);
    }

    //渠道 toutiao vivo yingyongbao xiaomi oppo huawei common
    public static final String COMMON = "common";
    public static final String TOUTIAO = "toutiao";
    public static final String VIVO = "vivo";
    public static final String YINGYONGBAO = "yingyongbao";
    public static final String XIAOMI = "xiaomi";
    public static final String OPPO = "oppo";
    public static final String HUAWEI = "huawei";
    public static final String FUSION = "fusion";
    public static final String KUAISHOU = "kuaishou";
    public static final String WEIBO = "weibo";
    public static final String DONGFANG = "dongfang";

    //渠道支付开关
    public static final String CHANNELZHIFU="channel_zhifu";
    //广告播放开关
    public static final String CHANNELAD="channel_ad";
    //tag切换
    public static final String CHANNEL_TAG="channel_tag";
    //开屏广告
    public static final String CHANNEL_AD_SPLASH="channel_ad_splash";
    //信息流广告
    public static final String CHANNEL_AD_INFO="channel_ad_info";
    //查找界面
    public static final String CHANNEL_SEARCH="channel_search";
    //VIP入口
    public static final String CHANNEL_VIP_IN="channel_vip_in";

    //登录成功数据
    public static final String USER_TOKEN = "user_token";
    public static final String USER_TOKEN_STATE = "user_token_state";
    public static final String USER_EXPIR_TIME = "expir_time";
    public static final String VIP_EXPIRE_TIME = "vip_expire_time";
    public static final String USER_NAME = "user_name";
    public static final String IS_Phone_LOGIN = "is_phone_login";
    public static final String IS_WX_LOGIN = "is_wx_login";
    public static final String HEAD_IMG_URL = "head_img_url";
    public static final String INVITE_CODE = "invite_code";
    public static final String NICK_NAME = "nick_name";
    public static final String WX_SIGN = "wx_sign";
    public static final String USER_ID = "user_id";
    public static final String PAY_UUID = "pay_uuid";
    public static final String PAY_UUID_TOKEN = "pay_uuid_token";
    public static final String IS_LOGIN = "is_login";
    //vip开关
    public static final String ISVIP = "is_vip";

    //用户登录和注册
    public static final String USER_ACT = "user_act";
    public static final int USER_LOGIN = 301;
    public static final int USER_REGISTER = 302;
    public static final int USER_POSS_WORD = 303;

    //获取验证码类型 //1登录 2注册 3身份验证 4修改密码
    public static final String REGISTER_CODE = "1";//登录
    public static final String LOGIN_CODE = "2";//注册
    public static final String USER_CODE = "3";//身份验证
    public static final String PASSWORD_CODE = "4";//修改密码

    //支付订单号
    public static final String PAY_ORDER_ID = "pay_order_id";
    public static final String PAY_ORDER_ID_NOW = "pay_order_id_now";//最新订单号
    public static final String PAY_PUSH_UMENG = "pay_push_umeng"; //更新到umeng

    public static void h5UrlShow(Context context,String url){
        Intent intent = new Intent(context, H5Act.class);
        intent.putExtra("web_url", url);
        context.startActivity(intent);
    }

    //初始化支付开关和广告开关
    public static void initHomeSwitch() {
        LoginUserHttpUtils.getChannel(new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed() && tag.equals("channel")) {
                    ADResult((ChannelResult) info);
                } else if (info.getCode().equals(CommonConstant.TOKEN_INVALID_CODE)) {
                    DataSaveMode.clearToken();
                    DataSaveMode.clearUserInfo();
                }
            }
        }, "channel");
    }

    private static void ADResult(ChannelResult result) {
        boolean show = false;
        boolean pay = false;
        boolean tag = false;
        boolean kp = false;
        boolean xxl = false;
        boolean search = false;
        boolean zf = false;

        if (LoginConstant.channel.equals(LoginConstant.TOUTIAO)) {//toutiao
            if ("on".equals(result.data.toutiao.ad)) {
                show = true;
            }
            if ("on".equals(result.data.toutiao.zf)) {
                pay = true;
            }

        }
        if (LoginConstant.channel.equals(LoginConstant.YINGYONGBAO)) {//yingyongbao
            if ("on".equals(result.data.yingyongbao.ad)) {
                show = true;
            }
            if ("on".equals(result.data.yingyongbao.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.VIVO)) {//vivo
            if ("on".equals(result.data.vivo.ad)) {
                show = true;
            }
            if ("on".equals(result.data.vivo.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.XIAOMI)) {//xiaomi
            if ("on".equals(result.data.xiaomi.ad)) {
                show = true;
            }
            if ("on".equals(result.data.xiaomi.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.OPPO)) {//oppo
            if ("on".equals(result.data.oppo.ad)) {
                show = true;
            }
            if ("on".equals(result.data.oppo.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.HUAWEI)) {//huawei
            if ("on".equals(result.data.huawei.ad)) {
                show = true;
            }
            if ("on".equals(result.data.huawei.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.COMMON)) {//common
            if ("on".equals(result.data.common.ad)) {
                show = true;
            }
            if ("on".equals(result.data.common.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.KUAISHOU)) {//kuaishou
            if ("on".equals(result.data.kuaishou.ad)) {
                show = true;
            }
            if ("on".equals(result.data.kuaishou.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.WEIBO)) {//weibo
            if ("on".equals(result.data.weibo.ad)) {
                show = true;
            }
            if ("on".equals(result.data.weibo.zf)) {
                zf = true;
            }
        }
        if (LoginConstant.channel.equals(LoginConstant.DONGFANG)) {//dongfang
            if ("on".equals(result.data.dongfang.ad)) {
                show = true;
            }
            if ("on".equals(result.data.dongfang.zf)) {
                zf = true;
            }
        }

        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.CHANNELAD, show);
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.CHANNELZHIFU, pay);
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.CHANNEL_TAG, tag);
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.CHANNEL_AD_SPLASH, kp);
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.CHANNEL_AD_INFO, xxl);
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.CHANNEL_SEARCH, search);
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.CHANNEL_VIP_IN, zf);
    }
}
