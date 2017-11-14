package com.lsc.anything.base;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.lsc.anything.R;

import butterknife.BindView;

/**
 * Created by lsc on 2017/9/11 0011.
 *
 * @author lsc
 */

public abstract class ToolBarActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ToolBar)
    protected Toolbar mToolbar;
    @BindView(R.id.AppBarLayout)
    protected AppBarLayout mAppBarLayout;
    private boolean isToolBarHiding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
    }

    protected void initToolBar() {

        mToolbar.setOnClickListener(this);
        setUpToolBar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack());
    }

    protected void setUpToolBar(Toolbar toolBar) {
    }


    protected void setToolbarColor(@ColorInt int color) {
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(color);
        }
    }

    protected void hideOrShowAppBar() {
        if (mAppBarLayout != null) {
            mAppBarLayout.animate().cancel();
            mAppBarLayout.animate()
                    .translationY(isToolBarHiding ? 0 : -mAppBarLayout.getHeight())
                    .setInterpolator(new DecelerateInterpolator(2))
                    .start();
            isToolBarHiding = !isToolBarHiding;
        } else if (mToolbar != null) {
            mToolbar.animate().cancel();
            mToolbar.animate()
                    .translationY(isToolBarHiding ? 0 : -mToolbar.getHeight())
                    .setInterpolator(new DecelerateInterpolator(2))
                    .start();
            isToolBarHiding = !isToolBarHiding;
        }
    }

    protected boolean canGoBack() {
        return false;
    }


    @Override
    public void onClick(View v) {
        onToolBarClick(v);
    }

    protected void onToolBarClick(View toolbar) {

    }
}
