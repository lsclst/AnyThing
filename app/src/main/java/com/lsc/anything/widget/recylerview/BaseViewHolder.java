package com.lsc.anything.widget.recylerview;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by lsc on 2017/9/14 0014.
 *
 * @author lsc
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {


    private SparseArray<View> mViews;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    public <T extends View> T getViewById(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mItemView.findViewById(id);
            mViews.append(id, view);
        }
        return (T) view;
    }

}
