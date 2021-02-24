package com.dozen.commonbase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/2
 */
public class SPUtils {
    public final static String SP_NAME = "sp_dozen_cache";
    private static SharedPreferences mPreferences;        // SharedPreferences的实例

    private static SharedPreferences getSp(Context context) {
        if (mPreferences == null) {
            mPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mPreferences;
    }

    /**
     * 通过SP获得boolean类型的数据，没有默认为false
     *
     * @param context : 上下文
     * @param key     : 存储的key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, false);
    }

    /**
     * 通过SP获得boolean类型的数据，没有默认为false
     *
     * @param context  : 上下文
     * @param key      : 存储的key
     * @param defValue : 默认值
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 设置int的缓存数据
     *
     * @param context
     * @param key     :缓存对应的key
     * @param value   :缓存对应的值
     */
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getInt(key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getString(key, defValue);
    }

    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getLong(key, 0);
    }

    /**
     * 设置int的缓存数据
     *
     * @param context
     * @param key     :缓存对应的key
     * @param value   :缓存对应的值
     */
    public static void setInt(Context context, String key, int value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        edit.putInt(key, value);
        edit.commit();
    }


    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        edit.putString(key, value);
        edit.commit();
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        edit.putLong(key, value);
        edit.commit();
    }


    public static void setInt(Context context, String key, String value) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        edit.putString(key, value);
        edit.commit();
    }


    /**
     * 删除指定key的value
     *
     * @param context
     * @param key
     */
    public static void deleteKeyData(Context context, String key) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        edit.remove(key);
        edit.commit();
    }

    /**
     * 删除全部值
     *
     * @param context
     */
    public static void deleteData(Context context) {
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        edit.clear().commit();
    }


    /**
     * 保存List
     *
     * @param context
     * @param key
     * @param datalist
     * @param <T>
     */
    public static <T> void setDataList(Context context, String key, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            return;
        }
        SharedPreferences sp = getSp(context);
        SharedPreferences.Editor edit = sp.edit();// 获取编辑器
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        edit.putString(key, strJson);
        edit.commit();
    }

    /**
     * 获取List
     *
     * @param key
     * @return
     */
    public static <T> List<T> getDataList(Context context, String key) {
        List<T> datalist = new ArrayList<T>();
        SharedPreferences sp = getSp(context);
        String strJson = sp.getString(key, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;
    }


    /**
     * 保存一个对象，object必须是普通类，而不是泛型
     *
     * @param context
     * @param object
     */
    public static void putObject(Context context, Object object) {
        String key = getKey(object.getClass());
        Gson gson = new Gson();
        String json = gson.toJson(object);
        setString(context, key, json);
    }

    public static String getKey(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 获取对象
     *
     * @param context
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getObject(Context context, Class<T> clazz) {
        String key = getKey(clazz);
        String json = getString(context, key, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

}
