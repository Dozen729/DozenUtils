package com.dozen.utils.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.SuspendWidget;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class SuspendFragment extends BaseFragment {

    public static final String KEY_TEXT = "suspend";

    public static SuspendFragment newInstance(String text) {
        SuspendFragment mineFragment = new SuspendFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private SuspendWidget suspendWidget;
    private LinearLayout boxLocation;
    private boolean isRun = false;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_suspend;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        boxLocation = getContentView().findViewById(R.id.ll_box_location);
        boxLocation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isRun) {
                    isRun = true;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.BOTTOM;
                    boxLocation.setLayoutParams(params);
                    boxLocation.setPadding(10, 200, 50, 10);
                }
            }
        });
    }

    @Override
    protected void setUpData() {
        initSuspendWidget();
    }

    private void initSuspendWidget() {
        suspendWidget = new SuspendWidget(getBaseContext(),getContentView());
        suspendWidget.setMainFrame(R.id.ll_main_frame);
        suspendWidget.setWidgetFrame(R.id.ll_widget_frame);
        suspendWidget.setScrollFrame(R.id.iv_scroll_frame);
        TextView tv = getContentView().findViewById(R.id.tv_box_tip);
        tv.setText("滑动滑动");
        suspendWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleToastUtil.success("oh!oh");
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
