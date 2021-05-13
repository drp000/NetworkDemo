package com.drp.networkdemo.api;


import com.drp.network.base.NetworkApi;
import com.drp.network.errorhandler.ExceptionHandle;
import com.drp.networkdemo.beans.GankBaseResponse;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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
 *
 */
public class GankApi extends NetworkApi {

    //使用双重检查的单例模式，加volatile防止指令重排实现线程安全
    //此处也可以使用静态内部类的方式实现单例
    private static volatile GankApi sInstance;

    public static GankApi getInstance() {
        if (sInstance == null) {
            synchronized (GankApi.class) {
                if (sInstance == null) {
                    sInstance = new GankApi();
                }
            }
        }
        return sInstance;
    }

    public static <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    protected Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //此处做一些自定义拦截器，比如添加通用的请求头等
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Source", "source");
                return chain.proceed(builder.build());
            }
        };
    }

    @Override
    protected <T> Function<T, T> getAppErrorHandler() {
        return new Function<T, T>() {
            @Override
            public T apply(@NonNull T response) throws Exception {
                //此处做一些统一的错误处理，比如说判断statusCode等
                if (response instanceof GankBaseResponse && ((GankBaseResponse) response).statusCode != 0) {
                    ExceptionHandle.ServerException serverException = new ExceptionHandle.ServerException();
                    serverException.code = ((GankBaseResponse) response).statusCode;
                    serverException.message = ((GankBaseResponse) response).errorMsg != null ? ((GankBaseResponse) response).errorMsg : "";
                    throw serverException;
                }
                return response;
            }
        };
    }

    @Override
    public String getFormal() {
        return "https://gank.io/api/";
    }

    @Override
    public String getTest() {
        return "https://gank.io/api/";
    }
}