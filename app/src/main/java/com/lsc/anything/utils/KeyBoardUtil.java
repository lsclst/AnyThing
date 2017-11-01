package com.lsc.anything.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lsc.anything.App;

/**
 * Created by lsc on 2017/10/20 0020.
 *
 * @author lsc
 */

public class KeyBoardUtil {
    public static void showKeyBoard(Context c, View view) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void closeKeyBoard(Context c, EditText view) {
        InputMethodManager inputMethodManager = (InputMethodManager) App.APPContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
