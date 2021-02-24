package com.dozen.commonbase.http;

import android.os.Handler;
import android.os.Looper;

import com.dozen.commonbase.utils.MyLog;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Http 请求
 */

public class BaseHttpUtils implements HttpClient.HttpCallback{

    private Handler handler = new Handler(Looper.getMainLooper());
    private HttpClient httpClient;
    private Decoder mDecoder;
    private CallBack mDallBack;

    public BaseHttpUtils(String url, CallBack callBack, Class cls, Object tag) {
        this(url, callBack, new DefDecoder(cls), tag);
    }

    public BaseHttpUtils(String url, CallBack callBack, Decoder decoder, Object tag) {
        httpClient = new HttpClient(url, tag);
        mDecoder = decoder;
        mDallBack = callBack;
        httpClient.setHttpCallback(this);
    }

    @Override
    public void onSuccess(final String value, final Object tag) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ResultInfo info = mDecoder.onRequestSuccess(value);
                    onRequestedBack(info, tag);
                } catch (Exception e) {
                    onFailed(HttpCode.DATA_ERROR, "数据解析错误", tag);
                    e.printStackTrace();
                    MyLog.e("数据解析错误，或跟新界面发生错误"+ e.getMessage());
            }
            }
        });
    }

    @Override
    public void onFailed(final int code, final String msg, final Object tag) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onRequestedBack(mDecoder.onRequestFail(code, msg),tag);
            }
        });
    }

    public void onRequestedBack(ResultInfo info, Object tag) {
        if(mDallBack != null) {
            mDallBack.onRequested(info, tag);
        }
    }

    /**
     * 传值
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        httpClient.addParam(key, value);
    }

    public HashMap<String, String> getParam() {
        return httpClient.getParam();
    }

    /**
     * 传文件 - file
     * @param key
     * @param file
     */
    public void addParam(String key, File file) {
        httpClient.addParam(key, file);
    }

    /**
     * 传文件 - new File(path)
     * @param key
     * @param path
     */
    public void addFileParam(String key, String path) {
        httpClient.addFileParam(key, path);
    }

    /**
     * 传文件 - new File(path)
     * @param key
     * @param list
     */
    public void addFileParam(String key, List<String> list) {
        httpClient.addFileParam(key, list);
    }

    public void post() {
        httpClient.post();
    }

    public void get() {
        httpClient.get();
    }


    public void put() {
        httpClient.put();
    }

    public void cancel() {
        mDallBack = null;
        httpClient.cancel();
    }

    //是否需要压缩
    public void needCompress(boolean need){
        httpClient.needCompress = need;
    }
}
