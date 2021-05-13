package com.drp.common.views;


import android.content.Context;
import android.view.View;

import com.drp.base.customview.BaseCustomView;
import com.drp.common.R;
import com.drp.common.databinding.LoadMoreBinding;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class LoadMoreView extends BaseCustomView<LoadMoreBinding, LoadMoreViewModel> {

    public LoadMoreView(Context context) {
        super(context);
    }

    @Override
    protected void setDataToView(LoadMoreViewModel viewModel) {
        getDataBinding().setViewModel(viewModel);
    }

    @Override
    protected void onRootViewClicked(View v) {

    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.load_more;
    }
}