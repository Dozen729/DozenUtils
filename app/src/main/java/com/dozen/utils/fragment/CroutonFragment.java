package com.dozen.utils.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.view.popup.Configuration;
import com.dozen.commonbase.view.popup.Crouton;
import com.dozen.commonbase.view.popup.Style;
import com.dozen.utils.R;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class CroutonFragment extends BaseFragment {

    public static final String KEY_TEXT = "empty";

    public static CroutonFragment newInstance(String text) {
        CroutonFragment mineFragment = new CroutonFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_crouton;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        btnSetClick(R.id.btn_crouton1);
        btnSetClick(R.id.btn_crouton2);
        btnSetClick(R.id.btn_crouton3);
        btnSetClick(R.id.btn_crouton4);
        btnSetClick(R.id.btn_crouton5);
        btnSetClick(R.id.btn_crouton6);
        btnSetClick(R.id.btn_crouton7);
        btnSetClick(R.id.btn_crouton8);
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
            switch (view.getId()){
                case R.id.btn_crouton1:
                    showCustomViewCrouton1();
                    break;
                case R.id.btn_crouton2:
                    showCustomViewCrouton2();
                    break;
                case R.id.btn_crouton3:
                    showCustomViewCrouton3();
                    break;
                case R.id.btn_crouton4:
                    showCustomViewCrouton4();
                    break;
                case R.id.btn_crouton5:
                    showCustomViewCrouton5();
                    break;
                case R.id.btn_crouton6:
                    showCustomViewCrouton6();
                    break;
                case R.id.btn_crouton7:
                    showCustomViewCrouton7();
                    break;
                case R.id.btn_crouton8:
                    showCustomViewCrouton8();
                    break;
            }
        }
    };

    private void showCustomViewCrouton1() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        Crouton crouton;
        crouton = Crouton.make(getActivity(), view);
        crouton.show();
    }

    private void showCustomViewCrouton2() {
        final Crouton crouton;
        View view = getActivity().getLayoutInflater().inflate(R.layout.crouton_custom_view, null);
        crouton = Crouton.make(getActivity(), view,R.id.alternate_view_group);
        crouton.show();
    }

    private void showCustomViewCrouton3() {
        final Crouton crouton;
        crouton = Crouton.makeText(getActivity(), "croutonText", Style.INFO);
        crouton.show();
    }

    private void showCustomViewCrouton4() {
        final Crouton crouton;
        crouton = Crouton.makeText(getActivity(), "croutonText", Style.ALERT);
        Configuration croutonConfiguration = new Configuration.Builder().setDuration(1000).build();
        crouton.setConfiguration(croutonConfiguration).show();
    }

    private void showCustomViewCrouton5() {
        final Crouton crouton;
        crouton = Crouton.makeText(getActivity(), "croutonText", Style.CONFIRM);
        Configuration croutonConfiguration = new Configuration.Builder().setDuration(1000).build();
        crouton.setConfiguration(croutonConfiguration).show();
    }

    private void showCustomViewCrouton6() {
        final Crouton crouton;
        Style croutonStyle = new Style.Builder().build();
        crouton = Crouton.makeText(getActivity(), "croutonText", croutonStyle);
        Configuration croutonConfiguration = new Configuration.Builder().setDuration(1000).build();
        crouton.setConfiguration(croutonConfiguration).show();
    }

    private void showCustomViewCrouton7() {
        Crouton.showText(getActivity(),"hello",Style.INFO);
    }

    private void showCustomViewCrouton8() {
        Style croutonStyle = new Style.Builder().build();
        Configuration croutonConfiguration = new Configuration.Builder().setDuration(1000).build();
        Crouton.showText(getActivity(),"hello",croutonStyle,R.id.alternate_view_group,croutonConfiguration);
    }
}
