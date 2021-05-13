package com.drp.base.customview;

import android.view.View;

/**
 * @author durui
 * @date 2021/4/1
 * @description
 */
interface ICustomViewActionListener {

    String ACTION_ROOT_VIEW_CLICKED = "ACTION_ROOT_VIEW_CLICKED";

    void onAction(String actionRootViewClicked, View v, BaseCustomViewModel viewModel);
}
