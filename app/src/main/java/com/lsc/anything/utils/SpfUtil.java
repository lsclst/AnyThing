package com.lsc.anything.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lsc.anything.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsc on 2017/10/23 0023.
 *
 * @author lsc
 */

public class SpfUtil {
    private static final String KEY_SEARCH_HIStORY = "search_history";
    private static final String KEY_SEARCH_COUNT = "search_size";
    private static final String SPF_NAME = "my_spf";

    private SharedPreferences spf;

    private SpfUtil() {
        spf = App.APPContext.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
    }

    private static volatile SpfUtil instance;

    public static SpfUtil getInstance() {
        synchronized (SpfUtil.class) {
            if (instance == null) {
                synchronized (SpfUtil.class) {
                    instance = new SpfUtil();
                }
            }
        }
        return instance;
    }

    public void saveSearchHistories(List<String> histories) {
        spf.edit().clear().commit();
        if (histories.size() <= 0) {
            return;
        }
        spf.edit().putInt(KEY_SEARCH_COUNT, histories.size()).apply();
        for (int i = 0; i < histories.size(); i++) {
            spf.edit().putString(KEY_SEARCH_HIStORY + i, histories.get(i)).apply();
        }
    }

    public List<String> getSearchHistories() {
        List<String> results = new ArrayList<>();
        int size = spf.getInt(KEY_SEARCH_COUNT, -1);
        if (size == -1) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            String item = spf.getString(KEY_SEARCH_HIStORY + i, "");
            results.add(item);
        }
        return results;
    }

}
