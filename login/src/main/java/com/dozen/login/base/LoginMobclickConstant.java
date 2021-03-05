package com.dozen.login.base;

import android.os.Handler;

import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.VersionInfoUtils;
import com.dozen.login.LoginConstant;
import com.dozen.login.net.LoginUserHttpUtils;
import com.dozen.login.net.bean.GetOrderInfoResult;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Dozen
 * @description:
 * @time: 21/1/12
 */
public class LoginMobclickConstant {
    public static final String pay_ment_successful_wechat = "pay_ment_successful_wechat";  //微信支付成功  了解微信支付成功的次数

    public static final String click_activate_now = "click_activate_now";             //	点击立即开通	了解点击立即开通的次数
    public static final String click_customer_service = "click_customer_service";             //	点击客服	了解点击客服的次数
    public static final String get_into_vip_page = "get_into_vip_page";         //进入vip页面 了解进入vip页面的次数
    public static final String call_api_number = "call_api_number";         //请求支付接口  了解接口调取的次数
    public static final String call_api_pay_return_fail = "call_api_pay_return_fail";           //调取支付接口返回失败  了解接口调取失败的次数
    public static final String call_api_pay_return_success = "call_api_pay_return_success";         //调取支付接口返回成功  了解接口调取成功的次数
    public static final String call_api_pay_handle_success = "call_api_pay_handle_success";         //调取支付接口并处理成功 了解接口调取成功并处理成功的次数
    public static final String appear_successful_popup = "appear_successful_popup";         //弹出解锁成功的次数  了解有多少人成功解锁

    //支付完成埋点
    public static void upData() {

        String order_id = SPUtils.getString(APPBase.getApplication(), LoginConstant.PAY_ORDER_ID_NOW, "");
        if (order_id.equals("")) {
            return;
        }
        if (SPUtils.getBoolean(APPBase.getApplication(), LoginConstant.PAY_PUSH_UMENG, false)) {
            return;
        }
        LoginUserHttpUtils.getOrderInfoVerify(order_id, new CallBack() {
            @Override
            public void onRequested(final ResultInfo info, Object tag) {
                if (info.isSucceed() && tag.equals("order")) {
                    final GetOrderInfoResult result = (GetOrderInfoResult) info;
                    MyLog.d("0000userid" + LoginConstant.GET_UUID() + "_orderid=" + result.data.order_sn + "_amount" + result.data.order_price);
                    if (result.data.order_status == 1) {
                        //只有头条才发送
                        if (CommonConstant.umeng_click) {
                            MyLog.d("11111userid" + LoginConstant.GET_UUID() + "_orderid=" + result.data.order_sn + "_amount" + result.data.order_price);
                            Map<String, String> map = new HashMap<String, String>();//统计埋点
                            map.put("wxpay", "1");
                            map.put("userid", LoginConstant.GET_UUID());
                            map.put("orderid", "_version=" + VersionInfoUtils.getVersionName(APPBase.getApplication()) + "_order=" + result.data.order_sn + "_amount=" + result.data.order_price);//订单号+版本号+价格
                            map.put("amount", result.data.order_price);
                            MobclickAgent.onEvent(APPBase.getApplication(), LoginMobclickConstant.pay_ment_successful_wechat, map);
                        }
                        if (LoginConstant.channel.equals(LoginConstant.H5Pay_UMENG)) {
                            //延迟一秒发，以免丢失
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (CommonConstant.umeng_click) {
                                        MyLog.d("2222userid" + LoginConstant.GET_UUID() + "_orderid=" + result.data.order_sn + "_amount" + result.data.order_price);
                                        Map<String, String> successPayMap = new HashMap<String, String>();//统计埋点
                                        successPayMap.put("userid", LoginConstant.GET_UUID());
                                        successPayMap.put("orderid", "_version=" + VersionInfoUtils.getVersionName(APPBase.getApplication()) + "_order=" + result.data.order_sn + "_amount=" + result.data.order_price);//订单号+版本号+价格
                                        successPayMap.put("item", "开通vip");
                                        successPayMap.put("amount", result.data.order_price);
                                        MobclickAgent.onEvent(APPBase.getApplication(), "__finish_payment", successPayMap);

                                        //上报成功
                                        SPUtils.setString(APPBase.getApplication(), LoginConstant.PAY_ORDER_ID_NOW, "");
                                        SPUtils.setBoolean(APPBase.getApplication(), LoginConstant.PAY_PUSH_UMENG, true);

                                    }
                                }
                            }, 10);
                        }
                    } else {
                        //支付未完成
                        SPUtils.setString(APPBase.getApplication(), LoginConstant.PAY_ORDER_ID_NOW, "");
                    }
                } else {
                    //支付未完成
                    SPUtils.setString(APPBase.getApplication(), LoginConstant.PAY_ORDER_ID_NOW, "");
                }
            }
        }, "order");
    }
}
