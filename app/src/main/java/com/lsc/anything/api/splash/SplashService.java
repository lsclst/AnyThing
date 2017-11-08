package com.lsc.anything.api.splash;

import com.lsc.anything.entity.splsh.Splash;
import com.lsc.anything.entity.splsh.SplashDetail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by lsc on 2017/9/12 0012.
 *
 * @author lsc
 */

public interface SplashService {
    @GET("http://open.lovebizhi.com/baidu_rom.php?width=540&height=960&type=1")
    Observable<Splash> getSplash();
    @GET
    Observable<SplashDetail> getSplashDetail(@Url String path);
}
