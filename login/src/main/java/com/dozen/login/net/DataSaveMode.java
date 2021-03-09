package com.dozen.login.net;

import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.login.LoginConstant;
import com.dozen.login.net.bean.UserLoginResult;
import com.dozen.login.net.bean.VipInfoResult;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/12/9
 */
public class DataSaveMode {

    //保存用户信息
    public static void saveUserData(UserLoginResult login) {
        UserLoginResult.Data.UserToken ut = login.data.tokeninfo;
        SPUtils.setString(APPBase.getApplication(), CommonConstant.USER_TOKEN, ut.token);
        SPUtils.setLong(APPBase.getApplication(), LoginConstant.USER_EXPIR_TIME, ut.expir_time);

        if (!ut.token.isEmpty()) {
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.USER_TOKEN_STATE, true);
        }


        VipInfoResult vipInfoResult = new VipInfoResult();
        vipInfoResult.data = login.data.userinfo;
        saveUserInfo(vipInfoResult);
    }

    public static void saveUserInfo(VipInfoResult info) {

        if (EmptyCheckUtil.isEmpty(info.data.vip_expire_time)){
            SPUtils.setString(APPBase.getApplication(), LoginConstant.VIP_EXPIRE_TIME, "");
        }else {
            SPUtils.setString(APPBase.getApplication(), LoginConstant.VIP_EXPIRE_TIME, info.data.vip_expire_time);
        }

        if (EmptyCheckUtil.isEmpty(info.data.mobile)) {
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_Phone_LOGIN, false);
        } else {
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_Phone_LOGIN, true);
        }
        SPUtils.setString(APPBase.getApplication(), LoginConstant.USER_NAME, info.data.mobile);
        if (EmptyCheckUtil.isEmpty(info.data.openid)) {
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_WX_LOGIN, false);
        } else {
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_WX_LOGIN, true);
        }
        int vip = info.data.is_vip;
        if (vip == 0) {
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.ISVIP, false);
        } else if (vip == 1) {
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.ISVIP, true);
        }

        String is_login = info.data.is_login;
        if (is_login.equals("1")){
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_LOGIN, true);
        }else if (is_login.equals("0")){
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_LOGIN, false);
        }

        SPUtils.setString(APPBase.getApplication(), LoginConstant.USER_ID, info.data.id);

        LoginConstant.SET_UUID(info.data.uuid);

        SPUtils.setString(APPBase.getApplication(), LoginConstant.HEAD_IMG_URL, info.data.headimgurl);
        SPUtils.setString(APPBase.getApplication(), LoginConstant.INVITE_CODE, info.data.code);
        SPUtils.setString(APPBase.getApplication(), LoginConstant.NICK_NAME, info.data.user_nickname);
    }

    public static void clearUserInfo() {
        SPUtils.setString(APPBase.getApplication(), CommonConstant.USER_TOKEN, "");
        SPUtils.setLong(APPBase.getApplication(), LoginConstant.USER_EXPIR_TIME, 0);
        SPUtils.setString(APPBase.getApplication(), LoginConstant.VIP_EXPIRE_TIME, "");
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_Phone_LOGIN, false);
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.ISVIP, false);

        SPUtils.setString(APPBase.getApplication(), LoginConstant.USER_NAME, "");
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_WX_LOGIN, false);

        SPUtils.setString(APPBase.getApplication(), LoginConstant.USER_ID, "");

        SPUtils.setString(APPBase.getApplication(), LoginConstant.HEAD_IMG_URL, "");
        SPUtils.setString(APPBase.getApplication(), LoginConstant.INVITE_CODE, "");
        SPUtils.setString(APPBase.getApplication(), LoginConstant.NICK_NAME, "");
        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.IS_LOGIN, false);


        if (SPUtils.getBoolean(APPBase.getApplication(), LoginConstant.USER_TOKEN_STATE, false)) {
            registerUUID();
            SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.USER_TOKEN_STATE, false);
        }
    }

    public static void clearToken() {
        clearUserInfo();
    }

    public static void registerUUID() {
        String pay_uuid = SPUtils.getString(APPBase.getApplication(), LoginConstant.PAY_UUID, "");
        if (LoginConstant.GET_UUID().equals(pay_uuid)) {
            LoginConstant.SET_UUID("");
            LoginConstant.GET_UUID();
        } else {
            String token = SPUtils.getString(APPBase.getApplication(),LoginConstant.PAY_UUID_TOKEN,"");
            if (token.equals("")){
                LoginConstant.SET_UUID("");
                LoginConstant.GET_UUID();
            }else {
                SPUtils.setString(APPBase.getApplication(), CommonConstant.USER_TOKEN,token);
                LoginConstant.SET_UUID(pay_uuid);
            }
        }
    }

    public static boolean isLogin() {
        return SPUtils.getBoolean(APPBase.getApplication(), LoginConstant.IS_LOGIN, false);
    }

    public static boolean isVip() {
        return SPUtils.getBoolean(APPBase.getApplication(), LoginConstant.ISVIP, false);
    }

}
