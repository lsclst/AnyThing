package com.lsc.anything.api.splash;

import com.lsc.anything.entity.splsh.Splash;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by lsc on 2017/9/12 0012.
 *
 * @author lsc
 */

public interface SplashService {
    @GET("https://bing.ioliu.cn/v1/?type=json")
    Observable<Splash> getSplash();
}
