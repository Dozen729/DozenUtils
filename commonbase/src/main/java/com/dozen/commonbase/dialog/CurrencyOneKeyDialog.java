package com.dozen.commonbase.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dozen.commonbase.R;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.commonbase.view.roundview.RoundTextView;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/17
 */
public class CurrencyOneKeyDialog extends Dialog {
    private Context mContext;
    private TextView tvTitle;
    private TextView tvDetail;
    private RoundTextView rtvOK;
    private ImageView ivClose;

    private DialogCommonListener dialogCommonListener;

    public void setDialogCommonListener(DialogCommonListener dialogCommonListener) {
        this.dialogCommonListener = dialogCommonListener;
    }

    public CurrencyOneKeyDialog(@NonNull Context context) {
        //设置对话框样式
        super(context, R.style.defaultDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_currency_one_key);
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

        tvTitle = findViewById(R.id.tv_one_key_title_show);
        tvDetail = findViewById(R.id.tv_one_key_detail);
        rtvOK = findViewById(R.id.tv_one_key_ok);
        ivClose = findViewById(R.id.iv_one_key_close);

        rtvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                dialogCommonListener.isConfirm();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                dialogCommonListener.isClose();
            }
        });

    }

    //设置标题颜色和大小，必须在dialog.show之后设置
    public void setTitleColorAndSize(int color, float size, String content) {
        if (color != 0) {
            tvTitle.setTextColor(color);
        }
        if (size != 0) {
            tvTitle.setTextSize(ScreenUtils.px2sp(mContext, size));
        }
        if (!content.equals("")) {
            tvTitle.setText(content);
        }
    }

    //设置内容颜色和大小，必须在dialog.show之后设置
    public void setDetailColorAndSize(int color, float size, String content) {
        if (color != 0) {
            tvDetail.setTextColor(color);
        }
        if (size != 0) {
            tvDetail.setTextSize(ScreenUtils.px2sp(mContext, size));
        }
        if (!content.equals("")) {
            tvDetail.setText(content);
        }
    }

    //设置确认按钮颜色和大小，必须在dialog.show之后设置
    public void setOKWordsColorAndSize(int color, float size, String content) {
        if (color != 0) {
            rtvOK.setTextColor(color);
        }
        if (size != 0) {
            rtvOK.setTextSize(ScreenUtils.px2sp(mContext, size));
        }
        if (!content.equals("")) {
            rtvOK.setText(content);
        }
    }
}
