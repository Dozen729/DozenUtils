package com.dozen.commonbase.http;

public class HttpConstant {

    //开发 调试环境
    public static final String API_BASE_URL_DV = "https://jdcx.2019bf.top";//https://habdev.fdwl.net.cn
    public static final String IMG_BASE_URL_DV = "http://www.zwjxt.com/test/upload.php";

    //测试
    public static final String API_BASE_URL_TEST = "http://kuaichacha-dev.xxzz123.top";//测试
    public static final String IMG_BASE_URL_TEST = "http://139.159.232.236:8086/receiver";//测试

    //生产 线上环境
    public static final String API_BASE_URL_PROD = "https://hab.fdwl.net.cn";
    public static final String IMG_BASE_URL_PROD = "http://receiver.cross188.com:20008";

    //切换环境
    public static String MPYS_API_BASE_URL;//数据
    public static String MPYS_IMG_BASE_URL;//图片

    public static final String api_version = "?api_version=1.0";//后缀

    //请求header,键
    public static final String HTTP_TYPE="XX-Device-Type";
    public static final String HTTP_Version="XX-Api-Version";
    public static final String HTTP_Channel="XX-Api-Channel";

    //请求header,值
    public static String HTTP_Channel_Value="toutiao";

    //是否需要代理 true为需要
    public static boolean NEED_PROXY = true;
}
