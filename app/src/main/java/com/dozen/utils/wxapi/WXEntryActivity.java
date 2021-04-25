package com.dozen.utils.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.utils.FileUtil;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.utils.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Objects;
import java.util.Random;

/**
 * @author: Dozen
 * @description:调起WX，每次都会在这里回调
 * @time: 2020/12/3
 */
@Route(path = "/seaking/wechat")
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Autowired
    Bundle wxBundle;

    private IWXAPI api;
    private String wx_type;
    private boolean isRun = false;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        Random random = new Random();
        int num = random.nextInt(5000) % (5000 - 10 + 1) + 10;
        WXConstant.WX_BACK = WXConstant.WX_BACK + num;

        api = WXAPIFactory.createWXAPI(this, WXConstant.APP_ID, false);

        api.registerApp(WXConstant.APP_ID);

        if (wxBundle != null && !wxBundle.isEmpty()) {
            if (wxBundle.containsKey(WXConstant.wx_type)){
                startWX(Objects.requireNonNull(wxBundle.getString(WXConstant.wx_type)));
            }else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void startWX(String type) {
        wx_type = type;
        switch (type) {
            case WXConstant.wx_login:
                loginWX();
                break;
            case WXConstant.share_url:
                shareUrl();
                break;
            case WXConstant.share_picture:
                sharePicture();
                break;
        }
    }

    //分享图片
    private void sharePicture() {

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String path = bundle.getString(WXConstant.share_picture);

        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject();
        imgObj.imagePath = path;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;


        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
        msg.thumbData = FileUtil.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    //分享链接
    private void shareUrl() {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String url = bundle.getString(WXConstant.share_url);

        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "邀请您加入";
        msg.description = "下载APP，拍照能赚钱，每日瓜分百万红包，新用户登录即送18元现金，人傻钱多，速来！";

        Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = FileUtil.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "web";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    //微信登录
    private void loginWX() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = WXConstant.WX_BACK;
        api.sendReq(req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRun) {
            isRun = true;
        } else {
            isRun = false;
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        switch (wx_type) {
            case WXConstant.wx_login:

                break;
            case WXConstant.share_url:
                finish();
                break;
            case WXConstant.share_picture:
                finish();
                break;
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        switch (wx_type) {
            case WXConstant.wx_login:

                break;
            case WXConstant.share_url:
                finish();
                break;
            case WXConstant.share_picture:
                finish();
                break;
        }
        switch (baseReq.getType()) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                break;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (wx_type) {
            case WXConstant.wx_login:
                if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                    SendAuth.Resp resp = (SendAuth.Resp) baseResp;

                    String code = resp.code;
                    String url = resp.url;
                    String state = resp.state;
                    WXConstant.code = code;
                    SPUtils.setString(this,WXConstant.wx_login_code,code);
                    setResult(RESULT_OK);
                } else {
                    StyleToastUtil.error("授权失败");
                    setResult(RESULT_CANCELED);
                }
                break;
            case WXConstant.share_url:
                break;
            case WXConstant.share_picture:
                break;
        }
        finish();

    }
}
