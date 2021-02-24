package com.dozen.commonbase.view.sixinput;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.dozen.commonbase.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/26
 */
public class SixInputView extends LinearLayout {
    private LastInputEditText etone;
    private LastInputEditText ettwo;
    private LastInputEditText etthree;
    private LastInputEditText etfour;
    private LastInputEditText etfive;
    private LastInputEditText etsix;
    private Context mContext;
    private View converview;

    private StringBuilder stringBuilder;
    private InputMethodManager imm;

    public SixInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        imm = (InputMethodManager)mContext.getSystemService(INPUT_METHOD_SERVICE);
        initView();
    }

    private void initView() {
        converview = LayoutInflater.from(mContext).inflate(R.layout.view_six_input, null);
        etone = (LastInputEditText) converview.findViewById(R.id.et_one);
        ettwo = (LastInputEditText) converview.findViewById(R.id.et_two);
        etthree = (LastInputEditText) converview.findViewById(R.id.et_three);
        etfour = (LastInputEditText) converview.findViewById(R.id.et_four);
        etfive = (LastInputEditText) converview.findViewById(R.id.et_five);
        etsix = (LastInputEditText) converview.findViewById(R.id.et_six);

        etone.addTextChangedListener(textWatcher);
        ettwo.addTextChangedListener(textWatcher);
        etthree.addTextChangedListener(textWatcher);
        etfour.addTextChangedListener(textWatcher);
        etfive.addTextChangedListener(textWatcher);
        etsix.addTextChangedListener(textWatcher);

        etone.setOnKeyListener(onKeyListener);
        ettwo.setOnKeyListener(onKeyListener);
        etthree.setOnKeyListener(onKeyListener);
        etfour.setOnKeyListener(onKeyListener);
        etfive.setOnKeyListener(onKeyListener);
        etsix.setOnKeyListener(onKeyListener);

        converview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.addView(converview);
    }

    public void setPasswordInvisibleType(){
        etone.setTransformationMethod(passwordTransformationMethod);
        ettwo.setTransformationMethod(passwordTransformationMethod);
        etthree.setTransformationMethod(passwordTransformationMethod);
        etfour.setTransformationMethod(passwordTransformationMethod);
        etfive.setTransformationMethod(passwordTransformationMethod);
        etsix.setTransformationMethod(passwordTransformationMethod);
    }


    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().length() == 1) {
                if (etone.isFocused()) {
                    ettwo.requestFocus();
                } else if (ettwo.isFocused()) {
                    etthree.requestFocus();
                } else if (etthree.isFocused()) {
                    etfour.requestFocus();
                } else if (etfour.isFocused()) {
                    etfive.requestFocus();
                } else if (etfive.isFocused()) {
                    etsix.requestFocus();
                } else if (etsix.isFocused()) {

                }
            }
        }
    };


    private PasswordTransformationMethod passwordTransformationMethod = new PasswordTransformationMethod() {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

    };

    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (etsix.isFocused() && etsix.getText().length() == 0) {
                    etfive.requestFocus();
                    etfive.setText("");
                } else if (etfive.isFocused() && etfive.getText().length() == 0) {
                    etfour.requestFocus();
                    etfour.setText("");
                } else if (etfour.isFocused() && etfour.getText().length() == 0) {
                    etthree.requestFocus();
                    etthree.setText("");
                } else if (etthree.isFocused() && etthree.getText().length() == 0) {
                    ettwo.requestFocus();
                    ettwo.setText("");
                } else if (ettwo.isFocused() && ettwo.getText().length() == 0) {
                    etone.requestFocus();
                    etone.setText("");
                }
            }
            return false;
        }
    };

    class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source;
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public char charAt(int index) {
            return '●';
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }

    public StringBuilder getInputValue() {
        stringBuilder = null;
        stringBuilder = new StringBuilder();
        return stringBuilder.append(etone.getText().toString())
                .append(ettwo.getText().toString())
                .append(etthree.getText().toString())
                .append(etfour.getText().toString())
                .append(etfive.getText().toString())
                .append(etsix.getText().toString());
    }

}
