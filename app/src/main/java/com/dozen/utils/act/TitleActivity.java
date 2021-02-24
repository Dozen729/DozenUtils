package com.dozen.utils.act;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.TitleView;
import com.dozen.utils.R;

@Route(path = "/view/title")
public class TitleActivity extends CommonActivity {

    private TitleView titleView;

    @Override
    protected int setLayout() {
        return R.layout.activity_title;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView = findViewById(R.id.title_view_click);
    }

    @Override
    protected void initData() {
        titleView.setTitle("设置标题");
        titleView.setRightTextBtn("设置右边");
        titleView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleToastUtil.success("点击了右边按键");
            }
        });
    }
}