package com.dozen.commonbase;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/2
 */
public class CommonConstant {

    public static String isFirstInstall = "isFirstInstall";//第一次启动
    public static String isFirstAgree = "isFirstAgree";//第一次同意
    public static String closeGuide = "closeGuide";//展示引导页
    public static String guideData = "guideData";//引导页数据
    public static String isAnswer = "isAnswer";//回答问题

    public static boolean umeng_click = false;//有盟点击事件传递开关
    public static boolean umeng_pay = false;//有盟付费
    public static boolean reyun_click_switch=false;//热云埋点开关
    public static boolean mine_click_switch=false;//自己的服务器上传开关
    public static final String skip_guide="skip_guide";//true为跳过引导，false为进入引导

    public static final int REQUEST_PERMISSIONS=10001;
    public static final String USER_TOKEN="user_token";

    public static final String permissions_state = "permissions_state";//权限判断
    public static final String app_no_net = "app_no_net";//网络实时判断

    //token失效错误码
    public static final String TOKEN_INVALID_CODE = "10001";
    //token失效广播
    public static final String TOKEN_INVALID = "com.example.detectiondog.client.TOKEN_INVALID";
    //是否需要加密
    public static final String ISENCRYPT = "isEncrypt";

    //oaid
    public static String OAID="oaid";
}
