package com.dozen.login.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.utils.CommonUtils;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.NetworkUtils;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.roundview.RoundLinearLayout;
import com.dozen.login.LoginConstant;
import com.dozen.login.R;
import com.dozen.login.net.DataSaveMode;
import com.dozen.login.net.LoginUserHttpUtils;
import com.dozen.login.net.LoginUserUrl;
import com.dozen.login.net.bean.UserLoginResult;

public class WXLoginActivity extends CommonActivity {

    private TextView tvUserRule;
    private TextView tvPrivatePolicy;
    private ImageView ivAgreePolicy;
    private boolean isAgree = false;

    private RoundLinearLayout wxLogin;

    private int WX_LOGIN_CODE = 112;
    private int BIND_PHONE = 114;

    @Override
    protected int setLayout() {
        return R.layout.activity_wx_login_dozen;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvUserRule = findViewById(R.id.tvUserRule);
        tvPrivatePolicy = findViewById(R.id.tvPrivacyPolicy);
        ivAgreePolicy = findViewById(R.id.iv_agree_select);

        wxLogin = findViewById(R.id.wx_login);
    }

    @Override
    protected void initData() {
        tvUserRule.setOnClickListener(registerListener);
        tvPrivatePolicy.setOnClickListener(registerListener);
        ivAgreePolicy.setOnClickListener(registerListener);
        wxLogin.setOnClickListener(registerListener);

    }

    private View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tvUserRule) {
                userRule();
            } else if (id == R.id.tvPrivacyPolicy) {
                privatePolicy();
            } else if (id == R.id.iv_agree_select) {
                agreeChange();
            } else if (id == R.id.wx_login) {
                if (CommonUtils.isFastClick()) {
                    wxLogin();
                }
            }
        }
    };

    //微信登录
    private void wxLogin() {
        if (!NetworkUtils.checkWifiAndGPRS(this)){
            StyleToastUtil.info("网络断开，请检查网络是否连接？");
            return;
        }
        if (isAgree) {
            openWXLogin();
        } else {
            StyleToastUtil.info("请同意隐私政策");
        }
    }

    //打开微信登录
    private void openWXLogin() {
        Bundle bundle = new Bundle();
        bundle.putString("wx_type", "wx_login");

        ARouter.getInstance().build("/seaking/wechat")
                .withBundle("wxBundle", bundle).navigation(this, WX_LOGIN_CODE);

    }

    //微信登录
    private void loginWX() {
        String code = SPUtils.getString(this, "wx_login_code", "");
        if (EmptyCheckUtil.isEmpty(code)) {
            StyleToastUtil.info("登录失败");
            return;
        }
        LoginUserHttpUtils.wxLogin(code, new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed() && tag.equals("loginWX")) {

                    UserLoginResult wechatResult = (UserLoginResult) info;
                    if (EmptyCheckUtil.isEmpty(wechatResult.data.sign)){
                        DataSaveMode.saveUserData(wechatResult);
                        StyleToastUtil.success("登录成功");
                        finish();
                    }else {
                        openBindPhone(wechatResult.data.sign);
                    }

                }else {
                    StyleToastUtil.info(info.getMsg());
                }
                SPUtils.setString(getBaseContext(), "wx_login_code", "");
            }
        }, "loginWX");
    }

    private void openBindPhone(String sign) {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        intent.putExtra("sign", sign);
        startActivityForResult(intent, BIND_PHONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == WX_LOGIN_CODE) {
                loginWX();
            } else if (requestCode == BIND_PHONE) {
                finish();
            } else {
                finish();
            }
        }
    }


    private void agreeChange() {
        if (!isAgree) {
            ivAgreePolicy.setImageResource(R.mipmap.login_is_agree_dozen);
            isAgree = true;
        } else {
            ivAgreePolicy.setImageResource(R.mipmap.login_no_agree_dozen);
            isAgree = false;
        }
    }

    private void userRule() {
        LoginConstant.h5UrlShow(this, LoginUserUrl.terms_for_use);
    }

    private void privatePolicy() {
        LoginConstant.h5UrlShow(this, LoginUserUrl.user_agreement);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}