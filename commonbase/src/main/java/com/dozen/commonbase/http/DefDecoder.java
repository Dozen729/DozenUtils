package com.dozen.commonbase.http;

import com.dozen.commonbase.utils.MyLog;
import com.google.gson.Gson;

/**
 * 默认Http解析
 */

public class DefDecoder implements Decoder {

    protected Class<? extends HttpResult> cls;

    public DefDecoder(Class<? extends HttpResult> cls) {
        this.cls = cls;
    }

    /**
     * 请求失败处理
     * @param httpCode
     * @param msg
     * @return
     */
    @Override
    public ResultInfo onRequestFail(int httpCode, String msg) {
        try {
            ResultInfo info = cls.newInstance();
            info.setFailInfo(httpCode, msg);
            return info;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求成功处理
     * @param resultStr
     * @return
     */
    @Override
    public ResultInfo onRequestSuccess(String resultStr){
        try {
            int start = resultStr.indexOf("{");
            int end = resultStr.indexOf("}");
            if (start < 0 || end < 0 || end < start) {
                return onRequestFail(HttpCode.DATA_ERROR,"网络数据错误");
            }
            HttpResult httpResult = fromJson(200, resultStr, cls);
            MyLog.d("HttpResult httpcode:" + httpResult.httpCode);
            return httpResult;
        } catch (Exception e) {
            MyLog.e("网络返回时错误-onSuccess\n"+e.toString());
            try {
                HttpResult info = cls.newInstance();
                HttpResult result = fromJson(HttpCode.DATA_ERROR,resultStr, HttpResult.class);
                info.code = result.code;
                info.message = result.message;
                info.httpCode = result.httpCode;
                return info;
            } catch (Exception e1) {
            }
            return null;
        }
    }

    /**
     * 格式化网络请求数据
     * @param httpCode
     * @param resultStr
     * @param cls
     * @return
     */
    private HttpResult fromJson(int httpCode, String resultStr, Class<? extends HttpResult> cls){
        Gson gson = new Gson();
        HttpResult result = gson.fromJson(resultStr.trim(), cls);
        result.httpCode = httpCode;
        return result;
    }
}
