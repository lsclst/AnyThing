package com.lsc.anything.widget.recylerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsc on 2017/10/30 0030.
 *
 * @author lsc
 */

public abstract class MultiChoiceAdapter<T> extends HeaderAndFooterAdapter<T> {
    private static final String TAG = MultiChoiceAdapter.class.getSimpleName();
    private boolean isMultiChoiceOpen;
    private ActionMode mActionMode;
    private ArrayMap<Integer, T> mChoices = new ArrayMap<>();
    private OnMultiChoiceListener mOnMultiChoiceListener = null;
    private ActionMode.Callback mCallback;

    public interface OnMultiChoiceListener {
        boolean onMultiChoiceActionModeClick(ActionMode actionMode, @IdRes int clickId);

        void onMultiItemClick(ActionMode actionMode);

        void onMultiChoiceActionModeClose();
    }

    public MultiChoiceAdapter(Context context, List data, @MenuRes int menuid, OnMultiChoiceListener onMultiChoiceListener) {
        super(context, data);
        mOnMultiChoiceListener = onMultiChoiceListener;
        mCallback = new MultiChoiceActionModeCallBack(mOnMultiChoiceListener, menuid);

    }

    public MultiChoiceAdapter(Context context, @MenuRes int menuid, OnMultiChoiceListener onMultiChoiceListener) {
        super(context);
        mOnMultiChoiceListener = onMultiChoiceListener;
        mCallback = new MultiChoiceActionModeCallBack(onMultiChoiceListener, menuid);
    }


    @Override
    protected void onDataViewBind(BaseViewHolder holder, int position, boolean isPayLoad) {
        onMultiChoiceViewBind(holder, position, isPayLoad, isMultiChoiceOpen);
    }

    protected abstract void onMultiChoiceViewBind(BaseViewHolder holder, int position, boolean isPayLoad, boolean isMultiChoiceOpen);

    public void finishedActionMode() {
        if (mActionMode != null && isMultiChoiceOpen) {
            mActionMode.finish();
            isMultiChoiceOpen = false;
            mChoices.clear();
            mActionMode = null;
            notifyItemRangeChanged(getHeaderCount(), getItemCount() - getFooterCount(), "yes");
        }
    }

    @Nullable
    public List<T> getSelectedItems() {
        if (isMultiChoiceOpen && mActionMode != null) {
            return new ArrayList<>(mChoices.values());
        }
        return null;
    }

    public void deleteSelectedItems() {
        if (isMultiChoiceOpen && mActionMode != null) {
            for (int pos :
                    mChoices.keySet()) {
                mData.remove(mChoices.get(pos));
                notifyItemRemoved(pos);
            }
            finishedActionMode();
        }
    }

    @Override
    public void clearData() {
        super.clearData();
        if (mActionMode != null && isMultiChoiceOpen) {
            mActionMode.finish();
            isMultiChoiceOpen = false;
            mChoices.clear();
            mActionMode = null;
        }

    }

    public boolean isItemCheck(int position) {
        return mChoices.containsKey(position);
    }

    @Override
    public void onLongClick(BaseViewHolder holder, int position) {
        if (!isMultiChoiceOpen && getItemViewType(position) == DATA_TYPE) {
            mActionMode = holder.itemView.startActionMode(mCallback);
            isMultiChoiceOpen = true;
            notifyItemRangeChanged(getHeaderCount(), getItemCount() - getFooterCount(), "yes");
        } else if (isMultiChoiceOpen && DATA_TYPE == getItemViewType(position)) {
            finishedActionMode();
            notifyItemRangeChanged(getHeaderCount(), getItemCount() - getFooterCount(), "yes");
        } else {
            super.onLongClick(holder, position);
        }
    }

    @Override
    public void onClick(BaseViewHolder holder, int position) {
        if (isMultiChoiceOpen && getItemViewType(position) == DATA_TYPE) {
            if (mChoices.containsKey(position)) {
                mChoices.remove(position);
            } else {
                mChoices.put(position, getData().get(position));
            }
            mOnMultiChoiceListener.onMultiItemClick(mActionMode);
            notifyItemChanged(position, "yes");
        } else {
            super.onClick(holder, position);
        }
    }


    private static class MultiChoiceActionModeCallBack implements ActionMode.Callback {

        private OnMultiChoiceListener mOnMultiChoiceListener;
        @MenuRes
        private int mMenuId;

        public MultiChoiceActionModeCallBack(OnMultiChoiceListener listener, @MenuRes int menuId) {
            mOnMultiChoiceListener = listener;
            mMenuId = menuId;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(mMenuId, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

            return mOnMultiChoiceListener.onMultiChoiceActionModeClick(actionMode, menuItem.getItemId());
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mOnMultiChoiceListener.onMultiChoiceActionModeClose();
        }
    }
}
