package com.dozen.utils.fragment;

import android.os.Bundle;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.view.TitleView;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class IndicatorFragment extends BaseFragment {

    public static final String KEY_TEXT = "indicator";

    public static IndicatorFragment newInstance(String text) {
        IndicatorFragment mineFragment = new IndicatorFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_indicator;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

    }

    @Override
    protected void setUpData() {

    }
}
