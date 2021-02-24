package com.dozen.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.fragmentswitcher.FragmentStateArrayPagerAdapter;
import com.dozen.commonbase.fragmentswitcher.FragmentSwitcher;
import com.dozen.commonbase.utils.ScreenUtils;
import com.dozen.commonbase.view.magicindicator.FragmentContainerHelper;
import com.dozen.commonbase.view.magicindicator.MagicIndicator;
import com.dozen.commonbase.view.magicindicator.ViewPagerHelper;
import com.dozen.commonbase.view.magicindicator.buildins.UIUtil;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.dozen.commonbase.view.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import com.dozen.utils.base.ItemType;
import com.dozen.utils.bean.MainItemBean;
import com.dozen.utils.fragment.ViewFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends CommonActivity {

    private List<MainItemBean> mainData;
    private List<MainItemBean> mainItemData;

    private ViewPager viewPager;
    private MagicIndicator magicIndicator;
    private MagicIndicator mainMagicIndicator;
    private FragmentStateArrayPagerAdapter mFragmentAdapter;

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

        mainData = DozenConstant.getMainData();
        mFragmentAdapter = new FragmentStateArrayPagerAdapter(getSupportFragmentManager());

        initMagicIndicatorMain();

        initMainItemData();
    }

    private void initMainItemData() {
        mainItemData = new ArrayList<>();

        int type = ItemType.MAIN_ALL;
        for (MainItemBean bean : mainData) {
            if (bean.isShow()) {
                type = bean.getType();
                break;
            }
        }

        if (type == ItemType.MAIN_ALL) {
            for (MainItemBean bean : DozenConstant.getMainDetailData()) {
                if (bean.isShow()) {
                    mainItemData.add(bean);
                }
            }
        } else {
            List<MainItemBean> list = new ArrayList<>();
            for (MainItemBean bean : DozenConstant.getMainDetailData()) {
                if (bean.getType() == type && bean.isShow()) {
                    list.add(bean);
                }
            }
            mainItemData.addAll(list);
        }

        initMagicIndicator();
        List<Fragment> fs = new ArrayList<>();
        for (MainItemBean bean : mainItemData) {
            fs.add(bean.getFragment());
        }

        mFragmentAdapter.clear();
        mFragmentAdapter.addAll(fs);
        mFragmentAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mFragmentAdapter);
    }

    private void initMagicIndicator() {
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
        for (MainItemBean bean : mainData) {
            bean.setShow(false);
        }
        mainData.get(index).setShow(true);
        initMainItemData();
    }
}