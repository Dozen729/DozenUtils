package com.dozen.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.fragmentswitcher.FragmentStateArrayPagerAdapter;
import com.dozen.commonbase.view.magicindicator.MagicIndicator;
import com.dozen.commonbase.view.magicindicator.ViewPagerHelper;
import com.dozen.commonbase.view.magicindicator.buildins.UIUtil;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.dozen.utils.base.ItemType;
import com.dozen.utils.bean.MainItemBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CommonActivity {

    private List<MainItemBean> mainData;

    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private MagicIndicator mainMagicIndicator;

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mainMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator_main);
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    protected void initData() {

        mainData = new ArrayList<>();
        for (MainItemBean bean:DozenConstant.getMainData()){
            if (bean.isShow()){
                mainData.add(bean);
            }
        }

        viewPager.setAdapter(new FragmentStateArrayPagerAdapter(getSupportFragmentManager()));

        initMagicIndicatorMain();

        initMainItemData();
    }

    private void initMainItemData() {

        for (int i = 0; i < mainData.size(); i++) {
            MainItemBean bean = mainData.get(i);
            FragmentStateArrayPagerAdapter adapter = new FragmentStateArrayPagerAdapter(getSupportFragmentManager());
            List<MainItemBean> list = new ArrayList<>();
            List<Fragment> fragmentList = new ArrayList<>();
            if (bean.getType() == ItemType.MAIN_ALL) {
                for (MainItemBean b : DozenConstant.getMainDetailData()) {
                    if (b.isShow()) {
                        list.add(b);
                        fragmentList.add(b.getFragment());
                    }
                }
            } else {
                for (MainItemBean b : DozenConstant.getMainDetailData()) {
                    if (b.isShow() && bean.getType() == b.getType()) {
                        list.add(b);
                        fragmentList.add(b.getFragment());
                    }
                }
            }
            mainData.get(i).setItemData(list);
            adapter.addAll(fragmentList);
            mainData.get(i).setFragmentAdapter(adapter);
        }

        setMainDataShow(0);

    }

    private void initMagicIndicator(List<MainItemBean> mainItemData) {
        magicIndicator.setBackgroundColor(Color.parseColor("#8493E3"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setSkimOver(true);
        int padding = UIUtil.getScreenWidth(this) / 2;
        commonNavigator.setRightPadding(padding);
        commonNavigator.setLeftPadding(padding);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mainItemData == null ? 0 : mainItemData.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mainItemData.get(index).getName());
                clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initMagicIndicatorMain() {
        mainMagicIndicator.setBackgroundColor(Color.parseColor("#5215FD"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.15f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mainData == null ? 0 : mainData.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(mainData.get(index).getName());
                simplePagerTitleView.setNormalColor(Color.parseColor("#f2c4c4"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainMagicIndicator.onPageSelected(index);
                        mainMagicIndicator.onPageScrollStateChanged(index);
                        mainMagicIndicator.onPageScrolled(index, 0, 100);
                        setMainDataShow(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(Color.parseColor("#8493E3"));
                return indicator;
            }
        });
        mainMagicIndicator.setNavigator(commonNavigator);
    }

    private void setMainDataShow(int index) {
        initMagicIndicator(mainData.get(index).getItemData());
        viewPager.setAdapter(mainData.get(index).getFragmentAdapter());
    }

}