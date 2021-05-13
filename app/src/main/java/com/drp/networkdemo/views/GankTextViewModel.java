package com.drp.networkdemo.views;


import com.drp.base.customview.BaseCustomViewModel;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class GankTextViewModel extends BaseCustomViewModel {

    public String title;
    public String url;

    public GankTextViewModel(String title, String url) {
        this.title = title;
        this.url = url;
    }
}