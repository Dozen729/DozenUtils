package com.dozen.thirdparty.ad.tt.config;

import com.bytedance.sdk.openadsdk.TTCustomController;
import com.bytedance.sdk.openadsdk.TTLocation;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/5/6
 */
public class TTMineCustomController extends TTCustomController {
    /**
     * 是否允许SDK主动使用地理位置信息
     *
     * @return true可以获取，false禁止获取。默认为true
     */
    public boolean isCanUseLocation() {
        return false;
    }

    /**
     * 当isCanUseLocation=false时，可传入地理位置信息，穿山甲sdk使用您传入的地理位置信息
     *
     * @return 地理位置参数
     */
    public TTLocation getTTLocation() {
        return null;
    }

    /**
     * 是否允许SDK主动使用手机硬件参数，如：imei
     *
     * @return true可以使用，false禁止使用。默认为true
     */
    public boolean isCanUsePhoneState() {
        return false;
    }

    /**
     * 当isCanUsePhoneState=false时，可传入imei信息，穿山甲sdk使用您传入的imei信息
     *
     * @return imei信息
     */
    public String getDevImei() {
        return null;
    }

    /**
     * 是否允许SDK主动使用ACCESS_WIFI_STATE权限
     *
     * @return true可以使用，false禁止使用。默认为true
     */
    public boolean isCanUseWifiState() {
        return true;
    }

    /**
     * 是否允许SDK主动使用WRITE_EXTERNAL_STORAGE权限
     *
     * @return true可以使用，false禁止使用。默认为true
     */
    public boolean isCanUseWriteExternal() {
        return true;
    }

    /**
     * 开发者可以传入oaid
     *
     * @return oaid
     */
    public String getDevOaid() {
        return null;
    }
}
