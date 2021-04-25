package com.dozen.login.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.view.NumberProgressBar;
import com.dozen.commonbase.view.TitleView;
import com.dozen.login.R;

/**
 * @author: fcg  2020/9/9
 * @description: 描述
 */
public class H5DownloadAct extends CommonActivity {

    private TitleView titleView;
    private NumberProgressBar progressBar;
    private WebView webView;

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

        String web_url = getIntent().getStringExtra("web_url");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(web_url+ "?channel=拍拍识图");
        webView.setWebChromeClient(webChromeClient);

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);

        //使用缓存，否则localstorage等无法使用
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath = this.getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAppCacheEnabled(true);

        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);
        //adwebkit 指的是类（接口）的别名 ，在我们上面的代码中也就是JavascriptInterface类 所以android和js要约定好的就是 类的别名 以及 方法名 即可调用成功。
        webView.addJavascriptInterface(new H5DownloadAct.AndroidJs(),"adwebkit");//adwebkit
    }

    @Override
    protected void initData() {

    }

    /**
     * 点击查看图片
     */
    public class AndroidJs{
        @JavascriptInterface
        public void download(String data) {
            MyLog.d("h5回调: " + data.toString());
            Uri uri = Uri.parse(data.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
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
