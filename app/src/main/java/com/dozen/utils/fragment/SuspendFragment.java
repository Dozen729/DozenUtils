package com.dozen.utils.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.SuspendWidget;
import com.dozen.commonbase.view.suspend.SuspendService;
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

        btnSetClick(R.id.btn_suspend1);
        btnSetClick(R.id.btn_suspend2);
        btnSetClick(R.id.btn_suspend3);
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
            switch (view.getId()){
                case R.id.btn_suspend1:
                    openDisplay();
                    break;
                case R.id.btn_suspend2:
                    if (openDisplay()){
                        getActivity().startService(new Intent(getActivity(), SuspendService.class));
                    }
                    break;
                case R.id.btn_suspend3:
                    if (openDisplay()){
                        getActivity().stopService(new Intent(getActivity(), SuspendService.class));
                    }
                    break;
            }
        }
    };

    private boolean openDisplay(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getBaseContext())) {
                StyleToastUtil.info(getResources().getString(R.string.app_name)+"---需要开启悬浮窗权限,才可以正常运行,请务必开启!!!");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 3);
                return false;
            }
        }
        return true;
    }

}
