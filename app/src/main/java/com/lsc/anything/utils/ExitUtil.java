package com.lsc.anything.utils;

/**
 * Created by lsc on 2017/11/9 0009.
 *
 * @author lsc
 */

public class ExitUtil {
    private static long sLastClickTime;

    public static void exit() {
        if (System.currentTimeMillis() - sLastClickTime < 2000) {
            System.exit(0);
        } else {
            ToastUtil.showMsg("再按一次退出");
            sLastClickTime = System.currentTimeMillis();
        }
    }
}
