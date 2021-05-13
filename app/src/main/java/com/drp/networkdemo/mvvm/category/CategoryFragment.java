package com.drp.networkdemo.mvvm.category;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.drp.networkdemo.MainActivity;
import com.drp.networkdemo.databinding.FragmentCategoryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        TabLayout tabLayout = binding.tabs;
        ViewPager2 viewPager = binding.viewPager;
        CategoryPagerAdapter pagerAdapter = new CategoryPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setIcon(pagerAdapter.getTabIcon(position));
            tab.setText(pagerAdapter.getTabTitle(position));
        }).attach();

        MainActivity activity = (MainActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar);

        return binding.getRoot();
    }
}