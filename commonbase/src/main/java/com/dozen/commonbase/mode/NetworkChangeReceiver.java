package com.dozen.commonbase.mode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dozen.commonbase.CommonConstant;
import com.dozen.commonbase.bean.EventBusBean;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

/**
 * @author: Dozen
 * @description:实时网络检测
 * @time: 2021/7/16
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            switch (networkInfo.getType()) {
                case TYPE_MOBILE:
//                    Toast.makeText(context, "正在使用2G/3G/4G网络", Toast.LENGTH_SHORT).show();
                    break;
                case TYPE_WIFI:
//                    Toast.makeText(context, "正在使用wifi上网", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } else {
//            Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().postSticky(new EventBusBean(CommonConstant.app_no_net));
        }
    }
}