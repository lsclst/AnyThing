package com.lsc.anything.utils;

import com.lsc.anything.App;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by lsc on 2017/9/8 0008.
 *
 * @author lsc
 */

public class ToastUtil {

    public static void showErrorMsg(String errorMsg) {
        TastyToast.makeText(App.APPContext, errorMsg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    public static void showMsg(String msg) {
        TastyToast.makeText(App.APPContext, msg, TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }

    public static void showSuccessMsg(String msg) {
        TastyToast.makeText(App.APPContext, msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }
}
