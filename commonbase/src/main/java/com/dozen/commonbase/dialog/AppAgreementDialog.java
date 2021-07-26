package com.dozen.commonbase.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dozen.commonbase.R;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.commonbase.utils.StringUtils;

import java.util.List;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/9/10
 */
public class AppAgreementDialog extends Dialog {

    private Context mContext;
    private TextView tvHead;
    private TextView tvAgreement;
    private TextView tvUserRule;

    public AppAgreementDialog(@NonNull Context context) {
        //设置对话框样式
        super(context, R.style.defaultDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_app_agreenment);
        setDialogWindowAttr();
        initView();
    }

    //设置宽高位置等属性
    private void setDialogWindowAttr() {
        Window window = getWindow();
        //中间显示
        window.setGravity(Gravity.CENTER);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = (int) (ScreenUtils.getScreenWidth(mContext) * 0.8);
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        //设置外面不可点击
        setCancelable(false);
    }

    //初始化
    private void initView() {
        findViewById(R.id.tvOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                OkListener.OK();
            }
        });
        findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkListener.Cancel();
            }
        });
        tvHead = findViewById(R.id.tvContext);
        tvAgreement = findViewById(R.id.content_agreement);
        tvUserRule = findViewById(R.id.tvUserRule);
    }

    public void setHeadContext(String head) {
        tvHead.setText(head);
    }

    public void setBodyContext(String body) {
        initMiddleTerm(tvAgreement, body);
    }

    @Override
    public void show() {
        super.show();
        initMiddleTerm(tvAgreement, "");
        initBottomTerm(tvUserRule);
    }

    private void initMiddleTerm(TextView tx_bottom_term, String body) {

        String middleText = mContext.getResources().getString(R.string.agreement);

        if (!EmptyCheckUtil.isEmpty(body)) {
            middleText = body;
        }

        SpannableString spanTextYinSi = new SpannableString(middleText);

        List<Integer> listUR = StringUtils.searchAllIndex(middleText, "《使用条款》");

        for (Integer in : listUR) {
            // 用户协议
            spanTextYinSi.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.title_bg2));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View view) {
                    OkListener.UserRule();
                }
            }, in, in + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        List<Integer> listPP = StringUtils.searchAllIndex(middleText, "《隐私政策》");

        for (Integer in : listPP) {
            // 隐私政策
            spanTextYinSi.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.title_bg2));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View view) {
                    OkListener.PrivatePolicy();
                }
            }, in, in + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tx_bottom_term.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        tx_bottom_term.setText(spanTextYinSi);
        tx_bottom_term.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
    }

    private void initBottomTerm(TextView tx_bottom_term) {
        String middleText = mContext.getResources().getString(R.string.agreement_end);

        SpannableString spanTextYinSi = new SpannableString(middleText);

        List<Integer> listUR = StringUtils.searchAllIndex(middleText, "《使用条款》");

        for (Integer in : listUR) {
            // 用户协议
            spanTextYinSi.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.title_bg2));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View view) {
                    OkListener.UserRule();
                }
            }, in, in + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        List<Integer> listPP = StringUtils.searchAllIndex(middleText, "《隐私政策》");

        for (Integer in : listPP) {
            // 隐私政策
            spanTextYinSi.setSpan(new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.title_bg2));       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View view) {
                    OkListener.PrivatePolicy();
                }
            }, in, in + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tx_bottom_term.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        tx_bottom_term.setText(spanTextYinSi);
        tx_bottom_term.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
    }

    private onOkListener OkListener;

    public void setOkListener(onOkListener okListener) {
        OkListener = okListener;
    }

    //回调接口
    public interface onOkListener {
        void UserRule();

        void PrivatePolicy();

        void OK();

        void Cancel();
    }
}
