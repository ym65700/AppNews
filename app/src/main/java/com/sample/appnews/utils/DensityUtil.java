package com.sample.appnews.utils;

import android.content.Context;

/**
 * 作者：Administrator on 2017/5/31 16:19
 * 作用：单位转换工具 px和dp互相转换工具
 */

public class DensityUtil {
    /**
     * 根据手机分辨率从dip转换成px像素
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
