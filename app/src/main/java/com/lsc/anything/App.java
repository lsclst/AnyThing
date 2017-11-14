package com.lsc.anything;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lsc on 2017/9/18 0018.
 *
 * @author lsc
 */

public class App extends Application {
    public static Context APPContext;

    @Override
    public void onCreate() {
        super.onCreate();
        APPContext = this;
        LeakCanary.install(this);
    }
}
