package com.dozen.commonbase.view.autopoll;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dozen.commonbase.R;
import com.dozen.commonbase.bean.DozenBean;

import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/29
 */
public class AutoPollAdapter extends RecyclerView.Adapter<AutoPollAdapter.BaseViewHolder> {
    private final List<DozenBean> mData;

    public AutoPollAdapter(List<DozenBean> list) {
        this.mData = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_auto_pull, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        DozenBean data = mData.get(position % mData.size());

        SpannableStringBuilder ssb = new SpannableStringBuilder(data.getName());
        SpannableStringBuilder ssb2 = new SpannableStringBuilder("查了");
        SpannableStringBuilder ssb3 = new SpannableStringBuilder(data.getDetail()+",找到了  ");
        SpannableStringBuilder ssb4 = new SpannableStringBuilder(data.getPicture());
        SpannableStringBuilder ssb5 = new SpannableStringBuilder("条 结果");

        ForegroundColorSpan fcs1 = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
        ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.parseColor("#20FD93"));
        ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.parseColor("#20FD93"));

        ssb.setSpan(fcs1, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb2.setSpan(fcs2, 0, ssb2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb3.setSpan(fcs1, 0, ssb3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb4.setSpan(fcs4, 0, ssb4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb5.setSpan(fcs1, 0, ssb5.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ssb2).append(ssb3).append(ssb4).append(ssb5);

        holder.tv.setText(ssb);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public BaseViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.auto_pull_name);
        }
    }
}