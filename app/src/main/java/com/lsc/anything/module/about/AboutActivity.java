package com.lsc.anything.module.about;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.lsc.anything.R;
import com.lsc.anything.base.ToolBarActivity;

/**
 * Created by lsc on 2017/11/3 0003.
 *
 * @author lsc
 */

public class AboutActivity extends ToolBarActivity {

    public static void start(Context c) {
        Intent intent = new Intent(c, AboutActivity.class);
        c.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.id_content, new AboutDetailFragment(), "about").commit();
    }

    @Override
    protected void setUpToolBar(Toolbar toolBar) {
        super.setUpToolBar(toolBar);
        toolBar.setTitle(R.string.about);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected boolean canGoBack() {
        return true;
    }
}
