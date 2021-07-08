package com.dozen.commonbase.http.rawhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/7/7
 */
public class HttpClientUtils {


    /**
     * 使用示例
     * HttpClientUtils().post(Api.API_BASE_URL_2 + "sdk_face_alive")
     * .setHead("token",XueGuMax.getToken())
     * .addParameter("serviceName", "real_auth_sdk")
     * .addParameter("platformNo", XueGuMax.getPlatformNo())
     * .addParameter("reqData", reqData)
     * .setCallBack(object : HttpClientUtils.OnRequestCallBack {
     * override fun onSuccess(json: String?) {
     * val data = Gson().fromJson(json, AuthResult::class.java)
     * <p>
     * }
     * override fun onError(errorMsg: String?) {
     * Log.e("数据获取失败", errorMsg)
     * }}).build
     */

    /**
     * 使用示例2
     * String strE = "{current:1,size:600,filter:{type:1,latitude:0,longitude:0,regionId:1,c:76863443526}}";
     * HttpClientUtils().post(Api.API_BASE_URL_2 + "sdk_face_alive")
     * .setHead("Content-Type", "application/json")
     * .addRawData(strE)
     * .setCallBack(object : HttpClientUtils.OnRequestCallBack {
     * override fun onSuccess(json: String?) {
     * val data = Gson().fromJson(json, AuthResult::class.java)
     * <p>
     * }
     * override fun onError(errorMsg: String?) {
     * Log.e("数据获取失败", errorMsg)
     * }}).build
     */


    private static final String TAG = "HttpClientUtils";

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 我自己封住的
     * 此方法为post请求,异步的网络请求,回调在主线程
     **/
    public HttpURLBuild post(final String requestUrl) {
        HttpURLBuild httpURLBuild = new HttpURLBuild(requestUrl, "post");
        return httpURLBuild;
    }

    public HttpURLBuild get(final String requestUrl) {
        HttpURLBuild httpURLBuild = new HttpURLBuild(requestUrl, "get");
        return httpURLBuild;
    }

    public interface OnRequestCallBack {
        void onSuccess(String json);

        void onError(String errorMsg);
    }

    public static class OnRequestCallBackBase {
        public OnRequestCallBack OnRequestCallBack;

        public OnRequestCallBackBase(OnRequestCallBack onRequestCallBack) {
            this.OnRequestCallBack = onRequestCallBack;
        }

        public void onSuccess(final String json) {

            if (TextUtils.equals(json, "null") || TextUtils.isEmpty(json)) {
                OnRequestCallBack.onError("数据为空");
                return;
            }
            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
//                    Type type = getClass().getGenericSuperclass();

//                    Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
                    OnRequestCallBack.onSuccess(json);
                }
            });
        }


        public void onError(final String errorMsg) {
            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    OnRequestCallBack.onError(errorMsg);
                }
            });

        }
    }
}
