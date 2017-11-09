package com.lsc.anything.api;

import android.support.annotation.IntDef;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsc.anything.App;
import com.lsc.anything.BuildConfig;
import com.lsc.anything.Config;
import com.lsc.anything.api.gank.GankService;
import com.lsc.anything.api.splash.SplashService;
import com.lsc.anything.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lsc on 2017/9/11 0011.
 *
 * @author lsc
 */

public class ApiHolder {

    public static final int REQUEST_TYPE_REFRESH = 0;
    public static final int REQUEST_TYPE_LOADMORE = 1;

    private static final Object mlock = new Object();
    private Retrofit mRetrofit;
    private static GankService mGankService;
    private static SplashService mSplashService;
    private final OkHttpClient mOkHttpClient;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REQUEST_TYPE_REFRESH, REQUEST_TYPE_LOADMORE})
    public @interface RequestType {
    }

    /**
     * 缓存机制
     * 在响应请求之后在 data/data/<包名>/cache 下建立一个response 文件夹，保持缓存数据。
     * 这样我们就可以在请求的时候，如果判断到没有网络，自动读取缓存的数据。
     * 同样这也可以实现，在我们没有网络的情况下，重新打开App可以浏览的之前显示过的内容。
     * 也就是：判断网络，有网络，则从网络获取，并保存到缓存中，无网络，则从缓存中获取。
     * https://werb.github.io/2016/07/29/%E4%BD%BF%E7%94%A8Retrofit2+OkHttp3%E5%AE%9E%E7%8E%B0%E7%BC%93%E5%AD%98%E5%A4%84%E7%90%86/
     */
    private static final Interceptor cacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetWorkAvailable(App.APPContext)) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetWorkAvailable(App.APPContext)) {
                // 有网络时 设置缓存为默认值
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                // 无网络时 设置超时为1周
                int maxStale = 60 * 60 * 24 * 7;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private HttpLoggingInterceptor mLoggingInterceptor = new HttpLoggingInterceptor();

    private ApiHolder() {
        if (BuildConfig.DEBUG) {
            mLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            mLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        File cacheFile = new File(App.APPContext.getCacheDir(), "httpCache");
        Cache cache = new Cache(cacheFile, 1024 * 50 * 1024);
        mOkHttpClient = new OkHttpClient.Builder().addInterceptor(cacheControlInterceptor)
                .addNetworkInterceptor(mLoggingInterceptor)
                .cache(cache)
                .build();
        Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        mRetrofit = new Retrofit.Builder().baseUrl(Config.GANK_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .client(mOkHttpClient)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private volatile static ApiHolder instance = null;

    public static ApiHolder getInstance() {
        synchronized (ApiHolder.class) {
            if (instance == null) {
                synchronized (ApiHolder.class) {
                    instance = new ApiHolder();
                }
            }
        }
        return instance;
    }

    /**
     * @return gank.io的数据
     */
    public GankService getGankService() {
        synchronized (mlock) {
            if (mGankService == null) {
                mGankService = mRetrofit.create(GankService.class);
            }
        }
        return mGankService;
    }

    public SplashService getSplashService() {
        synchronized (mlock) {
            if (mSplashService == null) {
                mSplashService = mRetrofit.create(SplashService.class);
            }
        }
        return mSplashService;
    }

}
