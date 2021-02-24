package com.dozen.thirdparty.ad.tuia;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.NetworkUtils;
import com.dozen.commonbase.utils.StyleToastUtil;
import com.dozen.commonbase.view.TitleView;
import com.dozen.thirdparty.R;
import com.dozen.thirdparty.ad.TPConstant;
import com.lechuan.midunovel.nativead.Ad;
import com.lechuan.midunovel.nativead.AdCallBack;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/12/1
 */
public class TuiAADAct extends CommonActivity {

    private Ad ad;
    private String slotId;
    private String titleName;
    private FrameLayout flShow;
    private TitleView titleView;
    private String taskID;

    @Override
    protected int setLayout() {
        return R.layout.activity_tui_a_ad;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        flShow=findViewById(R.id.fl_tui_a_ad_show);
        titleView=findViewById(R.id.titleView);

        slotId = getIntent().getStringExtra(TPConstant.TA_SLOT_ID);
        titleName = getIntent().getStringExtra(TPConstant.TA_TITLE_NAME);

        titleView.setTitle(titleName);

        if (!NetworkUtils.checkWifiAndGPRS(this)) {
            StyleToastUtil.error("网络中断");
            finish();
        } else {
            startAd();
        }
    }

    private void startAd() {

        String deviceId = CommonConstant.OAID;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            GetDeviceId getDeviceId = new GetDeviceId(this);
//            deviceId = getDeviceId.getIMEI();
//        } else {
//            deviceId = AppConstant.OAID;
//        }
        MyLog.d("deviceId:"+deviceId);
        ad = new Ad(TPConstant.TA_APP_KEY, slotId, deviceId, deviceId);
        ad.setRewadShow(true);
        ad.setLandPageShow(true);
        ad.init(this, flShow, Ad.AD_URL_NEW, new AdCallBack() {
            @Override
            public void onReceiveAd() {
                MyLog.d("onReceiveAd");
            }

            @Override
            public void onFailedToReceiveAd() {
                MyLog.d("onFailedToReceiveAd");
            }

            @Override
            public void onActivityClose() {
                MyLog.d("onActivityClose");
            }

            @Override
            public void onActivityShow() {
                MyLog.d("onActivityShow");
            }

            @Override
            public void onRewardClose() {
                MyLog.d("onRewardClose");
            }

            @Override
            public void onRewardShow() {
                MyLog.d("onRewardShow");
            }

            @Override
            public void onPrizeClose() {
                MyLog.d("onPrizeClose");
            }

            @Override
            public void onPrizeShow() {
                MyLog.d("onPrizeShow");
            }
        });
        if (ad != null) {
            ad.loadAd(TuiAADAct.this, false);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ad!=null){
            ad.destroy();
        }
    }
}
