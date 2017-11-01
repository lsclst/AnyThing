package com.lsc.anything;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.module.flower.FlowerFragment;
import com.lsc.anything.module.search.SearchActivity;
import com.lsc.anything.module.setting.SettingFragment;
import com.lsc.anything.module.study.StudyFragment;
import com.lsc.anything.widget.BottomNavigationViewHelper;

import butterknife.BindView;

public class MainActivity extends ToolBarActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.id_navigationView)
    BottomNavigationView mNavigationView;
    @BindView(R.id.id_content)
    FrameLayout mContent;

    private StudyFragment mStudyFragment;
    private FlowerFragment mFlowerFragment;
    private SettingFragment mSettingFragment;
    private int mNavSelectId = -1;

    @Override
    protected void initData() {
        mStudyFragment = StudyFragment.getInstance();
        mFlowerFragment = FlowerFragment.getInstance();
        mSettingFragment = SettingFragment.getInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_content, mSettingFragment)
                .add(R.id.id_content, mFlowerFragment)
                .add(R.id.id_content, mStudyFragment)
                .commit();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mNavSelectId == R.id.nav_menu_setting) {
            menu.findItem(R.id.id_menu_search).setVisible(false);
        } else {
            menu.findItem(R.id.id_menu_search).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_menu_search) {
            SearchActivity.start(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {

        mNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(mNavigationView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUpToolBar(Toolbar toolBar) {
        super.setUpToolBar(toolBar);
        toolBar.setTitle(R.string.study);

    }

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        hideAllFragment();
        mNavSelectId = item.getItemId();
        invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.nav_menu_flower:
                getSupportActionBar().setTitle(R.string.flower);
                getSupportFragmentManager().beginTransaction().show(mFlowerFragment).commit();
                break;
            case R.id.nav_menu_setting:
                getSupportActionBar().setTitle(R.string.setting);
                getSupportFragmentManager().beginTransaction().show(mSettingFragment).commit();
                break;
            case R.id.nav_menu_study:
                getSupportActionBar().setTitle(R.string.study);
                getSupportFragmentManager().beginTransaction().show(mStudyFragment).commit();
                break;
            default:
                break;
        }
        return true;
    }


    private void hideAllFragment() {
        if (mStudyFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mStudyFragment).commit();
        }
        if (mFlowerFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mFlowerFragment).commit();
        }
        if (mSettingFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mSettingFragment).commit();
        }
    }

}
