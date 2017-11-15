package com.lsc.anything.base;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.lsc.anything.R;
import com.lsc.anything.widget.recylerview.MyRecyclerView;
import com.lsc.anything.widget.recylerview.OnLoadMoreScrollListener;
import com.lsc.anything.widget.recylerview.HeaderAndFooterAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public abstract class ListFragment<T> extends LazyLoadFragment implements OnLoadMoreScrollListener.onLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.id_recyclerView)
    protected MyRecyclerView mRecyclerView;
    @BindView(R.id.id_swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.addOnScrollListener(new OnLoadMoreScrollListener(this));

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }


    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract HeaderAndFooterAdapter<T> getAdapter(List<T> datas);

    @Override
    protected void initData() {
        List<T> mDatas = new ArrayList<>();
        HeaderAndFooterAdapter<T> mAdapter = getAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void showProgress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    protected void hideProgress() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public MyRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }
}


