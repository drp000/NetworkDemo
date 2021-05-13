package com.drp.networkdemo.mvvm.category;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class CategoryViewModel extends BaseObservable {
    @Bindable
    public String name;
    @Bindable
    public int icon;

    public CategoryViewModel(String name, int icon) {
        this.name = name;
        this.icon = icon;
        notifyChange();
    }
}