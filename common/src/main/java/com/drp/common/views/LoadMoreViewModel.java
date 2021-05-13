package com.drp.common.views;


import androidx.databinding.Bindable;

import com.drp.base.customview.BaseCustomViewModel;
import com.drp.common.BR;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class LoadMoreViewModel extends BaseCustomViewModel {
    private String tip;
    private boolean hasMore;

    public LoadMoreViewModel(boolean hasMore, String tip) {
        this.hasMore = hasMore;
        this.tip = tip;
    }

    @Bindable
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
        notifyPropertyChanged(BR.tip);
    }

    @Bindable
    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        notifyPropertyChanged(BR.hasMore);
    }
}