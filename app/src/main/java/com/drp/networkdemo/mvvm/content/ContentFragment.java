package com.drp.networkdemo.mvvm.content;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.drp.base.IBaseModelListener;
import com.drp.base.PageResult;
import com.drp.base.customview.BaseCustomViewModel;
import com.drp.common.views.LoadMoreViewModel;
import com.drp.networkdemo.R;
import com.drp.networkdemo.databinding.FragmentContentBinding;


import java.util.ArrayList;
import java.util.List;

/**
 * @author durui
 * @date 2021/3/31
 * @description
 */
public class ContentFragment extends Fragment implements IBaseModelListener<ContentModel, List<BaseCustomViewModel>> {

    private static final String KEY_CATEGORY = "category";
    private static final int VISIBLE_THRESHOLE = 3;

    private FragmentContentBinding mBinding;
    private ContentRecyclerViewAdapter mRecyclerViewAdapter;
    private List<BaseCustomViewModel> mContentList = new ArrayList<>();
    private boolean hasMoreData;

    public static ContentFragment getInstance(String category) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false);
        return mBinding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerViewAdapter = new ContentRecyclerViewAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvContent.setLayoutManager(layoutManager);
        mBinding.rvContent.setAdapter(mRecyclerViewAdapter);
        String category = getArguments().getString(KEY_CATEGORY);

        ContentModel contentModel = new ContentModel(category, 10);
        contentModel.register(this);
        //????????????
        mBinding.srlContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contentModel.refresh();
            }
        });
        //??????????????????
        mBinding.rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int itemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (hasMoreData && !mBinding.srlContent.isRefreshing() && itemCount <= lastVisibleItemPosition + VISIBLE_THRESHOLE) {
                    contentModel.load();
                }
            }
        });
        //??????????????????
        mBinding.srlContent.setRefreshing(true);
        contentModel.getCachedDataAndLoad();
    }

    @Override
    public void onLoadSuccess(ContentModel model, List<BaseCustomViewModel> data, PageResult... pageResults) {
        if (pageResults[0].isFirstPage()) {
            //????????????????????????????????????????????????
            mContentList.clear();
            mBinding.srlContent.setRefreshing(false);
        } else {
            //????????????????????????????????????????????????View
            mRecyclerViewAdapter.removeLoadMore();
        }
        mContentList.addAll(data);
        mRecyclerViewAdapter.setData(mContentList);
        if (pageResults[0].isHasNextPage()) {
            //????????????????????????????????????????????????View
            hasMoreData = true;
            mRecyclerViewAdapter.add(new LoadMoreViewModel(true, "???????????????..."));
        } else {
            hasMoreData = false;
            mRecyclerViewAdapter.add(new LoadMoreViewModel(false, "??????????????????"));
        }
    }

    @Override
    public void onLoadFailure(ContentModel model, String error, PageResult... pageResults) {
//        Toast.makeText(getContext(), "?????????????????????" + error, Toast.LENGTH_SHORT).show();
        if (pageResults[0].isFirstPage()) {
            //????????????????????????????????????????????????
            mBinding.srlContent.setRefreshing(false);
        } else {
            //????????????????????????????????????????????????View
            mRecyclerViewAdapter.removeLoadMore();
        }
        if (pageResults[0].isHasNextPage()) {
            //????????????????????????????????????????????????View
            hasMoreData = true;
            mRecyclerViewAdapter.add(new LoadMoreViewModel(true, "???????????????..."));
        } else {
            hasMoreData = false;
            mRecyclerViewAdapter.add(new LoadMoreViewModel(false, "??????????????????"));
        }
    }
}