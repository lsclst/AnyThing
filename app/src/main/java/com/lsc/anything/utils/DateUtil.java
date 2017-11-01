package com.lsc.anything.utils;

import java.util.Calendar;

/**
 * Created by lsc on 2017/9/18 0018.
 *
 * @author lsc
 */

public class DateUtil {

    private static volatile DateUtil INSTANCE;
    private Calendar mCalendar;

    private DateUtil() {
        mCalendar = Calendar.getInstance();
    }

    public static DateUtil getINSTANCE() {
        synchronized (DateUtil.class) {
            if (INSTANCE == null) {
                synchronized (DateUtil.class) {
                    INSTANCE = new DateUtil();
                }
            }
        }
        return INSTANCE;
    }

    public String formatDate(String date) {
        String[] strings = date.split("T");
        if (strings.length >= 2) {
            return strings[0];
        } else {
            return "";
        }
    }
}
