package com.dozen.commonbase.http;

/**
 * Created by sinter on 2018/8/7.
 */

public class HttpCode {

    /**网络错误*/
    public final static int NET_ERROR = -1;
    /**请求超时*/
    public final static int REQUEST_TIMEOUT = -2;
    /**数据解析错误*/
    public final static int DATA_ERROR = -3;

    public static String getCauseByHttpCode(int causeCode) {
        switch (causeCode) {
            case 404:
                return "请求不存在";
            case 400:
                return "请求错误";
            case 401:
                return "请求未授权";
            case 403:
                return "服务器拒绝请求";
            case 405:
                return "请求已禁用";
            case 408:
                return "请求超时";
            case 502:
                return "错误网关";
            case 503:
                return "服务不可用";
            case 504:
                return "网关超时";
            case 500:
                return "服务器开小差了";
            case 505:
                return "HTTP版本不受支持";
        }
        return "请求失败";
    }

    /**
     * 获取网络请求失败的原因
     * @param errorCode
     * @return
     */
    public static String getRequestFailMess(int errorCode){
        switch (errorCode) {
            case NET_ERROR:
                return "网络异常,请检查网络后重试.";
            case REQUEST_TIMEOUT:
                return "请求超时,请重试.";
            case DATA_ERROR:
                return "请求数据错误,请重试.";
            case 404:
                return "请求不存在...";
            case 400:
                return "请求错误...";
            case 401:
                return "请求未授权...";
            case 403:
                return "服务器拒绝请求";
            case 405:
                return "请求已被禁用...";
            case 408:
                return "请求超时,请重试.";
            case 502:
                return "请求网关错误,请重试.";
            case 503:
                return "请求服务不可用...";
            case 504:
                return "请求网关超时,请重试.";
            case 500:
                return "服务器开小差了";
            case 505:
                return "HTTP版本不受支持";
        }
        return "请求失败,请重试.";
    }
}
