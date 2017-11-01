package com.lsc.anything.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lsc on 2017/9/27 0027.
 *
 * @author lsc
 */

public class HackViewPager extends ViewPager {
    public HackViewPager(Context context) {
        super(context);
    }

    public HackViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {

            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
