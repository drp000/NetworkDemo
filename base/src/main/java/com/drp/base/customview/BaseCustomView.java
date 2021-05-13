package com.drp.base.customview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author durui
 * @date 2021/4/1
 * @description
 */
public abstract class BaseCustomView<T extends ViewDataBinding, S extends BaseCustomViewModel>
        extends LinearLayout implements ICustomView<S>, View.OnClickListener {

    private T dataBinding;
    private S viewModel;
    private ICustomViewActionListener listener;

    public BaseCustomView(Context context) {
        this(context, null);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public View getRootView() {
        return dataBinding.getRoot();
    }

    protected void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (getViewLayoutId() != 0) {
            dataBinding = DataBindingUtil.inflate(inflater, getViewLayoutId(), this, false);
            dataBinding.getRoot().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAction(ICustomViewActionListener.ACTION_ROOT_VIEW_CLICKED, v, viewModel);
                    }
                    onRootViewClicked(v);
                }
            });
            addView(dataBinding.getRoot());
        }
    }

    @Override
    public void setData(S data) {
        viewModel = data;
        setDataToView(viewModel);
        if (dataBinding != null) {
            dataBinding.executePendingBindings();
        }
        onDataUpdated();
    }

    @Override
    public void setStyle(int resId) {

    }

    @Override
    public void setActionListener(ICustomViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    protected T getDataBinding() {
        return dataBinding;
    }

    protected S getViewModel() {
        return viewModel;
    }

    protected void onDataUpdated() {
    }

    /**
     * 绑定数据
     *
     * @param viewModel
     */
    protected abstract void setDataToView(S viewModel);

    /**
     * 点击事件
     *
     * @param v
     */
    protected abstract void onRootViewClicked(View v);

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getViewLayoutId();
}