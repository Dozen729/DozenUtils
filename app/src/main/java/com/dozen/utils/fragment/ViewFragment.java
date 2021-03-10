package com.dozen.utils.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.colorpicker.ColorPicker;
import com.dozen.commonbase.view.menu.SlidingMenu;
import com.dozen.commonbase.view.vrtv.DataSetAdapter;
import com.dozen.commonbase.view.vrtv.TVBean;
import com.dozen.commonbase.view.vrtv.VerticalRollingTextView;
import com.dozen.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class ViewFragment extends BaseFragment {

    public static final String KEY_TEXT = "view";

    public static ViewFragment newInstance(String text) {
        ViewFragment mineFragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private VerticalRollingTextView verticalRollingTextView;
    private SlidingMenu slidingMenu;
    private Button btnColor;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_view;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        btnSetClick(R.id.btn_show_title);
        btnSetClick(R.id.btn_show_menu);
        btnSetClick(R.id.btn_show_color);
        btnColor = getContentView().findViewById(R.id.btn_show_color);

        verticalRollingTextView = getContentView().findViewById(R.id.vertical_rolling_tv);
    }

    @Override
    protected void setUpData() {
        initVerticalRolling();
        initSlidingMenu();
    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(getBaseContext());
        slidingMenu.setMode(0);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(null);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35F);
        slidingMenu.setTouchModeAbove(0);
        slidingMenu.setBehindScrollScale(0.0F);
        slidingMenu.attachToActivity(getActivity(), 1);
        slidingMenu.setMenu(R.layout.fragment_menu);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, EmptyFragment.newInstance("空")).commit();
    }

    private void initVerticalRolling() {
        List<TVBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new TVBean(i,"等一下，这是一个上下滚动的View",""));
        }
        DataSetAdapter dataSetAdapter = new DataSetAdapter(list);
        verticalRollingTextView.setDataSetAdapter(dataSetAdapter);
        verticalRollingTextView.run();
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
                case R.id.btn_show_title:
                    ARouter.getInstance().build("/view/title").navigation();
                    break;
                case R.id.btn_show_menu:
                    slidingMenu.toggle();
                    break;
                case R.id.btn_show_color:
                    colorPickerShow();
                    break;
            }
        }
    };

    private void colorPickerShow(){
        ColorPicker colorPicker = new ColorPicker(getActivity());
        colorPicker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
                btnColor.setBackgroundColor(pickedColor);
            }

            @Override
            public void onColorString(String pColor) {
                StyleToastUtil.success("color:"+ pColor);
            }
        });
        colorPicker.show();
    }

}
