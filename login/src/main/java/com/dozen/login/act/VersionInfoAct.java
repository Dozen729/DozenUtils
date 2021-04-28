package com.dozen.login.act;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.act.CommonActivity;
import com.dozen.commonbase.utils.AppUtils;
import com.dozen.commonbase.utils.CommonUtils;
import com.dozen.commonbase.utils.MyLog;
import com.dozen.commonbase.utils.SPUtils;
import com.dozen.commonbase.utils.VersionInfoUtils;
import com.dozen.commonbase.utils.id.GetDeviceId;
import com.dozen.login.LoginConstant;
import com.dozen.login.R;

import java.util.Arrays;

public class VersionInfoAct extends CommonActivity {

    private TextView tvVersion;
    private TextView tvVersionMode;
    private TextView versionInfo;

    @Override
    protected int setLayout() {
        return R.layout.activity_version_info_dozen;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvVersion=findViewById(R.id.avi_tv_version_info);
        tvVersionMode=findViewById(R.id.tv_version_mode);
        versionInfo=findViewById(R.id.show_version_info);
        versionInfo.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        tvVersion.setText(VersionInfoUtils.getVersionName(this));
        if (AppUtils.isApkInDebug(this)){
            tvVersionMode.setText("debug");
        }else {
            tvVersionMode.setText("release");
        }

        final GetDeviceId getDeviceId = new GetDeviceId(this);
        final String oaid_id = SPUtils.getString(this, CommonConstant.OAID,"");
        MyLog.d("imei="+getDeviceId.getIMEI()+":::androidid="+getDeviceId.getAndroidId()+":::oaid="+SPUtils.getString(this,CommonConstant.OAID,""));

        tvVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isContinueFiveClick()){
                    StringBuilder sb = new StringBuilder();
                    sb.append("uuid:").append(LoginConstant.GET_UUID()).append("\n");
                    sb.append("build_time:").append(VersionInfoUtils.getAppBuildTime()).append("\n");
                    sb.append("channel:").append(LoginConstant.channel).append("\n");
                    sb.append("mine:").append(CommonConstant.mine_click_switch).append("\n");
                    sb.append("umeng:").append(CommonConstant.umeng_pay).append("\n");
                    sb.append("reyun:").append(CommonConstant.reyun_click_switch).append("\n");
                    sb.append("version_code:").append(VersionInfoUtils.getVersionCode(getBaseContext())).append("\n");
                    sb.append("version_name:").append(VersionInfoUtils.getVersionName(getBaseContext())).append("\n");
                    sb.append("device:").append(AppUtils.getPhoneBrand()).append("\n");
                    sb.append("android_version:").append(android.os.Build.VERSION.RELEASE).append("\n");
                    sb.append("wlan_mac:").append(getDeviceId.getWLANMAC()).append("\n");
                    sb.append("imei:").append(getDeviceId.getIMEI()).append("\n");
                    sb.append("android_id:").append(getDeviceId.getAndroidId()).append("\n");
                    sb.append("oaid:").append(oaid_id).append("\n");
                    sb.append("umeng_device_info:").append(Arrays.toString(AppUtils.getUMDeviceInfo(getBaseContext()))).append("\n");
                    versionInfo.setVisibility(View.VISIBLE);
                    versionInfo.setText(sb.toString());
                }
            }
        });
        versionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isContinueTwoClick()){
                    AppUtils.copyContent(versionInfo.getText().toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}