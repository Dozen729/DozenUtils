package com.dozen.thirdparty.ad;

/**
 * @author: Dozen
 * @description:第三方常量
 * @time: 2020/12/1
 */
public class TPConstant {
    //穿山甲广告
    public static final String TT_APP_ID="5177509";
    public static final String TT_AD_SPLASH="887486181";//开屏
    public static final String TT_AD_FULL_VIDEO_VERTICAL="945612710";//竖版全屏视频
    public static final String TT_AD_REWARD_VIDEO_VERTICAL="946188245";//竖版激励视频
    public static final String TT_AD_REWARD_VIDEO_VERTICAL_TWO="946188250";//竖版激励视频
    public static final String TT_AD_BANNER="946188151";//Banner广告
    public static final String TT_AD_EXPRESS_LIST="946188134";//信息流
    public static final String TT_AD_INSERT_VIDEO="946188280";//插屏广告
    public static final String TT_AD_INSERT_VIDEO_TWO="946188202";//插屏广告

    public static final boolean TT_AD_IS_EXPRESS=true;//是否请求模板广告

    public static boolean tt_log_switch = false; //穿山甲调试Log日志输出开关

    public static boolean ttAdSplashShow = true; //是否展示开屏广告
    public static boolean ttAdVideoShow = true; //是否展示激励视频广告
    public static boolean ttAdBannerShow = true; //是否展示Banner广告
    public static boolean ttAdListShow = true; //是否展示信息流广告
    public static boolean ttAdInsertShow = true; //是否展示插屏广告

    public static void allADState(boolean state){
        ttAdSplashShow = state;
        ttAdVideoShow = state;
        ttAdBannerShow = state;
        ttAdListShow = state;
        ttAdInsertShow = state;
    }
}
