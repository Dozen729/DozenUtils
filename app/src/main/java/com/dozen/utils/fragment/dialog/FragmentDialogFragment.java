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
import com.dozen.commonbase.dialog.fragment.BaseDialog;
import com.dozen.commonbase.dialog.fragment.CommonDialog;
import com.dozen.commonbase.dialog.fragment.ViewConvertListener;
import com.dozen.commonbase.dialog.fragment.ViewHolder;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.utils.R;
import com.dozen.utils.fragment.dialog.fd.TestOneDialog;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class FragmentDialogFragment extends BaseFragment {

    public static final String KEY_TEXT = "dialog";

    public static FragmentDialogFragment newInstance(String text) {
        FragmentDialogFragment mineFragment = new FragmentDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_fragment_dialog;
    }

        @Override
    protected void setUpView(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        btnSetClick(R.id.fragment_dialog_show1);
        btnSetClick(R.id.fragment_dialog_show2);
        btnSetClick(R.id.fragment_dialog_show3);
        btnSetClick(R.id.fragment_dialog_show4);
        btnSetClick(R.id.fragment_dialog_show5);
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
                case R.id.fragment_dialog_show1:
                    openDialog1();
                    break;
                case R.id.fragment_dialog_show2:
                    openDialog2();
                    break;
                case R.id.fragment_dialog_show3:
                    openDialog3();
                    break;
                case R.id.fragment_dialog_show4:
                    openDialog4();
                    break;
                case R.id.fragment_dialog_show5:
                    openDialog5();
                    break;

            }
        }
    };

    private void openDialog1() {
        CommonDialog commonDialog = new CommonDialog();
        commonDialog.setLayoutId(R.layout.fragment_fragment_dialog);
        commonDialog.setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, BaseDialog dialog) {
                holder.getView(R.id.fragment_dialog_show1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
            }
        });
        commonDialog.show(getFragmentManager());
    }

    private void openDialog2(){
        CommonDialog commonDialog = new CommonDialog();
        commonDialog.setLayoutId(R.layout.fragment_fragment_dialog);
        commonDialog.setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, BaseDialog dialog) {
                holder.getView(R.id.fragment_dialog_show2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
            }
        });
        getFragmentManager().beginTransaction().add(commonDialog,"dialog").commit();
    }

    private void openDialog3(){
        CommonDialog commonDialog = new CommonDialog();
        commonDialog.setLayoutId(R.layout.fragment_fragment_dialog);
        commonDialog.setConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, BaseDialog dialog) {
                holder.getView(R.id.fragment_dialog_show3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
            }
        });
        commonDialog.show(getFragmentManager());
    }

    private void openDialog4(){
        TestOneDialog testOneDialog = new TestOneDialog();
        testOneDialog.setDialogCallBack(new TestOneDialog.DialogCallBack() {
            @Override
            public void click() {
                testOneDialog.dismiss();
            }
        });
        testOneDialog.show(getFragmentManager());
    }

    private void openDialog5(){
        TestOneDialog testOneDialog = new TestOneDialog();
        testOneDialog.setViewConvertListener(new ViewConvertListener() {
            @Override
            public void convertView(ViewHolder holder, BaseDialog dialog) {
                holder.getView(R.id.fragment_dialog_show5).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testOneDialog.dismiss();
                    }
                });
            }
        });
        getFragmentManager().beginTransaction().add(testOneDialog,"dialog").commit();
    }

}
