package com.dozen.commonbase.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.dozen.commonbase.R;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.commonbase.view.CircleLoadingView;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/11/17
 */
public class LoadingDialog extends Dialog {

    private Context mContext;

    private CircleLoadingView loadingView;

    public LoadingDialog(@NonNull Context context) {
        //设置对话框样式
        super(context, R.style.defaultDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setDialogWindowAttr();
        initView();
    }

    private void initView() {
        loadingView = findViewById(R.id.loading_view);
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

    @Override
    public void show() {
        super.show();
        loadingView.startDotAnimator();
    }
}
