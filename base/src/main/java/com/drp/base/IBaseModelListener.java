package com.drp.base;

/**
 * @author durui
 * @date 2021/4/1
 * @description 用于将在ViewModel里获取到的数据（缓存数据或者网络数据）分发到具体的View里
 */
public interface IBaseModelListener<F, T> {

    void onLoadSuccess(F model, T data, PageResult... pageResults);

    void onLoadFailure(F model, String error, PageResult... pageResults);

}
