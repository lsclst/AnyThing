package com.lsc.anything;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.module.flower.FlowerDetailActivity;
import com.lsc.anything.module.flower.FlowerFragment;
import com.lsc.anything.module.search.SearchActivity;
import com.lsc.anything.module.setting.SettingFragment;
import com.lsc.anything.module.study.StudyFragment;
import com.lsc.anything.utils.ExitUtil;
import com.lsc.anything.utils.FileUtil;
import com.lsc.anything.utils.ShareUtil;
import com.lsc.anything.utils.SpfUtil;
import com.lsc.anything.widget.BottomNavigationViewHelper;
import com.lsc.anything.widget.recylerview.BaseViewHolder;

import java.util.List;
import java.util.Map;

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
    private int mNavSelectId = R.id.nav_menu_study;
    private Bundle mReturnBundle;

    @Override
    protected void initData() {
        mStudyFragment = StudyFragment.getInstance();
        mFlowerFragment = FlowerFragment.getInstance();
        mSettingFragment = SettingFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.id_content, mStudyFragment, FM_TAG_STUDY).commit();

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
        } else if (id == R.id.id_menu_change_layout) {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExitSharedElementCallback(new SharedElementCallback() {

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (mReturnBundle != null) {
                    int startPos = mReturnBundle.getInt(FlowerDetailActivity.KEY_STRAT_POS);
                    int endPos = mReturnBundle.getInt(FlowerDetailActivity.KEY_END_POS);
                    if (startPos != endPos) {
                        names.clear();
                        sharedElements.clear();
                        String s = mReturnBundle.getString(FlowerDetailActivity.KEY_SEN);
                        BaseViewHolder holder = (BaseViewHolder) mFlowerFragment.getRecyclerView().findViewHolderForAdapterPosition(endPos);
                        names.add(s);
                        sharedElements.put(s, holder.getViewById(R.id.id_flower_img));
                        mReturnBundle = null;
                    }
                }
            }
        });
    }

    @Override
    protected void initView() {
        mNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(mNavigationView);
        Log.e(TAG, "initView: "+SpfUtil.getInstance().getCrashFlag() );
        if (SpfUtil.getInstance().getCrashFlag()) {
            new AlertDialog.Builder(this, R.style.dialogNoTitle).setMessage(R.string.isLogFeedBack).setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SpfUtil.getInstance().saveCrashFlag(false);
                    String crashlog = FileUtil.readCrashFileTOString(MainActivity.this);
                    ShareUtil.sendMail(MainActivity.this, getString(R.string.sendto), getString(R.string.log_feedBack), crashlog);
                }
            }).setNegativeButton(R.string.btn_reject, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SpfUtil.getInstance().saveCrashFlag(false);
                }
            }).setTitle(R.string.log_feedBack).setCancelable(false).show();
        }
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
        if (mNavSelectId == item.getItemId()) {
            return false;
        }
        hideAllFragment();
        mNavSelectId = item.getItemId();
        invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.nav_menu_flower:
                getSupportActionBar().setTitle(R.string.flower);
                if (mFlowerFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(mFlowerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    mFlowerFragment = FlowerFragment.getInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.id_content, mFlowerFragment, FM_TAG_FLOWER).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
                break;
            case R.id.nav_menu_setting:
                getSupportActionBar().setTitle(R.string.setting);
                if (mSettingFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(mSettingFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.id_content, mSettingFragment, FM_TAG_SETTING).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
                break;
            case R.id.nav_menu_study:
                getSupportActionBar().setTitle(R.string.study);
                if (mStudyFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(mStudyFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    mStudyFragment = StudyFragment.getInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.id_content, mStudyFragment, FM_TAG_STUDY).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
                break;
            default:
                break;
        }
        return true;
    }


    private void hideAllFragment() {
        if (mStudyFragment.isAdded() && mStudyFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mStudyFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
        if (mFlowerFragment.isAdded() && mFlowerFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mFlowerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
        if (mSettingFragment.isAdded() && mSettingFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(mSettingFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
    }

    @Override
    public void onBackPressed() {
        ExitUtil.exit();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        mReturnBundle = data.getExtras();
        int startPos = mReturnBundle.getInt(FlowerDetailActivity.KEY_STRAT_POS);
        int endPos = mReturnBundle.getInt(FlowerDetailActivity.KEY_END_POS);
        if (startPos != endPos) {
            mFlowerFragment.getRecyclerView().scrollToPosition(endPos);
        }
        supportPostponeEnterTransition();
        mFlowerFragment.getRecyclerView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mFlowerFragment.getRecyclerView().getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return true;
            }
        });

    }

    @Override
    protected void onToolBarClick(View toolbar) {
        super.onToolBarClick(toolbar);
        switch (mNavSelectId) {
            case R.id.nav_menu_flower:
                if (mFlowerFragment != null) {
                    mFlowerFragment.scrollToTop();
                }
                break;
            case R.id.nav_menu_study:
                if (mStudyFragment != null) {
                    mStudyFragment.scrollToTop();
                }
                break;
            default:
                break;
        }
    }
}
