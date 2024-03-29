package com.dozen.login.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.login.R;

/**
 * 文件描述：
 * 作者：Dozen
 * 创建时间：2020/9/10
 */
public class VipTipDialog extends Dialog {

    private Context mContext;

    public VipTipDialog(@NonNull Context context) {
        //设置对话框样式
        super(context, R.style.defaultDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vip_tip_dozen);
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
        findViewById(R.id.tvOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ARouterLocation.login_h5_zf).withString("into_vip_tip", "在线客服").navigation();
                dismiss();
                if (okListener !=null){
                    okListener.OK();
                }
            }
        });

        findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (okListener !=null) {
                    okListener.cancel();
                }
            }
        });
    }

    private onOkListener okListener;
    public void setOkListener(onOkListener okListener) {
        this.okListener = okListener;
    }
    //回调接口
    public interface onOkListener {
        void OK();
        void cancel();
    }
}
