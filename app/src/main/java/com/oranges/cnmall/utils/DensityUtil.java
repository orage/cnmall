package com.oranges.cnmall.utils;

import android.content.Context;

/**
 * px和dp转换工具类 DensityUtil
 * Created by oranges on 2016/9/17.
 */
public class DensityUtil {


    // 根据设备分辨率从dp转换成px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 根据设备分辨率从px转换成dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
