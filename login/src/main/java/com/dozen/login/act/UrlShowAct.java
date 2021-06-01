package com.dozen.login.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dozen.commonbase.h5.AgentWebFragment;
import com.dozen.commonbase.h5.FragmentKeyDown;
import com.dozen.commonbase.router.ARouterLocation;
import com.dozen.commonbase.utils.EmptyCheckUtil;
import com.dozen.login.R;

@Route(path = ARouterLocation.app_url_show)
public class UrlShowAct extends AppCompatActivity {

    @Autowired
    String url_show = "";

    private AgentWebFragment mAgentWebFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_show);
        ARouter.getInstance().inject(this);

        if (EmptyCheckUtil.isEmpty(url_show)){
            return;
        }

        openH5Detail(url_show);
    }

    private void openH5Detail(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle mBundle = null;
        ft.replace(R.id.url_show_frame, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
        mBundle.putString(AgentWebFragment.URL_KEY, url);
        ft.commit();

        mAgentWebFragment.setMenuState(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}