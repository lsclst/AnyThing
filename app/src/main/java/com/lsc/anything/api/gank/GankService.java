package com.lsc.anything.api.gank;

import com.lsc.anything.Config;
import com.lsc.anything.entity.gank.GankResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by lsc on 2017/9/11 0011.
 *
 * @author lsc
 */

public interface GankService {
    @GET("data/Android/" + Config.GANK_SIZE + "/{page}")
    Observable<GankResult> getAndroidGank(@Path("page") int page);

    @GET("data/iOS/" + Config.GANK_SIZE + "/{page}")
    Observable<GankResult> getIosGank(@Path("page") int page);

    @GET("data/福利/" + Config.GANK_SIZE + "/{page}")
    Observable<GankResult> getMeizi(@Path("page") int page);

    @GET("data/前端/" + Config.GANK_SIZE + "/{page}")
    Observable<GankResult> getWebGank(@Path("page") int page);

    @FormUrlEncoded
    @POST("add2gank")
    void postGank(@Field("url") String url, @Field("desc") String desc, @Field("who") String who, @Field("type") String type);

    @Headers("Connection:close")
    @GET("search/query/{keyword}/category/all/count/" + Config.GANK_SIZE + "/page/{page}")
    Observable<GankResult> search(@Path("keyword") String keyword, @Path("page") int page);
}
