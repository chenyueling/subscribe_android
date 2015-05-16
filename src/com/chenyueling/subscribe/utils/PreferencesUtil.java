package com.chenyueling.subscribe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.chenyueling.subscribe.common.ConfigHelper;

/**
 *
 */
public final class PreferencesUtil {
    private static final String TAG = PreferencesUtil.class.getSimpleName();


    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(ConfigHelper.SUBSCRIBE, Context.MODE_PRIVATE);
    }
    public static boolean remove(Context context, String key) {
        if (key != null) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            return editor.remove(key).commit();
        }
        return false;
    }


    /**
     * ��preferences��ȡֵ����������ڣ��򷵻�null
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }
    /**
     * ��preferences��ȡֵ����������ڣ��򷵻�defaultVal
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key,String defaultVal) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getString(key, defaultVal);
    }


    public static boolean setString(Context context, String key, String value) {
        if (value != null) {
            SharedPreferences preferences = getPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            return editor.putString(key, value).commit();
        }
        return false;
    }

    /**
     * ��preferences��ȡֵ����������ڣ��򷵻�false
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * ��preferences��ȡֵ����������ڣ��򷵻�defaultVal
     * @param context
     * @param key
     * @param defaultVal
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defaultVal){
        SharedPreferences preferences = getPreferences(context);
        return preferences.getBoolean(key, defaultVal);
    }

    public static boolean setBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.putBoolean(key, value).commit();
    }

    /**
     * ��Preferences�л�ȡһ������ֵ�����û��ȡ��ֵ�򷵻�0
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }
    /**
     * ��Preferences�л�ȡһ������ֵ�����û��ȡ��ֵ�򷵻�defaultValue
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key,int defaultValue) {
        SharedPreferences preferences = getPreferences(context);
        return preferences.getInt(key, defaultValue);
    }

    public static boolean setInt(Context context, String key, int value) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.putInt(key, value).commit();
    }

    public static boolean clear(Context context) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return editor.clear().commit();
    }

    private PreferencesUtil() {}
}
