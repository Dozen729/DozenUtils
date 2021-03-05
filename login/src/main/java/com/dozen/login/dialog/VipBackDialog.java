package com.dozen.login.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dozen.commonbase.dialog.DialogCommonListener;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.commonbase.utils.TimeUtil;
import com.dozen.login.R;
import com.dozen.login.view.CusFntTextView;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/11/17
 */
public class VipBackDialog extends Dialog {

    private Context mContext;

    private DialogCommonListener dialogCommonListener;
    private CusFntTextView tvShowTime;

    public void setDialogCommonListener(DialogCommonListener dialogCommonListener) {
        this.dialogCommonListener = dialogCommonListener;
    }

    public VipBackDialog(@NonNull Context context) {
        //设置对话框样式
        super(context, R.style.defaultDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vip_back_dozen);
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
        lp.width = (int) (ScreenUtils.getScreenWidth(mContext)*0.8);
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        //设置外面不可点击
        setCancelable(false);
    }

    //初始化
    private void initView() {
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                dialogCommonListener.isConfirm();
            }
        });
        findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialogCommonListener.isCancel();
            }
        });
        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                dialogCommonListener.isClose();
            }
        });

        tvShowTime = findViewById(R.id.tv_show_reduce_time);
    }

    @Override
    public void show() {
        super.show();
        timeReduce();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (timer!=null){
            timer.cancel();
        }
    }

    private CountDownTimer timer;

    private void timeReduce(){
        timer = new CountDownTimer(10*60*1000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                tvShowTime.setText(TimeUtil.getUserSurplusTimeToHMS(millisUntilFinished));
            }

            public void onFinish() {
                dismiss();
            }
        };
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start();
    }
}
