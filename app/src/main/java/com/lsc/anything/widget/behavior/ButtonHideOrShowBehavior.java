package com.lsc.anything.widget.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public class ButtonHideOrShowBehavior extends CoordinatorLayout.Behavior<ImageView> {
    public ButtonHideOrShowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ImageView child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {//上滑
            Log.e("fab", "onNestedPreScroll: up" + dy);
            child.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start();
        } else if (dy < 0) {
            child.animate().scaleX(0.0f).scaleY(0.0f).setDuration(300).start();
            Log.e("fab", "onNestedPreScroll: down" + dy);
        }
    }
}
