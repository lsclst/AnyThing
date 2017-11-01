package com.lsc.anything.widget.recylerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

/**
 * Created by lsc on 2017/9/13 0013.
 *
 * @author lsc
 */

public class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {


    public interface onLoadMoreListener {
        void onLoadMore();
    }

    private onLoadMoreListener mOnLoadMoreListener;

    public OnLoadMoreScrollListener(onLoadMoreListener loadMoreListener) {
        mOnLoadMoreListener = loadMoreListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalCount = layoutManager.getItemCount();
        int visibleCount = layoutManager.getChildCount();
        int lastCompletelyVisibleItemPosition = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastpos = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastpos);
            for (int pos :
                    lastpos) {
                if (pos > lastCompletelyVisibleItemPosition) {
                    lastCompletelyVisibleItemPosition = pos;
                }
            }
        }

        if (visibleCount > 0 && (lastCompletelyVisibleItemPosition >= totalCount - 1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMore();
            }
        }


        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            Glide.with(recyclerView.getContext()).resumeRequests();
        } else {
            Glide.with(recyclerView.getContext()).pauseRequests();
        }
    }

}
