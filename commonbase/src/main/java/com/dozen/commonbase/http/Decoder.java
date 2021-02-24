package com.dozen.commonbase.http;

/**
 * http 数据解析
 */

public interface Decoder {
    ResultInfo onRequestFail(int httpCode, String msg);
    ResultInfo onRequestSuccess(String resultStr);
}
