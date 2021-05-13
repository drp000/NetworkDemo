package com.drp.base.model;


import android.text.TextUtils;

import androidx.annotation.CallSuper;

import com.drp.base.IBaseModelListener;
import com.drp.base.PageResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author durui
 * @date 2021/4/2
 * @description ViewModel的基类，负责缓存数据的获取和保存 需要考虑分页，缓存，预置
 */
public abstract class MvvmBaseModel<F, T> implements MvvmDataObserver<F> {

    private CompositeDisposable compositeDisposable;
    protected ReferenceQueue<IBaseModelListener<F, T>> mReferenceQueue;
    protected ConcurrentLinkedQueue<WeakReference<IBaseModelListener<F, T>>> mWeakListenerArrayList;
    private BaseCachedData<F> mCachedData;
    /**
     * 缓存的key，不需要缓存传空
     */
    private String mCachedPreferenceKey;
    /**
     * 预置的数据，没有传空，一般是那些不经常改变的数据
     */
    private String mApkPredefineData;
    /**
     * 是否分页
     */
    private boolean mIsPaging;
    private Class<F> clazz;
    /**
     * 初始页码，用于刷新的时候重置当前页码
     */
    private final int INIT_PAGE_NUMBER;
    /**
     * 当前页码
     */
    protected int mPageNumber = 0;


    public MvvmBaseModel(Class<F> clazz, boolean isPaging, String cachePreferenceKey, String apkPredefineData, int... initPageNumber) {
        this.mIsPaging = isPaging;
        this.clazz = clazz;
        this.mPageNumber = this.INIT_PAGE_NUMBER = (initPageNumber != null && initPageNumber.length > 0) ? initPageNumber[0] : 0;
        this.mCachedPreferenceKey = cachePreferenceKey;
        this.mApkPredefineData = apkPredefineData;
        mReferenceQueue = new ReferenceQueue<>();
        mWeakListenerArrayList = new ConcurrentLinkedQueue<>();
        if (mCachedPreferenceKey != null) {
            mCachedData = new BaseCachedData<F>();
        }
    }

    public void register(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            Reference<? extends IBaseModelListener<F, T>> releaseListener = null;
            while ((releaseListener = mReferenceQueue.poll()) != null) {
                mWeakListenerArrayList.remove(releaseListener);
            }
            for (WeakReference<IBaseModelListener<F, T>> weakReference : mWeakListenerArrayList) {
                IBaseModelListener<F, T> modelListener = weakReference.get();
                if (modelListener == listener) {
                    return;
                }
            }
            WeakReference<IBaseModelListener<F, T>> weakReference = new WeakReference(listener, mReferenceQueue);
            mWeakListenerArrayList.add(weakReference);
        }
    }

    public void unRegister(IBaseModelListener<F, T> listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            for (WeakReference<IBaseModelListener<F, T>> weakReference : mWeakListenerArrayList) {
                IBaseModelListener<F, T> modelListener = weakReference.get();
                if (listener == modelListener) {
                    mWeakListenerArrayList.remove(weakReference);
                    break;
                }
            }
        }
    }

    protected void saveDataToPreference(F data) {
        if (data != null) {
            mCachedData.data = data;
            mCachedData.updateTime = System.currentTimeMillis();
            BasicDataPreferenceUtil.getInstance().setString(mCachedPreferenceKey, new Gson().toJson(mCachedData));
        }
    }

    public void refresh() {
        if (mIsPaging) {
            mPageNumber = INIT_PAGE_NUMBER;
        }
        load();
    }

    /**
     * 是否每次都更新数据，可以设计策略，可以是一天一次，一月一次等
     * 默认每次都更新
     *
     * @return
     */
    public boolean isNeedToUpdate() {
        return true;
    }

    @CallSuper
    public void cancel() {
        if (compositeDisposable != null && compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }

    /**
     * 获取缓存以及记载新数据
     */
    public void getCachedDataAndLoad() {
        if (mCachedPreferenceKey != null) {
            String cachedData = BasicDataPreferenceUtil.getInstance().getString(mCachedPreferenceKey);
            if (!TextUtils.isEmpty(cachedData)) {
                try {
                    F savedData = new Gson().fromJson(new JSONObject(cachedData).getString("data"), clazz);
                    if (savedData != null) {
                        onSuccess(savedData, true);
                        if (isNeedToUpdate()) {
                            load();
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mApkPredefineData != null) {
                try {
                    F predefinedData = new Gson().fromJson(mApkPredefineData, clazz);
                    if (predefinedData != null) {
                        onSuccess(predefinedData, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        load();
    }

    protected void notifyResultToListeners(F netData, T data, boolean isFromCache) {
        synchronized (this) {
            for (WeakReference<IBaseModelListener<F, T>> weakReference : mWeakListenerArrayList) {
                IBaseModelListener modelListener = weakReference.get();
                if (modelListener != null) {
                    if (mIsPaging) {
                        modelListener.onLoadSuccess(this, data, isFromCache
                                ? new PageResult(false, true, true)
                                : new PageResult(data == null ? true : ((List) data).isEmpty(), mPageNumber == INIT_PAGE_NUMBER, data == null ? false : !((List) data).isEmpty()));

                    } else {
                        modelListener.onLoadSuccess(this, data);
                    }
                }
            }
            if (mIsPaging) {
                if (mCachedPreferenceKey != null && mPageNumber == INIT_PAGE_NUMBER && !isFromCache) {
                    saveDataToPreference(netData);
                }
                if (!isFromCache) {
                    if (data != null && data instanceof List && ((List) data).size() > 0) {
                        mPageNumber++;
                    }
                }
            } else {
                if (mCachedPreferenceKey != null && !isFromCache) {
                    saveDataToPreference(netData);
                }
            }
        }
    }

    protected void loadFail(final String errMsg) {
        synchronized (this) {
            for (WeakReference<IBaseModelListener<F, T>> weakReference : mWeakListenerArrayList) {
                IBaseModelListener modelListener = weakReference.get();
                if (modelListener != null) {
                    if (mIsPaging) {
                        modelListener.onLoadFailure(this, errMsg, new PageResult(true, mPageNumber == INIT_PAGE_NUMBER, false));
                    } else {
                        modelListener.onLoadFailure(this, errMsg);
                    }
                }
            }
        }
    }

    protected abstract void load();
}