package com.dozen.commonbase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.dozen.commonbase.R;

/**
 * 列表数据为空、网络错误提示
 */

public class EmptyView extends LinearLayout {

    public EmptyView(Context context) {

        super(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    private ImageView ivPrompt;
    private TextView tvPrompt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_empty_item, this);
        ivPrompt = (ImageView) findViewById(R.id.ivPrompt);
        tvPrompt = (TextView)findViewById(R.id.tvPrompt);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.emptyView);

        String tvPromptStr = a.getString(R.styleable.emptyView_text_prompt);
        a.recycle();

        tvPrompt.setText(tvPromptStr);
    }


    public void setImageBg(int imageId){

        ivPrompt.setImageResource(imageId);
    }

    public void setTvPromptText(String tvPromptStr){

        tvPrompt.setText(tvPromptStr);
    }

}
