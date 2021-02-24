package com.dozen.commonbase.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/12/15
 */
public class HorizontalScrollTextView extends androidx.appcompat.widget.AppCompatTextView {

    public HorizontalScrollTextView(@NonNull Context context) {
        super(context);
    }

    public HorizontalScrollTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {//必须重写，且返回值是true，表示始终获取焦点
        return true;
    }
}
