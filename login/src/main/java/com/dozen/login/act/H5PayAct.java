package com.dozen.login.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dozen.commonbase.APPBase;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.dialog.DialogCommonListener;
import com.dozen.commonbase.dialog.LoadingDialog;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.HttpConstant;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.CommonUtils;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.utils.VersionInfoUtils;
import com.dozen.commonbase.view.NumberProgressBar;
import com.dozen.commonbase.view.TitleView;
import com.dozen.login.LoginConstant;
import com.dozen.login.R;
import com.dozen.login.base.LoginMobclickConstant;
import com.dozen.login.dialog.OrderConfirmDialog;
import com.dozen.login.dialog.PayTipDialog;
import com.dozen.login.dialog.TipSuccessfulDialog;
import com.dozen.login.dialog.VipBackDialog;
import com.dozen.login.net.DataSaveMode;
import com.dozen.login.net.LoginUserHttpUtils;
import com.dozen.login.net.LoginUserUrl;
import com.dozen.login.net.bean.GetOrderInfoResult;
import com.dozen.login.net.bean.GoodsListResult;
import com.dozen.login.net.bean.PullGoodsResult;
import com.dozen.login.net.bean.VipInfoResult;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Dozen  2020/9/9
 * @description: 描述
 */
public class H5PayAct extends CommonActivity {

    private TitleView titleView;
    private NumberProgressBar progressBar;
    private WebView webView;

    private boolean isShow = false;

    private LoadingDialog loadingDialog;

    @Override
    protected int setLayout() {
        return R.layout.act_h5_dozen;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView(Bundle savedInstanceState) {

        titleView = findViewById(R.id.titleView);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);

        SPUtils.setString(H5PayAct.this, LoginConstant.PAY_ORDER_ID, "");

        String user_id = SPUtils.getString(H5PayAct.this, LoginConstant.USER_ID, "");

        String web_url = LoginUserHttpUtils.h5Pay(LoginConstant.GET_UUID(), VersionInfoUtils.getVersionName(this), LoginConstant.channel, user_id);

        if (CommonConstant.umeng_click) {
            Map<String, String> map = new HashMap<String, String>();//统计埋点
            map.put("click", "1");
            MobclickAgent.onEvent(getBaseContext(), LoginMobclickConstant.get_into_vip_page, map);
        }

        MyLog.d("pay_url=" + web_url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        Map<String, String> webviewHead = new HashMap<>();
        webviewHead.put("Referer", HttpConstant.MPYS_API_BASE_URL);
        webView.loadUrl(web_url, webviewHead);


        webView.setWebChromeClient(webChromeClient);

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);

        //使用缓存，否则localstorage等无法使用
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = this.getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAppCacheEnabled(true);


        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);
        //adwebkit 指的是类（接口）的别名 ，在我们上面的代码中也就是JavascriptInterface类 所以android和js要约定好的就是 类的别名 以及 方法名 即可调用成功。
        webView.addJavascriptInterface(new H5PayAct.AndroidJs(), "adwebkit");//adwebkit

        titleView.setLeftBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isFastClick()){
                    backDialog();
                }
            }
        });

    }

    @Override
    protected void initData() {

    }

    private void backDialog() {
        if (DataSaveMode.isVip()) {
            finish();
            return;
        }
        VipBackDialog dialog = new VipBackDialog(this);
        dialog.setDialogCommonListener(new DialogCommonListener() {
            @Override
            public void isConfirm() {
                queryGoodsData();
            }

            @Override
            public void isCancel() {

            }

            @Override
            public void isClose() {
                finish();
            }
        });
        dialog.show();
    }

    //查询商品信息
    private void queryGoodsData() {
        LoginUserHttpUtils.queryGoods(new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed() && tag.equals("goods")) {
                    GoodsListResult goodsListResult = (GoodsListResult) info;
                    if (goodsListResult != null) {
                        checkUserVip(goodsListResult.data[0].id);
                    }
                }
            }
        }, "goods");
    }

    //检查vip并下单
    private void checkUserVip(String orderID) {
        LoginUserHttpUtils.get_userInfo(LoginConstant.GET_UUID(), new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed() && tag.equals("vip")) {
                    DataSaveMode.saveUserInfo((VipInfoResult) info);
                    pullOrder(orderID);
                }else {
                    StyleToastUtil.info(info.getMsg());
                }
            }
        }, "vip");
    }

    //下单并跳转支付
    private void pullOrder(String orderID) {
        if (CommonConstant.umeng_click) {
            Map<String, String> map = new HashMap<String, String>();//统计埋点
            map.put("click", "1");
            MobclickAgent.onEvent(getBaseContext(), LoginMobclickConstant.call_api_number, map);
        }
        if (DataSaveMode.isVip()) {
            checkOrderStatus();
            return;
        }
        if (orderID.equals("")) {
            return;
        }
        LoginUserHttpUtils.pullGoodsData(orderID, new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed() && tag.equals("pull")) {

                    if (CommonConstant.umeng_click) {
                        Map<String, String> map = new HashMap<String, String>();//统计埋点
                        map.put("click", "1");
                        MobclickAgent.onEvent(getBaseContext(), LoginMobclickConstant.call_api_pay_return_success, map);
                    }

                    PullGoodsResult pullGoodsResult = (PullGoodsResult) info;

                    //设置订单号
                    SPUtils.setString(H5PayAct.this, LoginConstant.PAY_ORDER_ID, pullGoodsResult.data.order.id + "");
                    SPUtils.setString(H5PayAct.this, LoginConstant.PAY_ORDER_ID_NOW, pullGoodsResult.data.order.id + "");
                    SPUtils.setBoolean(H5PayAct.this, LoginConstant.PAY_PUSH_UMENG, false);

                    String d = pullGoodsResult.data.url;
                    String[] ds = d.split("&redirect_url");
                    Intent intent = new Intent(H5PayAct.this, H5ToWxPayAct.class);
                    intent.putExtra("web_url", ds[0]);
                    startActivity(intent);
                }
            }
        }, "pull");
    }

    //检查订单是否支付完成
    private void checkOrderStatus() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        String order_id = SPUtils.getString(H5PayAct.this, LoginConstant.PAY_ORDER_ID, "");
        LoginUserHttpUtils.getOrderInfoVerify(order_id, new CallBack() {
            @Override
            public void onRequested(final ResultInfo info, Object tag) {
                loadingDialog.cancel();
                if (info.isSucceed() && tag.equals("order")) {
                    final GetOrderInfoResult result = (GetOrderInfoResult) info;
                    if (result.data.order_status == 1) {
                        //支付成功
                        StyleToastUtil.info("支付成功");
                        SPUtils.setBoolean(H5PayAct.this, LoginConstant.ISVIP, true);

                        setUserInfo();

                        showOrderSuccessDialog();
                    } else {
                        //支付失败
                        StyleToastUtil.error("支付失败");
                    }
                } else {
                    String id = SPUtils.getString(H5PayAct.this, LoginConstant.PAY_ORDER_ID, "");
                    if (id.equals("")) {
                        if (DataSaveMode.isVip()) {
                            showVIPTimeDialog();
                        }
                    }
                    StyleToastUtil.info(info.getMsg());
                }
                SPUtils.setString(H5PayAct.this, LoginConstant.PAY_ORDER_ID, "");
            }
        }, "order");
    }

    //设置用户信息
    private void setUserInfo() {
        LoginUserHttpUtils.get_userInfo(LoginConstant.GET_UUID(), new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed()&&tag.equals("user")) {
                    VipInfoResult vip = (VipInfoResult) info;
                    DataSaveMode.saveUserInfo(vip);
                }
            }
        }, "user");
    }

    //弹出用户VIP到期时间
    private void showVIPTimeDialog() {
        PayTipDialog dialog = new PayTipDialog(this);

        dialog.setOkListener(new PayTipDialog.onOkListener() {
            @Override
            public void OK() {

            }

            @Override
            public void cancel() {

            }
        });
        dialog.show();
        dialog.setContextDetail(SPUtils.getString(this, LoginConstant.VIP_EXPIRE_TIME, ""));
    }

    //弹出订单成功对话框
    private void showOrderSuccessDialog() {
        TipSuccessfulDialog dialog = new TipSuccessfulDialog(this);
        dialog.setOkLisenter(new TipSuccessfulDialog.onOkLisenter() {
            @Override
            public void OK() {
                if (CommonConstant.umeng_click) {
                    Map<String, String> map = new HashMap<String, String>();//统计埋点
                    map.put("click", "1");
                    MobclickAgent.onEvent(getBaseContext(), LoginMobclickConstant.appear_successful_popup, map);
                }
                finish();
            }
        });
        dialog.show();
    }

    //弹出订单确认对话框
    private void showOrderConfirmDialog() {
        if (isShow) {
            return;
        }
        upData();
        isShow = true;
        OrderConfirmDialog dialog = new OrderConfirmDialog(this);
        dialog.setOkLisenter(new OrderConfirmDialog.onOkLisenter() {
            @Override
            public void OK() {
                isShow = false;
                checkOrderStatus();
            }

            @Override
            public void Cancle() {
                isShow = false;
                checkOrderStatus();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = SPUtils.getString(H5PayAct.this, LoginConstant.PAY_ORDER_ID, "");
        if (!id.equals("")) {
            showOrderConfirmDialog();
        }
    }

    /**
     * 点击查看图片
     */
    public class AndroidJs {

        @JavascriptInterface
        public void pay(String orderID, String status) {

            if (status.equals("1")) {
                StyleToastUtil.info("请先同意相关协议！");
                return;
            }

            if (CommonConstant.umeng_click) {
                Map<String, String> map = new HashMap<String, String>();//统计埋点
                map.put("click", "1");
                MobclickAgent.onEvent(getBaseContext(), LoginMobclickConstant.click_activate_now, map);
            }
            if (!AppUtils.isWeixinAvilible(H5PayAct.this)) {
                StyleToastUtil.warning("请先安装微信");
                return;
            }
            MyLog.d("__order_id:" + orderID);
            if (CommonUtils.isFastClick()) {
                checkUserVip(orderID);
            }
        }

        @JavascriptInterface
        public void member() {
            userPay();
        }

        @JavascriptInterface
        public void proivacy() {
            privatePolicy();
        }

        @JavascriptInterface
        public void service() {
            vipService();
        }

        @JavascriptInterface
        public void xieyi() {
            userPayAgreement();
        }
    }

    private void vipService() {
        if (CommonConstant.umeng_click) {
            Map<String, String> map = new HashMap<String, String>();//统计埋点
            map.put("click", "1");
            MobclickAgent.onEvent(getBaseContext(), LoginMobclickConstant.click_customer_service, map);
        }
        MyLog.d("click_customer_service");
    }

    private void userPay() {
        LoginConstant.h5UrlShow(this, LoginUserUrl.user_pay_agreement);
    }

    private void privatePolicy() {
        LoginConstant.h5UrlShow(this, LoginUserUrl.user_agreement);
    }

    private void userPayAgreement() {
        LoginConstant.h5UrlShow(this, LoginUserUrl.user_pay_agreement);
    }

    WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            MyLog.d(title);
            titleView.setTitle(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        //=========HTML5定位==========================================================

        //=========多窗口的问题==========================================================
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }
    };


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
                        if (LoginConstant.H5Pay_UMENG.contains(LoginConstant.channel)) {
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
