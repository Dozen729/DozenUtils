package com.dozen.utils.fragment.http;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.http.CallBack;
import com.dozen.commonbase.http.ResultInfo;
import com.dozen.commonbase.utils.CommonUtils;
import com.dozen.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class HttpFragment extends BaseFragment {

    public static final String KEY_TEXT = "http";

    public static HttpFragment newInstance(String text) {
        HttpFragment mineFragment = new HttpFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private TextView tv1;
    private RecyclerView httpRV1;
    private RecyclerView httpRV2;

    private int number = 1;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_http;
    }

        @Override
    protected void setUpView(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name = bundle.getString(KEY_TEXT);

        tv1 = getContentView().findViewById(R.id.http_zoom1);
        httpRV1 = getContentView().findViewById(R.id.http_rv1);
        httpRV2 = getContentView().findViewById(R.id.http_rv2);
    }

    @Override
    protected void setUpData() {

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonUtils.isContinueFiveClick()) {
                    loadAdapter2();
                    number += number;
                } else if (CommonUtils.isContinueTwoClick()) {
                    loadAdapter1();
                    number += number;
                }

            }
        });

    }

    private void loadAdapter1() {

        PullHttpUtil.pullPicture(number + "", new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                HttpPictureResult httpPictureResult = (HttpPictureResult) info;

                List<PictureBean> pictureBeanList = new ArrayList<>();

                for (int i = 0; i < httpPictureResult.data.length; i++) {
                    PictureBean pictureBean = new PictureBean();
                    pictureBean.setName(httpPictureResult.data[i].desc);
                    pictureBean.setPicture(httpPictureResult.data[i].url);
                    pictureBeanList.add(pictureBean);
                }

                HttpPictureAdapter adapter = new HttpPictureAdapter(getBaseContext(), pictureBeanList);
                httpRV1.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                httpRV1.setAdapter(adapter);

            }
        }, "picture");

    }

    private void loadAdapter2() {

        PullHttpUtil.pullPicture(number + "", new CallBack() {
            @Override
            public void onRequested(ResultInfo info, Object tag) {
                HttpPictureResult httpPictureResult = (HttpPictureResult) info;

                List<PictureBean> pictureBeanList = new ArrayList<>();

                for (int i = 0; i < httpPictureResult.data.length; i++) {
                    PictureBean pictureBean = new PictureBean();
                    pictureBean.setName(httpPictureResult.data[i].desc);
                    pictureBean.setPicture(httpPictureResult.data[i].url);
                    pictureBeanList.add(pictureBean);
                }

                HttpPictureAdapter adapter = new HttpPictureAdapter(getBaseContext(), pictureBeanList);
                httpRV2.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                httpRV2.setAdapter(adapter);

            }
        }, "picture");

    }

}
