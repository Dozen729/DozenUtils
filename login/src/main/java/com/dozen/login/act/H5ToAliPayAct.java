package com.dozen.login.act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.HttpConstant;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.view.NumberProgressBar;
import com.dozen.commonbase.view.TitleView;
import com.dozen.login.LoginConstant;
import com.dozen.login.R;
import com.dozen.login.base.LoginMobclickConstant;
import com.dozen.login.net.DataSaveMode;
import com.dozen.login.net.LoginUserHttpUtils;
import com.dozen.login.net.bean.VipInfoResult;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Dozen  2020/9/9
 * @description: 描述
 */
public class H5ToAliPayAct extends CommonActivity {

    private TitleView titleView;
    private NumberProgressBar progressBar;
    private WebView webView;

    private int ALIPAY =102;

    private boolean returnShow = false;
    private boolean closeShow = false;

    @Override
    protected int setLayout() {
        return R.layout.act_h5_dozen;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView(Bundle savedInstanceState) {

        SPUtils.setString(this, LoginConstant.PAY_MODE,"alipay");

        titleView=findViewById(R.id.titleView);
        progressBar=findViewById(R.id.progressBar);
        webView=findViewById(R.id.webView);

        if (AppUtils.checkAliPayInstalled(this)){
            webView.setVisibility(View.INVISIBLE);
            closeShow = true;
        }else {
            webView.setVisibility(View.VISIBLE);
        }


        String web_url = getIntent().getStringExtra("web_url");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if (url.startsWith("http")) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivityForResult(intent, ALIPAY);
                        returnShow = true;
                        return true;
                    } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                    }

                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                if (CommonConstant.umeng_click) {
                    Map<String, String> map = new HashMap<String, String>();//统计埋点
                    map.put("click", "1");
                    MobclickAgent.onEvent(H5ToAliPayAct.this, LoginMobclickConstant.call_api_pay_return_fail, map);
                }
                MyLog.d("aliPayError");
            }
        });

        Map<String,String> webviewHead =new HashMap<>();
        webviewHead.put("Referer", HttpConstant.MPYS_API_BASE_URL);
        webView.loadUrl(web_url,webviewHead);

        webView.setWebChromeClient(webChromeClient);

        WebSettings webSetting = webView.getSettings();
        //使用缓存，否则localstorage等无法使用
        webSetting.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = this.getCacheDir().getAbsolutePath();
        webSetting.setAppCachePath(appCachePath);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//这句很重要
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        //下面两行代码可以防止webview页打开黑屏
        webView.setBackgroundColor(getResources().getColor(R.color.transparent));
        webView.setBackgroundResource(R.color.white);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void checkUser(){
        if (!returnShow){
            return;
        }
        returnShow = false;
        LoginUserHttpUtils.get_userInfo(LoginConstant.GET_UUID(), new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed()&&tag.equals("userinfo")){
                    DataSaveMode.saveUserInfo((VipInfoResult) info);
                    if (DataSaveMode.isVip()){
                        finish();
                    }
                }
            }
        },"userinfo");
    }

    private boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                0);//PackageManager.GET_ACTIVITIES
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== ALIPAY){
            if (closeShow){
                finish();
                return;
            }
            returnShow = true;
            checkUser();
        }
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
}
