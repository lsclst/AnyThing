package com.lsc.anything.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import com.lsc.anything.App;
import com.lsc.anything.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by lsc on 2017/11/16 0016.
 *
 * @author lsc
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String CRASH_LOG_NAME = "crash.trace";
    private Context mContext;
    private static volatile CrashHandler mInstance = null;

    public static CrashHandler getInstance() {
        synchronized (CrashHandler.class) {
            if (mInstance == null) {
                synchronized (CrashHandler.class) {
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }


    private CrashHandler() {

    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handleException(e);
    }

    private void handleException(Throwable e) {
        StringBuilder builder = new StringBuilder();
        try {
            String versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            builder.append("AppVersion:").append(versionName).append("\n").append("Android OS:").append(Build.VERSION.RELEASE).append("\n").append("Device:").append(Build.DEVICE).append("\n").append("crashLog:").append("\n");
            writeToLocal(builder.toString(), e);
            PendingIntent intent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, 1000, intent);
            ((App) mContext).finishAllActivity();
            SpfUtil.getInstance().saveCrashFlag(true);
            Process.killProcess(Process.myPid());
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void writeToLocal(String crashLog, Throwable ex) {
        makeCrashFile();
        PrintWriter printWriter = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = mContext.openFileOutput(CRASH_LOG_NAME, Context.MODE_PRIVATE);
            printWriter = new PrintWriter(fileOutputStream);
            printWriter.append(crashLog);
            ex.printStackTrace(printWriter);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void makeCrashFile() {
        File androiFile = mContext.getFilesDir();
        if (!androiFile.exists()) {
            androiFile.mkdirs();
        }
        File f = new File(androiFile, CRASH_LOG_NAME);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
