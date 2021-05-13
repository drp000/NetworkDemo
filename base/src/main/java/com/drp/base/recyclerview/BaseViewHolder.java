package com.drp.base.recyclerview;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.drp.base.customview.BaseCustomViewModel;
import com.drp.base.customview.ICustomView;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private ICustomView view;

    public BaseViewHolder(ICustomView view) {
        super((View) view);
        this.view = view;
    }

    public void bind(BaseCustomViewModel item) {
        view.setData(item);
    }
}