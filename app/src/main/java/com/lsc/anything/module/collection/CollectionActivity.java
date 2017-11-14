package com.lsc.anything.module.collection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.lsc.anything.R;
import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.module.flower.FlowerDetailActivity;
import com.lsc.anything.widget.recylerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by lsc on 2017/10/31 0031.
 *
 * @author lsc
 */

public class CollectionActivity extends ToolBarActivity {
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.id_tabLayout)
    TabLayout mTabLayout;
    private Bundle mReturnBundle;
    private CollectionFragment mImgFragment;

    @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        mImgFragment = CollectionFragment.getInstance(CollectionFragment.TYPE_IMAGE);
        fragments.add(mImgFragment);
        fragments.add(CollectionFragment.getInstance(CollectionFragment.TYPE_ARTICLE));
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    for (Fragment f : adapter.getFragments()) {
                        ((CollectionFragment) f).closeMultiChoice();
                    }
                }
            }
        });
    }

    @Override
    protected void setUpToolBar(Toolbar toolBar) {
        super.setUpToolBar(toolBar);
        toolBar.setTitle(getString(R.string.my_collection));
    }

    @Override
    protected void initView() {
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected boolean canGoBack() {
        return true;
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
                        BaseViewHolder holder = (BaseViewHolder) mImgFragment.getRecyclerView().findViewHolderForAdapterPosition(endPos);
                        names.add(s);
                        sharedElements.put(s, holder.getViewById(R.id.id_flower_img));
                        mReturnBundle = null;
                    }
                }
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        mReturnBundle = data.getExtras();
        int startPos = mReturnBundle.getInt(FlowerDetailActivity.KEY_STRAT_POS);
        int endPos = mReturnBundle.getInt(FlowerDetailActivity.KEY_END_POS);
        Log.e("lsc", "onActivityReenter: "+startPos+" enpos "+endPos);
        if (startPos != endPos) {
            mImgFragment.getRecyclerView().scrollToPosition(endPos);
        }
        supportPostponeEnterTransition();
        mImgFragment.getRecyclerView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImgFragment.getRecyclerView().getViewTreeObserver().removeOnPreDrawListener(this);
                supportStartPostponedEnterTransition();
                return true;
            }
        });

    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragments;
        String[] titles = new String[]{"图片", "文章"};

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        public List<Fragment> getFragments() {
            return mFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
