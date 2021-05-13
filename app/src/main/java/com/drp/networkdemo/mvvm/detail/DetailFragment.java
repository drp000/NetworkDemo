package com.drp.networkdemo.mvvm.detail;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.drp.networkdemo.databinding.FragmentDetailBinding;


/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class DetailFragment extends Fragment {
    private FragmentDetailBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDetailBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DetailFragmentArgs args = DetailFragmentArgs.fromBundle(requireArguments());
        String url = args.getUrl();
        mBinding.webViewLayout.loadUrl(url);
    }
}