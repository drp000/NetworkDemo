package com.drp.base.customview;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public interface ICustomView<S extends BaseCustomViewModel> {

    void setData(S data);

    void setStyle(int resId);

    void setActionListener(ICustomViewActionListener listener);
}
