package com.dozen.utils.fragment.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.CodePictureCreate;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.commonbase.utils.SMSCodeUtil;
import com.dozen.commonbase.view.roundview.RoundTextView;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class ApplyFragment extends BaseFragment {

    public static final String KEY_TEXT = "apply";

    public static ApplyFragment newInstance(String text) {
        ApplyFragment mineFragment = new ApplyFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private SMSCodeUtil sms1;
    private SMSCodeUtil sms2;
    private SMSCodeUtil sms3;
    private SMSCodeUtil sms4;
    private SMSCodeUtil sms5;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_apply;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name = bundle.getString(KEY_TEXT);

        getContentView().findViewById(R.id.apply_btn1).setOnClickListener(btnClickListener);
    }

    @Override
    protected void setUpData() {
        loadSmsData();
    }

    private void loadSmsData() {

        sms1 = new SMSCodeUtil(getActivity(), Color.parseColor("#FF5722"), Color.parseColor("#000000"), btnSetClick(R.id.apply_sms1), 10);
        sms2 = new SMSCodeUtil(getActivity(), Color.parseColor("#2196F3"), Color.parseColor("#DCDDDD"), btnSetClick(R.id.apply_sms2), 30);
        sms3 = new SMSCodeUtil(getActivity(), Color.parseColor("#FFEB3B"), Color.parseColor("#DCDDDD"), btnSetClick(R.id.apply_sms3), 60);
        sms4 = new SMSCodeUtil(getActivity(), Color.parseColor("#9C27B0"), Color.parseColor("#DCDDDD"), btnSetClick(R.id.apply_sms4), 120);
        sms5 = new SMSCodeUtil(getActivity(), Color.parseColor("#8BC34A"), Color.parseColor("#DCDDDD"), btnSetClick(R.id.apply_sms5), 300);

    }

    private RoundTextView btnSetClick(int id) {
        RoundTextView rtv = getContentView().findViewById(id);
        rtv.setOnClickListener(btnClickListener);
        return rtv;
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.apply_sms1:
                    sms1.start();
                    break;
                case R.id.apply_sms2:
                    sms2.start();
                    break;
                case R.id.apply_sms3:
                    sms3.start();
                    break;
                case R.id.apply_sms4:
                    sms4.start();
                    break;
                case R.id.apply_sms5:
                    sms5.start();
                    break;
                case R.id.apply_btn1:
                    showCodePictureImage();
                    break;
            }
        }
    };

    private void showCodePictureImage(){

        EditText etInput = getContentView().findViewById(R.id.apply_code_et);
        String text=etInput.getText().toString();
        if (EmptyCheckUtil.isEmpty(text)){
            text=etInput.getHint().toString();
        }

        ImageView iv1 = getContentView().findViewById(R.id.apply_image1);
        Bitmap bitmap1 = CodePictureCreate.createQRCodeBitmap(text,200,200,"UTF-8","M","10",Color.BLACK,Color.WHITE);
        iv1.setImageBitmap(bitmap1);


        ImageView iv2 = getContentView().findViewById(R.id.apply_image2);
        Bitmap bitmap2 = CodePictureCreate.createQRCodeBitmap(text,500,500,"UTF-8","M","3",Color.RED,Color.BLUE);
        iv2.setImageBitmap(bitmap2);

        ImageView iv3 = getContentView().findViewById(R.id.apply_image3);
        Bitmap bitmap3 = CodePictureCreate.createQRCodeBitmap(text,1000,1000,"UTF-8","M","1",Color.parseColor("#2196F3"),Color.parseColor("#FFEB3B"));
        iv3.setImageBitmap(bitmap3);
    }

}
