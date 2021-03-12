package com.dozen.utils.fragment.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.dialog.CurrencyOneKeyDialog;
import com.dozen.commonbase.dialog.CurrencyTipDialog;
import com.dozen.commonbase.dialog.DialogCommonListener;
import com.dozen.commonbase.dialog.LoadingDialog;
import com.dozen.commonbase.dialog.LoginTipDialog;
import com.dozen.commonbase.dialog.VersionUpdateDialog;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class DialogShowFragment extends BaseFragment {

    public static final String KEY_TEXT = "dialog";

    public static DialogShowFragment newInstance(String text) {
        DialogShowFragment mineFragment = new DialogShowFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_dialog_show;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        btnSetClick(R.id.dialog_show1);
        btnSetClick(R.id.dialog_show2);
        btnSetClick(R.id.dialog_show3);
        btnSetClick(R.id.dialog_show4);
        btnSetClick(R.id.dialog_show5);
    }

    @Override
    protected void setUpData() {

    }

    private void btnSetClick(int id){
        Button btn = getContentView().findViewById(id);
        btn.setOnClickListener(btnClickListener);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.dialog_show1:
                    openDialog1();
                    break;
                case R.id.dialog_show2:
                    openDialog2();
                    break;
                case R.id.dialog_show3:
                    openDialog3();
                    break;
                case R.id.dialog_show4:
                    openDialog4();
                    break;
                case R.id.dialog_show5:
                    openDialog5();
                    break;

            }
        }
    };

    private void openDialog1() {
        CurrencyOneKeyDialog dialog = new CurrencyOneKeyDialog(getBaseContext());
        dialog.setDialogCommonListener(new DialogCommonListener() {
            @Override
            public void isConfirm() {
                ARouter.getInstance().build("/view/title").navigation();
            }

            @Override
            public void isCancel() {

            }

            @Override
            public void isClose() {

            }
        });
        dialog.show();
    }

    private void openDialog2(){
        CurrencyTipDialog dialog = new CurrencyTipDialog(getBaseContext());
        dialog.setDialogCommonListener(new DialogCommonListener() {
            @Override
            public void isConfirm() {
                StyleToastUtil.success("confirm");
            }

            @Override
            public void isCancel() {
                StyleToastUtil.error("cancel");
            }

            @Override
            public void isClose() {

            }
        });
        dialog.show();
    }

    private void openDialog3(){
        LoadingDialog dialog = new LoadingDialog(getBaseContext());
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        },3000);
    }

    private void openDialog4(){
        LoginTipDialog dialog = new LoginTipDialog(getBaseContext());
        dialog.setDialogCommonListener(new DialogCommonListener() {
            @Override
            public void isConfirm() {
                StyleToastUtil.success("confirm");
            }

            @Override
            public void isCancel() {
                StyleToastUtil.error("cancel");
            }

            @Override
            public void isClose() {

            }
        });
        dialog.show();
    }

    private void openDialog5(){
        VersionUpdateDialog dialog = new VersionUpdateDialog(getBaseContext());
        dialog.setDialogCommonListener(new DialogCommonListener() {
            @Override
            public void isConfirm() {
                StyleToastUtil.success("confirm");
            }

            @Override
            public void isCancel() {
                StyleToastUtil.error("cancel");
            }

            @Override
            public void isClose() {

            }
        });
        dialog.show();
    }

}
