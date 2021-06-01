package com.dozen.login.act;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.MobileUtil;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.roundview.RoundTextView;
import com.dozen.login.R;
import com.dozen.login.base.LoginMobclickConstant;
import com.dozen.login.net.LoginUserHttpUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/26
 */
@Route(path = ARouterLocation.login_complaint)
public class ComplaintAct extends CommonActivity {

    private EditText etPhone;
    private TextView tvPhoneLimit;
    private TextView tvPhoneLimitTotal;

    private EditText etProblem;
    private TextView tvProblemLimit;
    private TextView tvProblemLimitTotal;

    private RoundTextView rtvCommit;

    private int phoneNumber = 11;
    private int problemNumber = 300;

    @Override
    protected int setLayout() {
        return R.layout.activity_complaint;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        etPhone = findViewById(R.id.et_complaint_phone);
        tvPhoneLimit = findViewById(R.id.tv_complaint_phone_limit);
        tvPhoneLimitTotal = findViewById(R.id.tv_complaint_phone_total);

        etProblem = findViewById(R.id.et_complaint_problem);
        tvProblemLimit = findViewById(R.id.tv_complaint_problem_limit);
        tvProblemLimitTotal = findViewById(R.id.tv_complaint_problem_total);


        rtvCommit = findViewById(R.id.rtv_complaint_commit);
    }

    @Override
    protected void initData() {

        rtvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback();
            }
        });

        initContent();
    }

    private void sendFeedback() {
        String phone = etPhone.getText().toString();
        String problem = etProblem.getText().toString();

        if (!phone.equals("") && MobileUtil.checkPhone(phone)) {
            if (!problem.equals("")) {
                pushData(phone, problem);
            } else {
                StyleToastUtil.info("请输入问题描述");
            }
        } else {
            StyleToastUtil.info("请输入正确的手机号码");
        }

    }

    private void pushData(String mobile, String content) {
        LoginUserHttpUtils.feedback(mobile, content,"2", new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                if (info.isSucceed() && tag.equals("feedback")) {
                    StyleToastUtil.info("反馈成功");
                    finish();
                }
            }
        }, "feedback");
    }

    @SuppressLint("SetTextI18n")
    private void initContent() {
        etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(phoneNumber)});
        etProblem.setFilters(new InputFilter[]{new InputFilter.LengthFilter(problemNumber)});
        tvPhoneLimitTotal.setText(phoneNumber + "");
        tvProblemLimitTotal.setText(problemNumber + "");

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvPhoneLimit.setText(editable.length() + "");
            }
        });
        etProblem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvProblemLimit.setText(editable.length() + "");
                if (!etPhone.getText().toString().equals("") && editable.length() > 0) {
                    rtvCommit.getDelegate().setBackgroundColor(Color.parseColor("#FF11C2BF"));
                }
            }
        });
    }

    private InputFilter inputFilter = new InputFilter() {

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");//只能输入汉字,英文,数字

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (matcher.find()) {
                return "";
            }
            return null;
        }
    };
}
