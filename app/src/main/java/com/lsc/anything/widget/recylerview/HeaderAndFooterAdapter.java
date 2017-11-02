package com.lsc.anything.widget.recylerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsc on 2017/9/20 0020.
 *
 * @author lsc
 */

public abstract class HeaderAndFooterAdapter<T> extends BaseRecyclerViewAdapter<T> implements ItemClickListener.onItemClickListener {
    protected static final int HEADER_TYPE = 11;
    protected static final int DATA_TYPE = 12;
    protected static final int FOOTER_TYPE = 13;
    private static final String TAG = HeaderAndFooterAdapter.class.getSimpleName();

    private int mHeaderViewId, mFooterViewId;
    private Object mHeader, mFooter;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(BaseViewHolder holder, int position, Object item);

        void onItemLongClick(BaseViewHolder holder, int position, Object item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public HeaderAndFooterAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public HeaderAndFooterAdapter(Context context) {
        super(context);
    }

    public void appendDatas(List<T> data) {
        mData.addAll(data);
        int startPos = getItemCount() - 1 - getFooterCount();
        notifyItemRangeInserted(startPos, data.size());
    }

    public void insertData(T data) {
        mData.add(data);
        int position = getItemCount() - 1 - getFooterCount();
        notifyItemInserted(position);
    }

    public int getHeaderCount() {
        return mHeaderViewId != 0 && mHeader != null ? 1 : 0;
    }

    public int getFooterCount() {
        return mFooterViewId != 0 && mFooter != null ? 1 : 0;
    }

    public Object getHeader() {
        return mHeader;
    }

    public Object getFooter() {
        return mFooter;
    }

    @LayoutRes
    public int getHeaderViewId() {
        return mHeaderViewId;
    }

    public void setHeaderView(@LayoutRes int headerViewId, Object header) {
        mHeaderViewId = headerViewId;

        if (header == null) {
            mHeader = new Object();
        } else {
            mHeader = header;
        }
        notifyItemInserted(0);
    }

    public void removeHeaderView() {
        mHeaderViewId = 0;
        if (mHeader != null) {
            mHeader = null;
            notifyItemRemoved(0);
        }
    }

    @LayoutRes
    public int getFooterViewId() {
        return mFooterViewId;
    }

    public void removeFooterView() {
        mFooterViewId = 0;
        if (mFooter != null) {
            mFooter = null;
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    public void setFooterView(int footerViewId, Object footer) {
        mFooterViewId = footerViewId;
        if (footer == null) {
            mFooter = new Object();
        } else {

            mFooter = footer;
        }
        notifyItemInserted(getItemCount() - 1);
    }

    public void updateFooter(@NonNull Object footer){
        if (mFooterViewId != 0 && mFooter != null){
            mFooter = footer;
            notifyItemChanged(getItemCount()-1);
        }

    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null) {
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(
                        holder.getItemViewType() == HEADER_TYPE || holder.getItemViewType() == FOOTER_TYPE

                );
            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    return (itemViewType == HEADER_TYPE || itemViewType == FOOTER_TYPE) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() : 1;
                }
            });
        }
        recyclerView.addOnItemTouchListener(new ItemClickListener(mContext, this));
    }

    @Override
    public void onClick(BaseViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
            int type = getItemViewType(position);
            switch (type) {
                case HEADER_TYPE:
                    mOnItemClickListener.onItemClick(holder, position, mHeader);
                    break;
                case DATA_TYPE:
                    mOnItemClickListener.onItemClick(holder, position, mData.get(position - getHeaderCount()));
                    break;
                case FOOTER_TYPE:
                    mOnItemClickListener.onItemClick(holder, position, mFooter);
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public void onLongClick(BaseViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
            int type = getItemViewType(position);
            switch (type) {
                case HEADER_TYPE:
                    mOnItemClickListener.onItemLongClick(holder, position, mHeader);
                    break;
                case DATA_TYPE:
                    mOnItemClickListener.onItemLongClick(holder, position, mData.get(position - getHeaderCount()));
                    break;
                case FOOTER_TYPE:
                    mOnItemClickListener.onItemLongClick(holder, position, mFooter);
                    break;
                default:
                    break;

            }
        }
    }

    protected abstract int getItemViewId();

    protected abstract void onDataViewBind(BaseViewHolder holder, int position,boolean isPayLoad);

    protected void onHeaderViewBind(BaseViewHolder holder, Object header,boolean isPayLoad) {
    }


    protected void onFooterViewBind(BaseViewHolder holder, Object footer,boolean isPayLoad) {
    }

    @Override
    public int getItemCount() {

        return mData != null ? mData.size() + (mFooter != null ? 1 : 0) + (mHeader != null ? 1 : 0) : 0;
    }

    @Override
    protected List<? extends ViewType> getViewTypes() {
        List<ViewType> viewTypes = new ArrayList<>(3);
        viewTypes.add(new DataViewType(this));
        viewTypes.add(new HeaderType(this));
        viewTypes.add(new FooterViewType(this));
        return viewTypes;
    }

    private static class HeaderType extends ViewType<HeaderAndFooterAdapter> {

        public HeaderType(HeaderAndFooterAdapter adapter) {
            super(adapter);
        }


        @Override
        protected int getItemViewId() {
            return adapter.getHeaderViewId();
        }

        @Override
        protected boolean isMatchType(int position) {
            return adapter.getData() != null && adapter.getHeader() != null && position == 0;
        }

        @Override
        protected int getItemType() {
            return HEADER_TYPE;
        }

        @Override
        protected void onBindViewHolder(BaseViewHolder holder, int position,boolean isPayLoad) {
            adapter.onHeaderViewBind(holder, adapter.getHeader(),isPayLoad);
        }

    }

    private static class FooterViewType extends ViewType<HeaderAndFooterAdapter> {


        public FooterViewType(HeaderAndFooterAdapter adapter) {
            super(adapter);
        }

        @Override
        protected int getItemViewId() {
            return adapter.getFooterViewId();
        }

        @Override
        protected boolean isMatchType(int position) {
            return adapter.getData() != null && adapter.getFooter() != null && adapter.getItemCount() - 1 == position;
        }

        @Override
        protected int getItemType() {
            return FOOTER_TYPE;
        }


        @Override
        protected void onBindViewHolder(BaseViewHolder holder, int position,boolean isPayLoad) {
            adapter.onFooterViewBind(holder, adapter.getFooter(),isPayLoad);
        }

    }

    private static class DataViewType extends ViewType<HeaderAndFooterAdapter> {

        public DataViewType(HeaderAndFooterAdapter adapter) {
            super(adapter);
        }


        @Override
        protected int getItemViewId() {
            return adapter.getItemViewId();
        }

        @Override
        protected boolean isMatchType(int position) {
            return position >= (adapter.getHeader() != null ? 1 : 0) && position <= (adapter.getFooter() != null ? adapter.getItemCount() - 2 : adapter.getItemCount() - 1);
        }

        @Override
        protected int getItemType() {
            return DATA_TYPE;
        }

        @Override
        protected void onBindViewHolder(BaseViewHolder holder, int position,boolean isPayLoad) {
            if (adapter.getHeader() != null) {
                position -= 1;
            }
            adapter.onDataViewBind(holder, position,isPayLoad);
        }

    }

}
