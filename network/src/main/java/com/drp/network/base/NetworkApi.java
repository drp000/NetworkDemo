package com.drp.network.base;

import android.content.Context;
import android.content.Intent;

import com.drp.network.environment.EnvironmentActivity;
import com.drp.network.environment.IEnvironment;
import com.drp.network.errorhandler.HttpErrorHandler;
import com.drp.network.interceptors.CommonRequestInterceptor;
import com.drp.network.interceptors.CommonResponseInterceptor;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NetworkApi implements IEnvironment {
    private static INetworkRequiredInfo iNetworkRequiredInfo;
    private static HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();
    private String mBaseUrl;
    private OkHttpClient mOkHttpClient;
    private static boolean mIsFormal = true;

    public NetworkApi() {
        if (!mIsFormal) {
            mBaseUrl = getTest();
        }
        mBaseUrl = getFormal();
    }

    public static void chooseEnvironment(Context context) {
        if (iNetworkRequiredInfo != null && iNetworkRequiredInfo.isDebug()) {
            Intent intent = new Intent(context, EnvironmentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        iNetworkRequiredInfo = networkRequiredInfo;
        mIsFormal = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.getApplicationContext());
    }

    /**
     * ??????baseurl??????????????????????????????retofit??????
     *
     * @param service
     * @return
     */
    protected Retrofit getRetrofit(Class service) {
        if (retrofitHashMap.get(mBaseUrl + service.getName()) != null) {
            return retrofitHashMap.get(mBaseUrl + service.getName());
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(mBaseUrl);
        retrofitBuilder.client(getOkHttpClient());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();
        retrofitHashMap.put(mBaseUrl + service.getName(), retrofit);
        return retrofit;
    }


    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            //?????????
            if (getInterceptor() != null) {
                okHttpClientBuilder.addInterceptor(getInterceptor());
            }
            okHttpClientBuilder.addInterceptor(new CommonRequestInterceptor(iNetworkRequiredInfo));
            okHttpClientBuilder.addInterceptor(new CommonResponseInterceptor());
            //debug??????????????? okhttp???????????????
            if (iNetworkRequiredInfo != null && (iNetworkRequiredInfo.isDebug())) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            }
            okHttpClientBuilder.sslSocketFactory(createSSLSocketFactory());
            okHttpClientBuilder.hostnameVerifier(new TrustAllHostnameVerifier());

            mOkHttpClient = okHttpClientBuilder.build();
        }
        return mOkHttpClient;
    }


    /**
     * ???????????????????????????
     *
     * @param observer
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                if (getAppErrorHandler() != null) {
                    observable = observable.map(NetworkApi.this.<T>getAppErrorHandler());
                }
                observable.onErrorResumeNext(new HttpErrorHandler<T>())
                        .subscribe(observer);
                return observable;
            }

        };
    }

    protected abstract Interceptor getInterceptor();

    protected abstract <T> Function<T, T> getAppErrorHandler();

    //?????????SS???????????????
    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }
}
