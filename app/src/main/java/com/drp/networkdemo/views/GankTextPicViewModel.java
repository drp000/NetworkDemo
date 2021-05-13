package com.drp.networkdemo.views;


import com.drp.base.customview.BaseCustomViewModel;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class GankTextPicViewModel extends BaseCustomViewModel {

    public String title;
    public String url;
    public String imgUrl;
    public int errDrawable;

    public GankTextPicViewModel(String title, String url, String imgUrl, int errDrawable) {
        this.title = title;
        this.url = url;
        this.imgUrl = imgUrl;
        this.errDrawable = errDrawable;
    }
}