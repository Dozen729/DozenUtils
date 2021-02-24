package com.dozen.commonbase.view.sixinput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dozen.commonbase.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/26
 */
@SuppressLint("AppCompatCustomView")
public class LastInputEditText extends EditText {
    public LastInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LastInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastInputEditText(Context context) {
        super(context);
    }

    public void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);//保证光标始终在最后面
        if (selStart == selEnd) {//防止不能多选
            setSelection(getText().length());
        }
        setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });//设置只能输入一个字符
        setInputType(InputType.TYPE_CLASS_NUMBER);//设置只能输入阿拉伯数字
        setGravity(Gravity.CENTER);//设置输入字符居中
        setBackgroundColor(Color.parseColor("#FFF2F3F4")); //设置背景
        setTextColor(Color.parseColor("#FF606AED"));
    }
}