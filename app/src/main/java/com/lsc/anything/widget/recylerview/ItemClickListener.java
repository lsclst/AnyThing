package com.lsc.anything.widget.recylerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lsc on 2017/9/20 0020.
 *
 * @author lsc
 */

public class ItemClickListener extends GestureDetector.SimpleOnGestureListener implements RecyclerView.OnItemTouchListener {
    public interface onItemClickListener {
        void onClick(BaseViewHolder holder, int position);

        void onLongClick(BaseViewHolder holder, int position);
    }

    private GestureDetector mGestureDetector;
    private onItemClickListener mOnItemClickListener;

    public ItemClickListener(Context context, onItemClickListener listener) {
        mOnItemClickListener = listener;
        mGestureDetector = new GestureDetector(context, this);
    }

    private RecyclerView mRecyclerView;


    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (mRecyclerView == null) {
            mRecyclerView = recyclerView;
        }
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public void onLongPress(MotionEvent e) {
        View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null){

            int pos = mRecyclerView.getChildAdapterPosition(childView);
            RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(childView);
            if (viewHolder != null){
                mOnItemClickListener.onLongClick((BaseViewHolder) viewHolder, pos);
            }
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView!= null){

            int pos = mRecyclerView.getChildAdapterPosition(childView);
            RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(childView);
            mOnItemClickListener.onClick((BaseViewHolder) viewHolder, pos);
            return true;
        }
        return false;
    }
}
