package com.dozen.utils.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dozen.commonbase.view.roundview.RoundTextView;
import com.dozen.utils.R;
import com.dozen.utils.bean.SharesBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SharesAdapter extends BaseQuickAdapter<SharesBean, BaseViewHolder> {

    public SharesAdapter(@Nullable List<SharesBean> data) {
        super(R.layout.item_shares, data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SharesBean sharesBean) {
        baseViewHolder.setText(R.id.shares_code, sharesBean.getCode() + "");
        baseViewHolder.setText(R.id.shares_name, sharesBean.getName() + "");
        baseViewHolder.setText(R.id.shares_price, sharesBean.getPrice() + "");
        RoundTextView rtvUp = baseViewHolder.getView(R.id.shares_upup);
        if (sharesBean.getUpup() > 0) {
            rtvUp.setTextColor(Color.parseColor("#FF0000"));
        } else {
            rtvUp.setTextColor(Color.parseColor("#4CAF50"));
        }
        rtvUp.setText(sharesBean.getUpup() + "%");
    }

}
