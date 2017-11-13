package com.wkp.softlinearlayout.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 首选项工具类
 *
 * @author wkp111
 */
public class SPUtils {
    /**
     * 获取首选项
     *
     * @param context
     * @return SharedPreferences sp
     */
    public static SharedPreferences getSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp;
    }

    /**
     * 获取首选项中String类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(String)
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = getSP(context);
        String string = sp.getString(key, defaultValue);
        return string;
    }

    /**
     * 获取首选项中String类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(String)
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = getSP(context);
        String string = sp.getString(key, "");
        return string;
    }

    /**
     * 获取首选项中boolean类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = getSP(context);
        boolean b = sp.getBoolean(key, false);
        return b;
    }

    /**
     * 获取首选项中boolean类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(boolean)
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = getSP(context);
        boolean b = sp.getBoolean(key, defaultValue);
        return b;
    }

    /**
     * 获取首选项中int类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(int)
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = getSP(context);
        int i = sp.getInt(key, defaultValue);
        return i;
    }

    /**
     * 获取首选项中int类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(int)
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp = getSP(context);
        int i = sp.getInt(key, 0);
        return i;
    }

    /**
     * 获取首选项中long类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(long)
     */
    public static long getLong(Context context, String key, int defaultValue) {
        SharedPreferences sp = getSP(context);
        long i = sp.getLong(key, defaultValue);
        return i;
    }

    /**
     * 获取首选项中long类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(long)
     */
    public static long getLong(Context context, String key) {
        SharedPreferences sp = getSP(context);
        long i = sp.getLong(key, 0);
        return i;
    }

    /**
     * 获取首选项中float类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(float)
     */
    public static float getFloat(Context context, String key, int defaultValue) {
        SharedPreferences sp = getSP(context);
        float i = sp.getFloat(key, defaultValue);
        return i;
    }

    /**
     * 获取首选项中float类型值，对应键值为key
     *
     * @param context
     * @param key
     * @return key对应的值(float)
     */
    public static float getFloat(Context context, String key) {
        SharedPreferences sp = getSP(context);
        float i = sp.getFloat(key, 0);
        return i;
    }

    /**
     * 向首选项中编辑数据(键值对)(值可以是String boolean int)
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = getSP(context);
        Editor editor = sp.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (float) value);
        }

        editor.commit();
    }
}
