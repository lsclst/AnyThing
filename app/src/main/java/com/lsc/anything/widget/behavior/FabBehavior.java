package com.lsc.anything.widget.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public class FabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {//上滑
            Log.e("fab", "onNestedPreScroll: up"+dy);
            child.show();
        } else if (dy < 0) {
            child.hide();
            Log.e("fab", "onNestedPreScroll: down"+dy);

        }
    }
}
