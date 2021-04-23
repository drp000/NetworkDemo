package com.drp.networkdemo.api;

import com.drp.networkdemo.beans.GankData;
import com.drp.networkdemo.beans.GankItem;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *     ┌─┐       ┌─┐
 *  ┌──┘ ┴───────┘ ┴──┐
 *  │                 │
 *  │       ───       │
 *  │  ─┬┘       └┬─  │
 *  │                 │
 *  │       ─┴─       │
 *  │                 │
 *  └───┐         ┌───┘
 *      │         │
 *      │         │
 *      │         │
 *      │         └──────────────┐
 *      │                        │
 *      │                        ├─┐
 *      │                        ┌─┘
 *      │                        │
 *      └─┐  ┐  ┌───────┬──┐  ┌──┘
 *        │ ─┤ ─┤       │ ─┤ ─┤
 *        └──┴──┘       └──┴──┘
 * 神兽坐镇，永无bug！
 * @author DRP
 * @date 2021/4/23
 * 注意，当post里不需要写具体地址的时候，需要些 。或者/ 否则Retrofit会报错
 * 当用Post方式传递Json格式数据时，需要先转换成RequestBody，方法如下
 * RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), msg)
 */
public interface DemoApiInterface {

    @POST(".")
    Observable<String> login(@Body RequestBody body);

    @GET("data/{category}/{size}/{page}")
    Observable<GankData<List<GankItem>>> getGankListByCategory(@Path("category") String category, @Path("page") int page, @Path("size") int size);
}