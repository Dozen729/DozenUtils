package com.dozen.utils.fragment.third;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.utils.R;
import com.dozen.utils.adapter.EffectAdapter;
import com.dozen.utils.bean.CommonBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class AnimationFragment extends BaseFragment {

    public static final String KEY_TEXT = "empty";

    public static AnimationFragment newInstance(String text) {
        AnimationFragment mineFragment = new AnimationFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private EffectAdapter mAdapter;
    private TextView mTarget;
    private YoYo.YoYoString rope;
    private RecyclerView recyclerView;
    private List<CommonBean> commonBeans;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_animation;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

    }

    @Override
    protected void setUpData() {
        mTarget = getContentView().findViewById(R.id.hello_world);
        recyclerView = getContentView().findViewById(R.id.list_items);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        commonBeans = new ArrayList<>();

        Techniques[] t=Techniques.values();

        for (int i = 0; i < t.length; i++) {
            CommonBean commonBean = new CommonBean();
            commonBean.setName(t[i].name());
            commonBean.setTechniques(t[i]);
            commonBeans.add(commonBean);
        }

        Collections.reverse(commonBeans);


        mAdapter = new EffectAdapter(commonBeans);

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (rope != null) {
                    rope.stop(true);
                }
                Techniques technique = commonBeans.get(position).getTechniques();
                rope = YoYo.with(technique)
                        .duration(1200)
//                        .repeat(YoYo.INFINITE)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                StyleToastUtil.info("canceled previous animation");
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .playOn(mTarget);
            }
        });


        mTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rope != null) {
                    rope.stop(true);
                }
            }
        });


    }

}
