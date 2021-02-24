package com.dozen.commonbase.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/10/23
 */
public class NavigationBarUtils {
    /**
     *
     * @param context
     * @return 返回true表示显示虚拟导航键、false表示隐藏虚拟导航键
     */
    public static boolean hasNavigationBar(Context context) {
        //navigationGestureEnabled()从设置中取不到值的话，返回false，因此也不会影响在其他手机上的判断
        return deviceHasNavigationBar() && !navigationGestureEnabled(context);
    }
    /**
     * 获取主流手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     * @param context app Context
     * @return
     * false 表示使用的是虚拟导航键(NavigationBar)，
     * true 表示使用的是手势， 默认是false
     */
    private static boolean navigationGestureEnabled(Context context) {
        int val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
        return val != 0;
    }
    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
     *
     * @return
     */
    private static String getDeviceInfo() {
        String brand = Build.BRAND;
        if(TextUtils.isEmpty(brand)) return "navigationbar_is_min";

        if (brand.equalsIgnoreCase("HUAWEI")||"HONOR".equals(brand)) {
            return "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            return "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            return "navigation_gesture_on";
        } else if(brand.equalsIgnoreCase("samsung")){
            return "navigationbar_hide_bar_enabled";
        }else {
            return "navigationbar_is_min";
        }
    }

    /**
     * 判断设备是否存在NavigationBar
     *
     * @return true 存在, false 不存在
     */
    public static boolean deviceHasNavigationBar() {
        boolean haveNav = false;
        try {
            //1.通过WindowManagerGlobal获取windowManagerService
            // 反射方法：IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
            Class<?> windowManagerGlobalClass = Class.forName("android.view.WindowManagerGlobal");
            Method getWmServiceMethod = windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService");
            getWmServiceMethod.setAccessible(true);
            //getWindowManagerService是静态方法，所以invoke null
            Object iWindowManager = getWmServiceMethod.invoke(null);

            //2.获取windowMangerService的hasNavigationBar方法返回值
            // 反射方法：haveNav = windowManagerService.hasNavigationBar();
            Class<?> iWindowManagerClass = iWindowManager.getClass();
            Method hasNavBarMethod = iWindowManagerClass.getDeclaredMethod("hasNavigationBar");
            hasNavBarMethod.setAccessible(true);
            haveNav = (Boolean) hasNavBarMethod.invoke(iWindowManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return haveNav;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (deviceHasNavigationBar()) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }
}
