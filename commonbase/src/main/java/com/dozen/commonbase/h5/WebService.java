package com.dozen.commonbase.h5;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.dozen.commonbase.utils.MyLog;

/**
 * @author xiaozhongcen
 * @date 20-8-18
 * @since 1.0.0
 * 提前初始化进程减少白屏
 * 解决run日记中一直显示（E/GPUAUX: [AUX]GuiExtAuxCheckAuxPath:630: Null anb）的问题
 *
 */
public class WebService extends Service {

    private static final String TAG = WebService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.e(TAG + "init process");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
