package com.lsc.anything.widget.recylerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by lsc on 2017/11/13 0013.
 *
 * @author lsc
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        Adapter adapter = getAdapter();
        if (adapter != null && adapter instanceof HeaderAndFooterAdapter){
            ((HeaderAndFooterAdapter) adapter).changeGridSpan(this);
        }
    }
}
