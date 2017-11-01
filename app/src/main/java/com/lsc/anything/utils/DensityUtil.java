package com.lsc.anything.utils;

import android.content.Context;

/**
 * Created by lsc on 2017/10/11 0011.
 *
 * @author lsc
 */

public class DensityUtil {
    public static int getScreenWidth(Context c) {
        return c.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context c) {
        return c.getResources().getDisplayMetrics().heightPixels;
    }
}
