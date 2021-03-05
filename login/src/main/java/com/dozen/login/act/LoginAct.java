package com.dozen.login.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.utils.CommonUtils;
import com.dozen.commonbase.utils.MobileUtil;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.TitleView;
import com.dozen.commonbase.view.roundview.RoundTextView;
import com.dozen.login.LoginConstant;
import com.dozen.login.R;
import com.dozen.login.net.DataSaveMode;
import com.dozen.login.net.LoginUserHttpUtils;
import com.dozen.login.net.LoginUserUrl;
import com.dozen.login.net.bean.UserLoginResult;

public class LoginAct extends CommonActivity {

    private TextView tvUserRule;
    private TextView tvPrivatePolicy;
    private TextView tvToResetPassword;
    private TextView tvToRegister;
    private ImageView ivAgreePolicy;

    private EditText etPhone;
    private EditText etPassWord;

    private TitleView topTitle;

    private RoundTextView btnLogin;

    private boolean isAgree=false;

    @Override
    protected int setLayout() {
        return R.layout.activity_login_dozen;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvUserRule=findViewById(R.id.tvUserRule);
        tvPrivatePolicy=findViewById(R.id.tvPrivacyPolicy);
        ivAgreePolicy=findViewById(R.id.iv_agree_select);
        topTitle=findViewById(R.id.titleView);
        tvToRegister=findViewById(R.id.tv_open_register);
        tvToResetPassword=findViewById(R.id.tv_open_reset_password);


        etPhone=findViewById(R.id.et_user_phone);
        etPassWord=findViewById(R.id.et_user_password);
        etPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
        etPassWord.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });

        btnLogin=findViewById(R.id.btn_login_pull);

        btnLogin.setOnClickListener(loginListener);
        tvUserRule.setOnClickListener(loginListener);
        tvPrivatePolicy.setOnClickListener(loginListener);
        ivAgreePolicy.setOnClickListener(loginListener);
        tvToRegister.setOnClickListener(loginListener);
        tvToResetPassword.setOnClickListener(loginListener);

        topTitle.setRightTextBtn("注册");
        topTitle.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginAct.this, RegisterAct.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void saveUserData(UserLoginResult login){
        DataSaveMode.saveUserData(login);
//        SPUtils.setString(LoginAct.this,PaiPaiConstant.USER_PASSWORD,etPassWord.getText().toString());
        etPassWord.setClickable(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StyleToastUtil.success("登录成功");
                setResult(RESULT_OK);
                finish();
            }
        },1000);
    }

    private View.OnClickListener loginListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tvUserRule) {
                userRule();
            } else if (id == R.id.tvPrivacyPolicy) {
                privatePolicy();
            } else if (id == R.id.iv_agree_select) {
                agreeChange();
            } else if (id == R.id.tv_open_register) {
                Intent intentR = new Intent(LoginAct.this, RegisterAct.class);
                startActivity(intentR);
            } else if (id == R.id.tv_open_reset_password) {
                Intent intent = new Intent(LoginAct.this, ResetPasswordAct.class);
                startActivity(intent);
            } else if (id == R.id.btn_login_pull) {
                if (CommonUtils.isFastClick()) {
                    userLogin();
                }
            }
        }
    };

    private void userLogin() {
        String phone=etPhone.getText().toString();
        String password=etPassWord.getText().toString();
        if (!phone.equals("") && MobileUtil.checkPhone(phone)){
            if (!password.equals("") && password.length()==6){
                if (isAgree){
                    loginPhone(phone,password);
                    etPassWord.setClickable(false);
                }else {
                    StyleToastUtil.info("请同意隐私政策");
                }
            }else {
                StyleToastUtil.info("请输入6位登录密码");
            }
        }else {
            StyleToastUtil.info("请输入正确的手机号码");
        }
    }

    private void loginPhone(String phone,String password){
        LoginUserHttpUtils.loginMobile(phone, password, new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed()&&tag.equals("login")){
                    UserLoginResult login= (UserLoginResult) info;
                    saveUserData(login);
                }else {
                    StyleToastUtil.error(info.getMsg());
                }
            }
        },"login");
    }

    private void agreeChange(){
        if (!isAgree){
            ivAgreePolicy.setImageResource(R.mipmap.login_is_agree_dozen);
            isAgree=true;
        }else {
            ivAgreePolicy.setImageResource(R.mipmap.login_no_agree_dozen);
            isAgree=false;
        }
    }

    private void userRule() {
        LoginConstant.h5UrlShow(this,LoginUserUrl.terms_for_use);
    }

    private void privatePolicy() {
        LoginConstant.h5UrlShow(this,LoginUserUrl.user_agreement);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.getBoolean(this, LoginConstant.IS_Phone_LOGIN,false)){
            setResult(RESULT_OK);
            finish();
        }
    }
}