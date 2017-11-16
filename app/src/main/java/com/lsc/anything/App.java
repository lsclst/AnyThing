package com.lsc.anything;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.lsc.anything.utils.CrashHandler;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsc on 2017/9/18 0018.
 *
 * @author lsc
 */

public class App extends Application {
    public static Context APPContext;
    private List<Activity> mActivities = new ArrayList<>();
    private final ActivityLifecycleCallbacks mLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivities.remove(activity);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        APPContext = this;
        LeakCanary.install(this);
        CrashHandler.getInstance().init(this.getApplicationContext());
        registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }


    public void finishAllActivity() {
        for (Activity a : mActivities) {
            a.finish();
        }
    }
}
