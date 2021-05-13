package com.drp.networkdemo.mvvm.content;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drp.base.customview.BaseCustomViewModel;
import com.drp.base.recyclerview.BaseViewHolder;
import com.drp.common.views.LoadMoreView;
import com.drp.common.views.LoadMoreViewModel;
import com.drp.networkdemo.views.GankTextPicView;
import com.drp.networkdemo.views.GankTextPicViewModel;
import com.drp.networkdemo.views.GankTextView;
import com.drp.networkdemo.views.GankTextViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author durui
 * @date 2021/3/31
 * @description 单一职责，负责创建ViewHolder并绑定数据，如需修改UI只需修改对应的自定义View即可
 */
public class ContentRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int TYPE_TEXT = 1;
    private static final int TYPE_PICTURE = 2;
    private static final int TYPE_LOAD_MORE = 3;
    private List<BaseCustomViewModel> mItems = new ArrayList<>();

    public void setData(List<BaseCustomViewModel> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void add(BaseCustomViewModel item) {
        this.mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void add(LoadMoreViewModel item) {
        if (mItems.size() < 1) {
            return;
        }
        BaseCustomViewModel viewModel = mItems.get(mItems.size() - 1);
        if (viewModel instanceof LoadMoreViewModel) {
            LoadMoreViewModel loadMoreViewModel = (LoadMoreViewModel) viewModel;
            loadMoreViewModel.setHasMore(item.isHasMore());
            loadMoreViewModel.setTip(item.getTip());
        } else {
            this.mItems.add(item);
            notifyItemInserted(mItems.size() - 1);
        }
    }

    public void addAll(List<BaseCustomViewModel> items) {
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof GankTextViewModel) {
            return TYPE_TEXT;
        } else if (mItems.get(position) instanceof GankTextPicViewModel) {
            return TYPE_PICTURE;
        } else if (mItems.get(position) instanceof LoadMoreViewModel) {
            return TYPE_LOAD_MORE;
        }
        return -1;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TEXT) {
            GankTextView gankTextView = new GankTextView(parent.getContext());
            return new BaseViewHolder(gankTextView);
        } else if (viewType == TYPE_PICTURE) {
            GankTextPicView gankTextPicView = new GankTextPicView(parent.getContext());
            return new BaseViewHolder(gankTextPicView);
        } else if (viewType == TYPE_LOAD_MORE) {
            LoadMoreView loadMoreView = new LoadMoreView(parent.getContext());
            return new BaseViewHolder(loadMoreView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (mItems != null && mItems.size() > 0) {
            return mItems.size();
        }
        return 0;
    }

    public void removeLoadMore() {
        mItems.remove(mItems.size() - 1);
        notifyItemRemoved(mItems.size() - 1);
    }
}