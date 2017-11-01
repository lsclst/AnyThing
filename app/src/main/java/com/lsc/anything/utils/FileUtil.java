package com.lsc.anything.utils;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.lsc.anything.App;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by lsc on 2017/10/25 0025.
 *
 * @author lsc
 */

public class FileUtil {
    public static final String KEY_HTTP = "http";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_WEB = "webview";

    private static final String HTTP_CACHE_PATH = App.APPContext.getCacheDir() + File.separator + "httpCache" + File.separator;
    private static final String IMAGE_CACHE_PATH = Glide.getPhotoCacheDir(App.APPContext).getAbsolutePath();
    private static final String WEB_CACHE_PATH = "data/data/" + App.APPContext.getPackageName() + File.separator + "app_webview" + File.separator;

    private static long getCacheSize(File folder) {

        long fileSize = 0;
        Log.e("lsc", "getCacheSize: " + folder.getAbsolutePath() + "  " + folder.exists());
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f :
                        files) {
                    if (f.isFile()) {

                        fileSize += f.length();
                    } else if (f.isDirectory()) {
                        fileSize += getCacheSize(f);
                    }
                }
            }
        }
        return fileSize;
    }

    private static boolean deleteCache(File folder) {
        boolean isSuccess = false;
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f :
                        files) {
                    if (f.isFile()) {
                        isSuccess = deleteFile(f);
                    } else if (f.isDirectory()) {
                        File[] childFiles = f.listFiles();
                        if (childFiles == null) {
                            isSuccess = f.delete();
                        } else {
                            isSuccess = deleteCache(f);
                            f.delete();
                        }
                    }
                }
            }
        }
        return isSuccess;
    }

    private static boolean deleteFile(File f) {
        return f.exists() && f.delete();
    }


    public static String formatSize(long size) {
        DecimalFormat df = new DecimalFormat("####.00");
        if (size < 1024) // 小于1KB
        {
            return size + "Byte";
        } else if (size < 1024 * 1024) // 小于1MB
        {
            float kSize = size / 1024f;
            return df.format(kSize) + "KB";
        } else if (size < 1024 * 1024 * 1024) // 小于1GB
        {
            float mSize = size / 1024f / 1024f;
            return df.format(mSize) + "MB";
        } else if (size < 1024L * 1024L * 1024L * 1024L) // 小于1TB
        {
            float gSize = size / 1024f / 1024f / 1024f;
            return df.format(gSize) + "GB";
        } else {
            return "0kb";
        }
    }


    public static Observable<Map<String, Long>> getHttpCacheSize() {
        return Observable.create(new ObservableOnSubscribe<Map<String, Long>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Map<String, Long>> e) throws Exception {
                if (!e.isDisposed()) {
                    long cacheSize = getCacheSize(new File(HTTP_CACHE_PATH));
                    Map<String, Long> map = new HashMap<>();
                    map.put(KEY_HTTP, cacheSize);
                    e.onNext(map);
                    e.onComplete();
                }
            }
        });
    }

    public static Observable<Map<String, Long>> getImageCacheSize() {
        return Observable.create(new ObservableOnSubscribe<Map<String, Long>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Map<String, Long>> e) throws Exception {
                if (!e.isDisposed()) {
                    long cacheSize = getCacheSize(new File(IMAGE_CACHE_PATH));
                    Map<String, Long> map = new HashMap<>();
                    map.put(KEY_IMAGE, cacheSize);
                    e.onNext(map);
                    e.onComplete();
                }
            }
        });
    }

    public static Observable<Map<String, Long>> getWebViewCacheSize() {
        return Observable.create(new ObservableOnSubscribe<Map<String, Long>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Map<String, Long>> e) throws Exception {
                if (!e.isDisposed()) {
                    long cacheSize = getCacheSize(new File(WEB_CACHE_PATH));
                    Map<String, Long> map = new HashMap<>();
                    map.put(KEY_WEB, cacheSize);
                    e.onNext(map);
                    e.onComplete();
                }
            }
        });
    }

    public static Observable<Map<String, Long>> getallCachaSize() {
        return getWebViewCacheSize().concatWith(getHttpCacheSize()).concatWith(getImageCacheSize());
    }


    public static Observable<Boolean> deleteHttpCache() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteCache(new File(HTTP_CACHE_PATH)));
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> deleteImageCache() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteCache(new File(IMAGE_CACHE_PATH)));
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> deleteWebCache() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteCache(new File(WEB_CACHE_PATH)));
                e.onComplete();
            }
        });
    }

}
