package com.dozen.utils.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.spinner.NiceSpinner;
import com.dozen.commonbase.view.spinner.OnSpinnerItemSelectedListener;
import com.dozen.commonbase.view.spinner.SpinnerTextFormatter;
import com.dozen.utils.DozenConstant;
import com.dozen.utils.R;
import com.dozen.utils.bean.MainItemBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class SpinnerFragment extends BaseFragment {

    public static final String KEY_TEXT = "spinner";

    public static SpinnerFragment newInstance(String text) {
        SpinnerFragment mineFragment = new SpinnerFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private static final String[] CHANNELS = new String[]{"CircleImageView", "ClearEditText", "HeadView", "NumberProgressBar", "HorizontalScrollTextView"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    private NiceSpinner niceSpinner1;
    private NiceSpinner niceSpinner2;
    private NiceSpinner niceSpinner3;
    private NiceSpinner niceSpinner4;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_spinner;
    }

        @Override
    protected void setUpView(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        niceSpinner1 = getContentView().findViewById(R.id.nice_spinner1);
        niceSpinner2 = getContentView().findViewById(R.id.nice_spinner2);
        niceSpinner3 = getContentView().findViewById(R.id.nice_spinner3);
        niceSpinner4 = getContentView().findViewById(R.id.nice_spinner4);
    }

    @Override
    protected void setUpData() {
        initNiceSpinner1();
        initNiceSpinner2();
        initNiceSpinner3();
        initNiceSpinner4();
    }

    private void initNiceSpinner4() {
        List<MainItemBean> list = DozenConstant.getMainDetailData();

        SpinnerTextFormatter<MainItemBean> textFormatter = new SpinnerTextFormatter<MainItemBean>() {
            @Override
            public Spannable format(MainItemBean bean) {
                return new SpannableString(bean.getName());
            }
        };

        niceSpinner4.setSpinnerTextFormatter(textFormatter);
        niceSpinner4.setSelectedTextFormatter(textFormatter);
        niceSpinner4.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                StyleToastUtil.info(list.get(position).getTip());
            }
        });
        niceSpinner4.attachDataSource(list);
    }

    private void initNiceSpinner3() {
        List<MainItemBean> list = DozenConstant.getMainData();

        SpinnerTextFormatter<MainItemBean> textFormatter = new SpinnerTextFormatter<MainItemBean>() {
            @Override
            public Spannable format(MainItemBean bean) {
                return new SpannableString(bean.getName());
            }
        };

        niceSpinner3.setSpinnerTextFormatter(textFormatter);
        niceSpinner3.setSelectedTextFormatter(textFormatter);
        niceSpinner3.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                StyleToastUtil.info(list.get(position).getTip());
            }
        });
        niceSpinner3.attachDataSource(list);
    }

    private void initNiceSpinner2() {
        List<MainItemBean> list = DozenConstant.getMainData();

        SpinnerTextFormatter<MainItemBean> textFormatter = new SpinnerTextFormatter<MainItemBean>() {
            @Override
            public Spannable format(MainItemBean bean) {
                return new SpannableString(bean.getName());
            }
        };

        niceSpinner2.setSpinnerTextFormatter(textFormatter);
        niceSpinner2.setSelectedTextFormatter(textFormatter);
        niceSpinner2.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                StyleToastUtil.info(list.get(position).getTip());
            }
        });
        niceSpinner2.attachDataSource(list);
    }

    private void initNiceSpinner1() {

        niceSpinner1.attachDataSource(mDataList);
        niceSpinner1.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String name=mDataList.get(position);
                StyleToastUtil.info(name);
            }
        });

    }

    private void btnSetClick(int id){
        Button btn = getContentView().findViewById(id);
        btn.setOnClickListener(btnClickListener);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

        }
    };

}
