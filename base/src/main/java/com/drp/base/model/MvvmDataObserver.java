package com.drp.base.model;

/**
 * @author durui
 * @date 2021/4/2
 * @description
 */
public interface MvvmDataObserver<F> {
    /**
     * 数据获取成功
     *
     * @param f           数据
     * @param isFromCache 是否来自缓存
     */
    void onSuccess(F f, boolean isFromCache);

    /**
     * 数据获取失败
     *
     * @param e
     */
    void onFailure(Throwable e);
}
