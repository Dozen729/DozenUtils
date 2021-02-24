package com.dozen.commonbase.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/2
 */
public class SystemUtils {

    /**
     * 获取SD路径
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }

    public static String getFile(Context context) {
        return context.getApplicationContext().getFilesDir().getAbsolutePath();
    }

    /**
     * 是否安装某个app
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isApkExist(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String name = packageInfos.get(i).packageName;
                if(packageName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * byte数组转十六进制字符串
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int data = 0xff & bytes[i];
            if(data > 0xf) {
                sb.append(Integer.toHexString(bytes[i] & 0xff));
            } else {
                sb.append("0" + Integer.toHexString(bytes[i] & 0xff));
            }
        }
        return sb.toString();
    }

    /**
     * 获得相册路径
     * @return
     */
    public static String cameraPath() {
        return getSDPath() + "/DCIM/Camera";
    }

    /**
     * 打开相册
     * @param context
     * @return
     */
    public static boolean openGallery(Context context) {
        boolean openSucceed = openApp(context, "com.android.gallery3d");
//        return openSucceed ? true : openApp(context, "com.miui.gallery"); // 小米的相册
        return openSucceed ? true : openImage(context);
    }

    /**
     * 打开应用程序
     * @param context
     * @param packageName
     * @return
     */
    public static boolean openApp(Context context, String packageName) {
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageName);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(activityPackageName, className);
            intent.setComponent(componentName);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 根据setType("image/*") 打开相册
     * @param context
     * @return
     */
    public static boolean openImage(Context context) {
        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
        intent1.setType("image/*");
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent1, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            return openApp(context, activityPackageName);
        }
        return false;
    }

    /**
     * 检测当前运行APP是否为 【packageName】
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isRunningForeground (Context context, String packageName) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName))
            return true ;
        return false ;
    }

}
