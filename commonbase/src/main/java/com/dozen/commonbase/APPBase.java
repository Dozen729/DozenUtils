package com.dozen.commonbase;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author: Dozen
 * @description:保存 static Application, 以便其他地方调用
 * @time: 2020/11/2
 */
public class APPBase {

    private static Application application;
    private static APPBase app;
    //Debug模式
    private boolean isDebug;

    private APPBase(Application application) {
        APPBase.application = application;
        isDebug = isDebug(application);
    }

    /**
     * 保存 static Application, 以便其他地方调用，同时初始化xutils
     */
    public static void init(Application application) {
        app = new APPBase(application);
    }

    /**
     * 判断是否为Debug模式。
     * 注意：该方法使用的的Context是全局上下文。
     * 如果您使用的Application没有继承全局上下文，请使用isDebug(Context context)
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 判断是否为Debug模式
     * @return
     */
    public boolean isDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
        } catch (Exception e) {
        }
        return false;
    }

    public static APPBase getInstance() {
        if (application == null) {
            throw new RuntimeException("请在您的Application onCreate() 中调用 APPBase.init(...)");
        }
        return app;
    }

    public static Application getApplication() {
        if (application == null) {
            throw new RuntimeException("请在您的Application onCreate() 中调用 APPBase.init(...)");
        }
        return application;
    }

    //token提示框
    Toast toast;
    public void showToast(String text) {
        if(TextUtils.isEmpty(text)) return;
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(application, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}

