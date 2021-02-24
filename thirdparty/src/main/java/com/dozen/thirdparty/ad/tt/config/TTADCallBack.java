package com.dozen.thirdparty.ad.tt.config;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/19
 */
public interface TTADCallBack {
    void isFail(String error);
    void downloadFinish(String fileName,String appName);
    void loadSuccess();
    void playComplete();
}
