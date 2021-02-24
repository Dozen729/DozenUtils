package com.dozen.commonbase.view.vrtv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dozen.commonbase.R;

import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/24
 */
public class DataSetAdapter extends BaseQuickAdapter<TVBean, BaseViewHolder> {

    public DataSetAdapter(@Nullable List<TVBean> data) {
        super(R.layout.item_data_set,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, TVBean tvBean) {

    }
}
