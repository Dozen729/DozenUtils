package com.dozen.utils.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dozen.utils.R;
import com.dozen.utils.bean.CommonBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffectAdapter extends BaseQuickAdapter<CommonBean, BaseViewHolder> {

    public EffectAdapter(@Nullable List<CommonBean> data) {
        super(R.layout.item_animation, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, CommonBean commonBean) {
        TextView name = baseViewHolder.getView(R.id.list_item_text);
        name.setText(commonBean.getName());
    }

}
