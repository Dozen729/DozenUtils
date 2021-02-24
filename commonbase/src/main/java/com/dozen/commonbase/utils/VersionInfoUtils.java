package com.dozen.commonbase.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.dozen.commonbase.BuildConfig;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/2
 */
public class VersionInfoUtils {

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取版本号(内部识别号)
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 方法名：  signatureMD5	<br>
     * 方法描述：获得app md5<br>
     * 修改备注：<br>
     * 创建时间： 2016-1-5下午2:40:47<br>
     *
     * @param context
     * @return
     */
    public static String signatureMD5(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            return signatureMD5(signs);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法名：  signatureMD5	<br>
     * 方法描述：通过Signature[]获得app md5<br>
     *
     * @param signatures
     * @return
     */
    public static String signatureMD5(Signature[] signatures) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            if (signatures != null) {
                for (Signature s : signatures)
                    digest.update(s.toByteArray());
            }
            return SystemUtils.toHexString(digest.digest());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取版本升级的文件
     *
     * @return
     */
    public static String getVersionFilePath() {
        File appFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/commonbase");
        if (!appFile.exists()) {
            appFile.mkdirs();
        }
        return appFile.getAbsolutePath() + "/commonBase_update.apk";
    }

    /**
     * 获取指定路径apk的版本号
     *
     * @param mContext
     * @return
     */
    public static int getApkFileVersionCode(Context mContext) {
        String filePath = getVersionFilePath();
        if (!new File(filePath).exists())
            return -1;
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (packageInfo == null)
            return -1;
        return packageInfo.versionCode;
    }

    /**
     * 安装apk
     *
     * @param mContext
     */
    public static void installApk(Context mContext) {
        MyLog.d("安装apk");
        File apkfile = new File(getVersionFilePath());
        if (!apkfile.exists()) {
            MyLog.d("apkfile不存在");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", apkfile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } else {
            MyLog.d("Build.VERSION.SDK_INT < Build.VERSION_CODES.N");
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }

    /**
     * @return
     * @retur boolean
     * @Description: 判断是否需要更新
     */
    public static boolean isUpgrade(Context context, int newVersionCode) {
        int oldVersionCode = getVersionCode(context);
        return newVersionCode > oldVersionCode;
    }

    /**
     *
     * 需要在build.gradle中添加如下代码
     *defaultConfig {
     *      buildConfigField "long", "BUILD_TIMESTAMP", System.currentTimeMillis() + "L"
     * }
     *
     * @return 返回编译时间
     */
    public static String getAppBuildTime() {
        String result = "";
        try {
            long time = BuildConfig.BUILD_TIMESTAMP;
            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
            formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
            result = formatter.format(new java.util.Date(time));
        } catch (Exception e) {
            MyLog.e(e.toString());
        }

        return result;
    }

}
