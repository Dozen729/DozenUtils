package com.dozen.utils;

import com.dozen.utils.base.ItemType;
import com.dozen.utils.bean.MainItemBean;
import com.dozen.utils.fragment.CroutonFragment;
import com.dozen.utils.fragment.EmptyFragment;
import com.dozen.utils.fragment.IndicatorFragment;
import com.dozen.utils.fragment.ShapeFragment;
import com.dozen.utils.fragment.SpinnerFragment;
import com.dozen.utils.fragment.SuspendFragment;
import com.dozen.utils.fragment.ViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class DozenConstant {


    public static List<MainItemBean> getMainData(){
        List<MainItemBean> list = new ArrayList<>();

        list.add(new MainItemBean(0, ItemType.MAIN_ALL,"全部","显示所有项目","",null,0,false,true));
        list.add(new MainItemBean(1, ItemType.MAIN_VIEW,"自己义View","收集常用的View","",null,0,false,false));
        list.add(new MainItemBean(2, ItemType.MAIN_UTILS,"工具","整理经常使用的方法","",null,0,false,false));
        list.add(new MainItemBean(3, ItemType.MAIN_HTTP,"http网络请求","okHttp库简单使用","",null,0,false,false));
        list.add(new MainItemBean(4, ItemType.MAIN_DIALOG,"对话框","常用对话框集合","",null,0,false,false));

        return list;
    }

    public static List<MainItemBean> getMainDetailData(){
        List<MainItemBean> list = new ArrayList<>();

        list.add(new MainItemBean(0, ItemType.MAIN_VIEW,"简单的控件","简单的自定义View使用","", ViewFragment.newInstance("简单的控件"),0,false,true));
        list.add(new MainItemBean(2, ItemType.MAIN_VIEW,"下拉框","简单的下拉框","", SpinnerFragment.newInstance("下拉框"),0,false,true));
        list.add(new MainItemBean(3, ItemType.MAIN_VIEW,"悬浮控件","悬浮在界面中的一个组件","", SuspendFragment.newInstance("悬浮控件"),0,false,true));
        list.add(new MainItemBean(4, ItemType.MAIN_VIEW,"奇形怪状","各种背景切图","", ShapeFragment.newInstance("背景切图"),0,false,true));
        list.add(new MainItemBean(5, ItemType.MAIN_VIEW,"弹框","弹框提示","", CroutonFragment.newInstance("弹框提示"),0,false,true));


        list.add(new MainItemBean(99, ItemType.MAIN_VIEW,"空的","真的是空的","", EmptyFragment.newInstance("空"),0,false,false));
        list.add(new MainItemBean(1, ItemType.MAIN_VIEW,"指示器","view","", IndicatorFragment.newInstance("指示器"),0,false,true));

        return list;
    }

}
