package com.dozen.utils.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.CommonUtils;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class EmptyFragment extends BaseFragment {

    public static final String KEY_TEXT = "empty";

    public static EmptyFragment newInstance(String text) {
        EmptyFragment mineFragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_empty;
    }

    @Override
    protected void setUpView(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        getContentView().findViewById(R.id.empty_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isContinueTwoClick()){
                    ARouter.getInstance().build(ARouterLocation.app_shares).navigation();
                }
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    private void btnSetClick(int id){
        Button btn = getContentView().findViewById(id);
        btn.setOnClickListener(btnClickListener);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

        }
    };

}
