package com.lsc.anything.widget.recylerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsc on 2017/9/20 0020.
 *
 * @author lsc
 */

public abstract class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter<BaseViewHolder>  {
    private static final String TAG = BaseRecyclerViewAdapter.class.getSimpleName();
    protected List<E> mData;
    private List<? extends ViewType> mViewTypes;
    protected Context mContext;


    public BaseRecyclerViewAdapter(Context context, List<E> data) {
        mData = data != null ? data : new ArrayList<E>();
        mContext = context;
        mViewTypes = getViewTypes();
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    protected abstract List<? extends ViewType> getViewTypes();

    public BaseRecyclerViewAdapter(Context context) {
        this(context, null);
    }




    public List<E> getData() {
        return mData;
    }

    public void setData(List<E> data) {
        mData = data;
        notifyDataSetChanged();
    }


    public void clearData() {
        mData.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        if (mViewTypes != null && mViewTypes.size() > 0) {
            for (ViewType viewType : mViewTypes) {
                if (viewType.getItemType() == type) {
                    View view = LayoutInflater.from(mContext).inflate(viewType.getItemViewId(), viewGroup, false);
                    return new BaseViewHolder(view);
                }
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int i) {
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        if (mViewTypes != null && mViewTypes.size() > 0) {
            for (ViewType type :
                    mViewTypes) {
                if (type.isMatchType(position)) {
                    type.onBindViewHolder(holder, position,payloads.isEmpty());
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mViewTypes != null && mViewTypes.size() > 0) {
            for (ViewType type :
                    mViewTypes) {
                if (type.isMatchType(position)) {
                    return type.getItemType();
                }
            }
        }
        return super.getItemViewType(position);
    }



    public static abstract class ViewType<T extends BaseRecyclerViewAdapter> {
        T adapter;

        public ViewType(T adapter) {
            this.adapter = adapter;
        }

        @LayoutRes
        protected abstract int getItemViewId();

        protected abstract boolean isMatchType(int position);

        protected abstract int getItemType();

        protected abstract void onBindViewHolder(BaseViewHolder holder, int position,boolean isPayLoad);

    }
}
