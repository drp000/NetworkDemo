package com.drp.network.observer;


import android.util.Log;

import com.drp.base.model.MvvmBaseModel;
import com.drp.base.model.MvvmDataObserver;
import com.drp.network.exception.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author durui
 * @date 2021/3/30
 * @description
 */
public class BaseNetObserver<T> implements Observer<T> {

    private static final String TAG = BaseNetObserver.class.getSimpleName();
    private MvvmBaseModel baseModel;
    private MvvmDataObserver<T> mvvmDataObserver;

    public BaseNetObserver(MvvmBaseModel baseModel, MvvmDataObserver<T> mvvmDataObserver) {
        this.baseModel = baseModel;
        this.mvvmDataObserver = mvvmDataObserver;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        Log.i(TAG, "onSubscribe");
        if (baseModel != null) {
            baseModel.addDisposable(d);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        mvvmDataObserver.onSuccess(t, false);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof ExceptionHandle.ResponseThrowable) {
            mvvmDataObserver.onFailure(e);
        } else {
            mvvmDataObserver.onFailure(new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onComplete() {
        Log.i(TAG, "onComplete");
    }
}