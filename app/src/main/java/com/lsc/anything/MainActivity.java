package com.lsc.anything;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.module.flower.FlowerFragment;
import com.lsc.anything.module.search.SearchActivity;
import com.lsc.anything.module.setting.SettingFragment;
import com.lsc.anything.module.study.StudyFragment;
import com.lsc.anything.utils.ExitUtil;
import com.lsc.anything.widget.BottomNavigationViewHelper;

import butterknife.BindView;

public class MainActivity extends ToolBarActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String FM_TAG_STUDY = "study";
    public static final String FM_TAG_FLOWER = "flower";
    public static final String FM_TAG_SETTING = "setting";

    @BindView(R.id.id_navigationView)
    BottomNavigationView mNavigationView;
    @BindView(R.id.id_content)
    FrameLayout mContent;

    private StudyFragment mStudyFragment;
    private FlowerFragment mFlowerFragment;
    private SettingFragment mSettingFragment;
    private int mNavSelectId = -1;
    private boolean isgridLayout = true;

    @Override
    protected void initData() {
        mStudyFragment = StudyFragment.getInstance();
        mFlowerFragment = FlowerFragment.getInstance();
        mSettingFragment = SettingFragment.getInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_content, mStudyFragment, FM_TAG_STUDY)
                .commit();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mNavSelectId == R.id.nav_menu_flower) {
            menu.findItem(R.id.id_menu_change_layout).setVisible(true);
            boolean gridLayout = mFlowerFragment.isGridLayout();
            if (gridLayout) {
                menu.findItem(R.id.id_menu_change_layout).setIcon(R.drawable.ic_menu_stagger);
                menu.findItem(R.id.id_menu_change_layout).setTitle(R.string.stagger);
            } else {
                menu.findItem(R.id.id_menu_change_layout).setIcon(R.drawable.ic_menu_grid);
                menu.findItem(R.id.id_menu_change_layout).setTitle(R.string.grid);
            }
        } else {
            menu.findItem(R.id.id_menu_change_layout).setVisible(false);
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
        if (id == R.id.id_menu_change_layout) {
            if (mFlowerFragment.isGridLayout()) {
                item.setTitle(R.string.grid);
                item.setIcon(R.drawable.ic_menu_grid);
            } else {
                item.setTitle(R.string.stagger);
                item.setIcon(R.drawable.ic_menu_stagger);
            }
            mFlowerFragment.changeListLayout();
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
                if (mFlowerFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(mFlowerFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    mFlowerFragment = FlowerFragment.getInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.id_content, mFlowerFragment, FM_TAG_FLOWER)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
                break;
            case R.id.nav_menu_setting:
                getSupportActionBar().setTitle(R.string.setting);
                if (mSettingFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(mSettingFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.id_content, mSettingFragment, FM_TAG_SETTING)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
                break;
            case R.id.nav_menu_study:
                getSupportActionBar().setTitle(R.string.study);
                if (mStudyFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(mStudyFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    mStudyFragment = StudyFragment.getInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.id_content, mStudyFragment, FM_TAG_STUDY)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
                break;
            default:
                break;
        }
        return true;
    }


    private void hideAllFragment() {
        if (mStudyFragment.isAdded() && mStudyFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mStudyFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
        if (mFlowerFragment.isAdded() && mFlowerFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mFlowerFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
        if (mSettingFragment.isAdded() && mSettingFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mSettingFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
    }

    @Override
    public void onBackPressed() {
        ExitUtil.exit();
    }
}
