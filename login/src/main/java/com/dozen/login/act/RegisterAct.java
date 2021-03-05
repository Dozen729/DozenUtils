package com.dozen.login.act;

import android.content.Intent;
import android.graphics.Color;
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
import com.dozen.commonbase.utils.SMSCodeUtil;
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

public class RegisterAct extends CommonActivity {

    private TextView tvUserRule;
    private TextView tvPrivatePolicy;
    private ImageView ivAgreePolicy;

    private EditText etPhone;
    private EditText etPassWord;
    private EditText etCode;

    private TitleView topTitle;

    private RoundTextView tvCodeVerification;
    private SMSCodeUtil smsCodeUtil;

    private RoundTextView btnRegister;

    private String phoneValue;
    private String passwordValue;

    private boolean isAgree=false;

    @Override
    protected int setLayout() {
        return R.layout.activity_register_dozen;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvUserRule=findViewById(R.id.tvUserRule);
        tvPrivatePolicy=findViewById(R.id.tvPrivacyPolicy);
        ivAgreePolicy=findViewById(R.id.iv_agree_select);
        topTitle=findViewById(R.id.titleView);

        tvCodeVerification=findViewById(R.id.tv_user_get_code_and_time);

        etPhone=findViewById(R.id.et_user_phone);
        etPassWord=findViewById(R.id.et_user_password);
        etCode=findViewById(R.id.et_user_verification);

        etPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
        etPassWord.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
        etCode.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });


        btnRegister=findViewById(R.id.btn_user_register);

        btnRegister.setOnClickListener(registerListener);
        tvCodeVerification.setOnClickListener(registerListener);
        tvUserRule.setOnClickListener(registerListener);
        tvPrivatePolicy.setOnClickListener(registerListener);
        ivAgreePolicy.setOnClickListener(registerListener);

        topTitle.setRightTextBtn("登录");
        topTitle.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterAct.this,LoginAct.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        smsCodeUtil=new SMSCodeUtil(this, getResources().getColor(R.color.login_code_text),Color.parseColor("#FFDFDFDF"),tvCodeVerification,120);
    }

    private void saveUserData(UserLoginResult login){
        DataSaveMode.saveUserData(login);
//        SPUtils.setString(RegisterAct.this,PaiPaiConstant.USER_PASSWORD,passwordValue);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StyleToastUtil.success("登录成功");
                setResult(RESULT_OK);
                finish();
            }
        },2000);
    }

    private View.OnClickListener registerListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tvUserRule) {
                userRule();
            } else if (id == R.id.tvPrivacyPolicy) {
                privatePolicy();
            } else if (id == R.id.iv_agree_select) {
                agreeChange();
            } else if (id == R.id.tv_user_get_code_and_time) {
                getCode();
            } else if (id == R.id.btn_user_register) {
                if (CommonUtils.isFastClick()) {
                    registerUser();
                }
            }
        }
    };

    //注册信息
    private void registerUser() {
        String phone=etPhone.getText().toString();
        String password=etPassWord.getText().toString();
        String sms=etCode.getText().toString();
        if (!phone.equals("") && MobileUtil.checkPhone(phone)){
            if (!password.equals("") && password.length()==6){
                if (!sms.equals("")){
                    if (isAgree){
                        phoneValue=phone;
                        passwordValue=password;
                        registerPhone(phone,password,sms);
                    }else {
                        StyleToastUtil.info("请同意隐私政策");
                    }
                }else {
                    StyleToastUtil.info("请输入验证码");
                }
            }else {
                StyleToastUtil.info("请输入6位登录密码");
            }
        }else {
            StyleToastUtil.info("请输入正确的手机号码");
        }
    }

    //注册手机
    private void registerPhone(final String phone, final String password, String sms){
        LoginUserHttpUtils.register(LoginConstant.GET_UUID(), phone, sms, password, new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                btnRegister.setClickable(true);
                if (info.isSucceed()&&tag.equals("register")){
                        StyleToastUtil.success("注册成功");
                        loginPhone(phone,password);
                }else {
                    StyleToastUtil.info(info.getMsg());
                }
            }
        },"register");
    }

    //登录手机
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

    //获取验证码
    private void getCode() {
        String phone=etPhone.getText().toString();
        String password=etPassWord.getText().toString();
        if (!phone.equals("") && MobileUtil.checkPhone(phone)){
            if (!password.equals("") && password.length()==6){
                smsCodeUtil.start();
                etPassWord.setClickable(false);
                LoginUserHttpUtils.sendCode(phone, LoginConstant.REGISTER_CODE, new CallBack() {
                    @Override
                    public void onRequested(ResultInfo info, Object tag) {
                        if (info.isSucceed()&&tag.equals("code")){

                        }else {
                            StyleToastUtil.info("获取验证码失败,请稍后重试");
                        }
                    }
                },"code");
            }else {
                StyleToastUtil.info("请输入6位登录密码");
            }
        }else {
            StyleToastUtil.info("请输入正确的手机号码");
        }
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
        phoneValue="";
        passwordValue="";
        if (SPUtils.getBoolean(this, LoginConstant.IS_Phone_LOGIN,false)){
            setResult(RESULT_OK);
            finish();
        }
    }
}