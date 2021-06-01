package com.dozen.login.act;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.h5.WebLayout;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.utils.VersionInfoUtils;
import com.dozen.commonbase.view.NumberProgressBar;
import com.dozen.commonbase.view.TitleView;
import com.dozen.login.LoginConstant;
import com.dozen.login.R;
import com.dozen.login.base.LoginMobclickConstant;
import com.dozen.login.dialog.VipTipDialog;
import com.dozen.login.net.DataSaveMode;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

@Route(path = ARouterLocation.app_url_show_android_js)
public class UrlShowAndroidJsAct extends CommonActivity {

    @Autowired
    String url_show_ajs = "";

    private TitleView titleView;
    private NumberProgressBar progressBar;
    protected AgentWeb mAgentWeb;
    private LinearLayout mLinearLayout;

    @Override
    protected int setLayout() {
        return R.layout.activity_online_service;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        if (EmptyCheckUtil.isEmpty(url_show_ajs)) {
            return;
        }

        if (url_show_ajs.contains("?")) {
            url_show_ajs += "&vip=" + DataSaveMode.isVip();
        } else {
            url_show_ajs += "?vip=" + DataSaveMode.isVip();
        }

        url_show_ajs += "&uuid=" + LoginConstant.GET_UUID() + "&version=" + VersionInfoUtils.getVersionName(this) + "&channel=" + LoginConstant.channel + "&device=" + AppUtils.getPhoneBrand();


        titleView = findViewById(R.id.titleView);
        progressBar = findViewById(R.id.progressBar);
        mLinearLayout = findViewById(R.id.online_service);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .addJavascriptInterface("adwebkit", new AndroidJs())//adwebkit
                .setWebChromeClient(webChromeClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setWebLayout(new WebLayout(this))
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(url_show_ajs);


        WebSettings mWebSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);

        //使用缓存，否则localstorage等无法使用
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = this.getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setBuiltInZoomControls(true);//support zoom
        mWebSettings.setUseWideViewPort(true);//關鍵點
        mWebSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;

        if (mDensity == 240) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            mWebSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }

    }

    @Override
    protected void initData() {

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

    /**
     * 点击查看图片
     */
    public class AndroidJs {

        @JavascriptInterface
        public void onlineService(String url) {
            if (CommonConstant.umeng_click) {
                Map<String, String> map = new HashMap<String, String>();//统计埋点
                map.put("click", "1");
                MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_to_enter_customer, map);
            }
            if (DataSaveMode.isVip()) {
                if (!EmptyCheckUtil.isEmpty(url)) {
                    ARouter.getInstance().build(ARouterLocation.app_url_show).withString("url_show", url).navigation();
                }
            } else {
                if (CommonConstant.umeng_click) {
                    Map<String, String> map = new HashMap<String, String>();//统计埋点
                    map.put("click", "1");
                    MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.eject_pop_up_window, map);
                }
                VipTipDialog dialog = new VipTipDialog(UrlShowAndroidJsAct.this);
                dialog.setOkListener(new VipTipDialog.onOkListener() {
                    @Override
                    public void OK() {
                        if (CommonConstant.umeng_click) {
                            Map<String, String> map = new HashMap<String, String>();//统计埋点
                            map.put("click", "1");
                            MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_open_membership, map);
                        }
                    }

                    @Override
                    public void cancel() {
                        if (CommonConstant.umeng_click) {
                            Map<String, String> map = new HashMap<String, String>();//统计埋点
                            map.put("click", "1");
                            MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_fine, map);
                        }
                    }
                });
                dialog.show();

            }
        }

        @JavascriptInterface
        public void showToast(String msg) {
            if (CommonConstant.umeng_click) {
                Map<String, String> map = new HashMap<String, String>();//统计埋点
                map.put("click", "1");
                MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_copy, map);
            }
            StyleToastUtil.success(msg);
        }

        @JavascriptInterface
        public void showToas(String msg) {
            if (CommonConstant.umeng_click) {
                Map<String, String> map = new HashMap<String, String>();//统计埋点
                map.put("click", "1");
                MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_copy, map);
            }
            StyleToastUtil.success(msg);
        }

        @JavascriptInterface
        public void click_faq(String url) {
            if (CommonConstant.umeng_click) {
                Map<String, String> map = new HashMap<String, String>();//统计埋点
                map.put("click", "1");
                MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_faq, map);
            }
            if (!EmptyCheckUtil.isEmpty(url)) {
                ARouter.getInstance().build(ARouterLocation.app_url_show).withString("url_show", url).navigation();
            }
            MyLog.d("click_faq");
        }

        @JavascriptInterface
        public void click_to_enter_cost(String url) {
            if (CommonConstant.umeng_click) {
                Map<String, String> map = new HashMap<String, String>();//统计埋点
                map.put("click", "1");
                MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_to_enter_cost, map);
            }
            if (!EmptyCheckUtil.isEmpty(url)) {
                ARouter.getInstance().build(ARouterLocation.app_url_show_android_js).withString("url_show_ajs", url).navigation();
            }
            MyLog.d("click_to_enter_cost" + url);
        }

        @JavascriptInterface
        public void click_to_see_details(String msg, String url) {
            if (CommonConstant.umeng_click) {
                Map<String, String> map = new HashMap<String, String>();//统计埋点
                map.put("click", msg);
                MobclickAgent.onEvent(UrlShowAndroidJsAct.this, LoginMobclickConstant.click_to_see_details, map);
            }
            if (!EmptyCheckUtil.isEmpty(url)) {
                ARouter.getInstance().build(ARouterLocation.app_url_show).withString("url_show", url).navigation();
            }
            MyLog.d("click_to_see_details" + msg);
        }

    }
}