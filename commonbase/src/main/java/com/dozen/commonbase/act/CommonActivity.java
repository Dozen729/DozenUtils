package com.dozen.commonbase.act;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.dozen.commonbase.ActivityManager;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public abstract class CommonActivity extends CommonBaseActivity {

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //添加活动到管理activity列表
        ActivityManager.getIntance().addActivity(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) == Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) {
            finish();
            return;
        }
        mContext = this;
        setContentView(setLayout());
        //兼容软键盘
//        AndroidBug5497Workaround.assistActivity(this);
        initView(savedInstanceState);
        initData();
    }

    protected abstract int setLayout();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        //将配置项设置为系统默认值
        config.setToDefaults();
        //保存更改后的配置项
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

}