package com.dozen.utils.fragment.dialog.fd;

import android.view.View;

import com.dozen.commonbase.dialog.fragment.BaseDialog;
import com.dozen.commonbase.dialog.fragment.ViewConvertListener;
import com.dozen.commonbase.dialog.fragment.ViewHolder;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/19
 */
public class TestOneDialog extends BaseDialog {

    private DialogCallBack dialogCallBack;
    private ViewConvertListener viewConvertListener;

    @Override
    public int setUpLayoutId() {
        return R.layout.fragment_fragment_dialog;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
        holder.getView(R.id.fragment_dialog_show4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallBack.click();
            }
        });
        if (viewConvertListener != null) {
            viewConvertListener.convertView(holder,dialog);
        }
    }

    public void setViewConvertListener(ViewConvertListener viewConvertListener) {
        this.viewConvertListener = viewConvertListener;
    }

    public void setDialogCallBack(DialogCallBack dialogCallBack) {
        this.dialogCallBack = dialogCallBack;
    }

    public interface DialogCallBack{
        void click();
    }
}
