package com.drp.networkdemo.mvvm.category;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.drp.networkdemo.R;
import com.drp.networkdemo.mvvm.category.CategoryViewModel;
import com.drp.networkdemo.mvvm.content.ContentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *     ┌─┐       ┌─┐
 *  ┌──┘ ┴───────┘ ┴──┐
 *  │                 │
 *  │       ───       │
 *  │  ─┬┘       └┬─  │
 *  │                 │
 *  │       ─┴─       │
 *  │                 │
 *  └───┐         ┌───┘
 *      │         │
 *      │         │
 *      │         │
 *      │         └──────────────┐
 *      │                        │
 *      │                        ├─┐
 *      │                        ┌─┘
 *      │                        │
 *      └─┐  ┐  ┌───────┬──┐  ┌──┘
 *        │ ─┤ ─┤       │ ─┤ ─┤
 *        └──┴──┘       └──┴──┘
 * 神兽坐镇，永无bug！
 * @author DRP
 * @date 2021/5/6
 *
 */
public class CategoryPagerAdapter extends FragmentStateAdapter {

    private List<CategoryViewModel> mModelList = new ArrayList<>();
    private List<ContentFragment> mFragments = new ArrayList<>();

    public CategoryPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        mModelList.add(new CategoryViewModel("全部", R.mipmap.icon_all));
        mModelList.add(new CategoryViewModel("Android", R.mipmap.icon_android));
        mModelList.add(new CategoryViewModel("iOS", R.mipmap.icon_ios));
        mModelList.add(new CategoryViewModel("前端", R.mipmap.icon_web));
        mModelList.add(new CategoryViewModel("休息视频", R.mipmap.icon_video));
        mModelList.add(new CategoryViewModel("福利", R.mipmap.icon_weal));
        mModelList.add(new CategoryViewModel("拓展资源", R.mipmap.icon_expand));
        mModelList.add(new CategoryViewModel("瞎推荐", R.mipmap.icon_recommend));
        mModelList.add(new CategoryViewModel("App", R.mipmap.icon_app));

        for (int i = 0; i < mModelList.size(); i++) {
            if (i == 0) {
                mFragments.add(ContentFragment.getInstance("all"));
            } else {
                mFragments.add(ContentFragment.getInstance(mModelList.get(i).name));
            }
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

    public int getTabIcon(int position) {
        return mModelList.get(position).icon;
    }

    public String getTabTitle(int position) {
        return mModelList.get(position).name;
    }
}