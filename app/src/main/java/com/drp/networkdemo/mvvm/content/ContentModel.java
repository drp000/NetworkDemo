package com.drp.networkdemo.mvvm.content;


import android.annotation.SuppressLint;

import com.drp.base.customview.BaseCustomViewModel;
import com.drp.base.model.MvvmBaseModel;
import com.drp.networkdemo.views.GankTextPicViewModel;
import com.drp.networkdemo.views.GankTextViewModel;
import com.drp.network.observer.BaseNetObserver;
import com.drp.networkdemo.R;
import com.drp.networkdemo.api.GankApi;
import com.drp.networkdemo.api.GankApiInterface;
import com.drp.networkdemo.beans.GankData;
import com.drp.networkdemo.beans.GankItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author durui
 * @date 2021/4/1
 * @description 只需要考虑数据的加载和分发
 */
public class ContentModel extends MvvmBaseModel<GankData, List<BaseCustomViewModel>> {
    private static final String TAG = ContentModel.class.getSimpleName();
    private final String mCategory;
    private final int mSize;

    public ContentModel(String category, int size) {
        super(GankData.class, true, "cache_" + category, null, 1);
        this.mCategory = category;
        this.mSize = size;
    }

    @SuppressLint("CheckResult")
    protected void load() {
        GankApi.getService(GankApiInterface.class)
                .getGankListByCategory(mCategory, mPageNumber, mSize)
                .compose(GankApi.getInstance()
                        .applySchedulers(new BaseNetObserver<GankData>(this, this)));
    }

    @Override
    public void onSuccess(GankData gankData, boolean isFromCache) {
        List<BaseCustomViewModel> baseCustomViewModels = new ArrayList<>();
        for (GankItem result : gankData.results) {
            BaseCustomViewModel viewModel;
            if (result.images != null && result.images.size() > 0) {
                viewModel = new GankTextPicViewModel(result.desc, result.url, result.images.get(0), R.mipmap.icon_weal);
            } else {
                viewModel = new GankTextViewModel(result.desc, result.url);
            }
            baseCustomViewModels.add(viewModel);
        }
        notifyResultToListeners(gankData, baseCustomViewModels, isFromCache);
    }

    @Override
    public void onFailure(Throwable e) {
        e.printStackTrace();
        loadFail(e.getMessage());
    }
}