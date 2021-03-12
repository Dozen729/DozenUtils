package com.dozen.utils.fragment.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dozen.utils.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/12
 */
public class HttpPictureAdapter extends BaseQuickAdapter<PictureBean, BaseViewHolder> {

    private Context context;

    public HttpPictureAdapter(Context context,@Nullable List<PictureBean> data) {
        super(R.layout.view_http_picture, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PictureBean pictureBean) {
        baseViewHolder.setText(R.id.http_picture_name,pictureBean.getName());
        Glide.with(context).asBitmap().load(pictureBean.getPicture()).into((ImageView) baseViewHolder.getView(R.id.http_picture_url));
    }
}
