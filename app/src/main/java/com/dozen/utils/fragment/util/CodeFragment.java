package com.dozen.utils.fragment.util;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.AppInfoUtils;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.FileUtil;
import com.dozen.commonbase.utils.GsonUtils;
import com.dozen.commonbase.utils.MobileUtil;
import com.dozen.commonbase.utils.NavigationBarUtils;
import com.dozen.commonbase.utils.NetworkUtils;
import com.dozen.commonbase.utils.PackageUtils;
import com.dozen.commonbase.utils.RegexUtils;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.ScreenShotUtils;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.commonbase.utils.ShareUtils;
import com.dozen.commonbase.utils.ShellUtils;
import com.dozen.commonbase.utils.StringUtils;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.utils.SystemUtils;
import com.dozen.commonbase.utils.ThreadUtils;
import com.dozen.commonbase.utils.TimeUtil;
import com.dozen.commonbase.utils.UIUtils;
import com.dozen.commonbase.utils.ValueChangeUtils;
import com.dozen.commonbase.utils.VersionInfoUtils;
import com.dozen.utils.R;

import java.lang.reflect.Method;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class CodeFragment extends BaseFragment {

    public static final String KEY_TEXT = "code";

    public static CodeFragment newInstance(String text) {
        CodeFragment mineFragment = new CodeFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_code;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

    }

    @Override
    protected void setUpData() {

        showClassMethods(StringUtils.class,R.id.code_class1,R.id.code_tv1);
        showClassMethods(StyleToastUtil.class,R.id.code_class2,R.id.code_tv2);
        showClassMethods(SystemUtils.class,R.id.code_class3,R.id.code_tv3);
        showClassMethods(ThreadUtils.class,R.id.code_class4,R.id.code_tv4);
        showClassMethods(TimeUtil.class,R.id.code_class5,R.id.code_tv5);
        showClassMethods(UIUtils.class,R.id.code_class6,R.id.code_tv6);
        showClassMethods(ValueChangeUtils.class,R.id.code_class7,R.id.code_tv7);
        showClassMethods(VersionInfoUtils.class,R.id.code_class8,R.id.code_tv8);
        showClassMethods(ShellUtils.class,R.id.code_class9,R.id.code_tv9);
        showClassMethods(ShareUtils.class,R.id.code_class10,R.id.code_tv10);
        showClassMethods(NavigationBarUtils.class,R.id.code_class11,R.id.code_tv11);

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
