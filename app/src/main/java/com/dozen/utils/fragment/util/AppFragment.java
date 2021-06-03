package com.dozen.utils.fragment.util;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.AppInfoUtils;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.FileUtil;
import com.dozen.commonbase.utils.GsonUtils;
import com.dozen.commonbase.utils.MobileUtil;
import com.dozen.commonbase.utils.NetworkUtils;
import com.dozen.commonbase.utils.PackageUtils;
import com.dozen.commonbase.utils.RegexUtils;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.ScreenShotUtils;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.utils.R;

import java.lang.reflect.Method;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class AppFragment extends BaseFragment {

    public static final String KEY_TEXT = "app";

    public static AppFragment newInstance(String text) {
        AppFragment mineFragment = new AppFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_app;
    }

        @Override
    protected void setUpView(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

    }

    @Override
    protected void setUpData() {

        showClassMethods(AppInfoUtils.class,R.id.app_class1,R.id.app_tv1);
        showClassMethods(AppUtils.class,R.id.app_class2,R.id.app_tv2);
        showClassMethods(FileUtil.class,R.id.app_class3,R.id.app_tv3);
        showClassMethods(GsonUtils.class,R.id.app_class4,R.id.app_tv4);
        showClassMethods(MobileUtil.class,R.id.app_class5,R.id.app_tv5);
        showClassMethods(NetworkUtils.class,R.id.app_class6,R.id.app_tv6);
        showClassMethods(PackageUtils.class,R.id.app_class7,R.id.app_tv7);
        showClassMethods(RegexUtils.class,R.id.app_class8,R.id.app_tv8);
        showClassMethods(ScreenShotUtils.class,R.id.app_class9,R.id.app_tv9);
        showClassMethods(ScreenUtils.class,R.id.app_class10,R.id.app_tv10);
        showClassMethods(SPUtils.class,R.id.app_class11,R.id.app_tv11);

    }

    @SuppressLint("SetTextI18n")
    private void showClassMethods(Class c, int cl, int me){
        Method[]  m = c.getDeclaredMethods();
        StringBuilder sb = new StringBuilder();
        for (Method method : m) {
            sb.append(method.getName()).append("\n");
        }

        TextView tvClass = getContentView().findViewById(cl);
        TextView tvMethods = getContentView().findViewById(me);

        tvClass.setText(c.getName());
        tvMethods.setText("方法名:\n"+sb.toString());
    }

}
