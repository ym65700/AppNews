package com.sample.appnews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import org.xutils.common.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 作者：Administrator on 2017/5/31 14:33
 * 作用：缓存软件的一些参数和数据
 */

public class CacheUtils {

    /**
     * 得到缓存值
     *
     * @param context 上下文
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("xinwen", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 保存软件参数
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences("xinwen", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取缓存的文本信息
     *
     * @param context
     * @param key
     * @return
     */

    public static String getString(Context context, String key) {
        String result = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(key);
                ///mnt/sdcard/xinwen/files/llkskljskljklsjklsllsl
                File file = new File(Environment.getExternalStorageDirectory() + "/xinwen/files", fileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) != -1) {
                        stream.write(buffer, 0, length);
                    }
                    fis.close();
                    stream.close();
                    result = stream.toString();

                }

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本缓存获取失败");
            }

        } else {
            SharedPreferences sp = context.getSharedPreferences("xinwen", Context.MODE_PRIVATE);
            result = sp.getString(key, "");
        }
        return result;

    }

    /**
     * 缓存的文本信息
     *
     * @param context
     * @param key
     * @return
     */
    public static void putString(Context context, String key, String value) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(key);
                ///mnt/sdcard/xinwen/files/llkskljskljklsjklsllsl
                File file = new File(Environment.getExternalStorageDirectory() + "/xinwen/files", fileName);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                //保存文本数据
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(value.getBytes());
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本缓存失败");
            }

        } else {
            SharedPreferences sp = context.getSharedPreferences("xinwen", Context.MODE_PRIVATE);
            sp.edit().putString(key, value).commit();
        }


    }
}
