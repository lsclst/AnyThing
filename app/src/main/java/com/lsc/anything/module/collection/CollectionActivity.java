package com.lsc.anything.module.collection;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.lsc.anything.R;
import com.lsc.anything.base.ToolBarActivity;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(CollectionFragment.getInstance(CollectionFragment.TYPE_IMAGE));
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
                if (state == ViewPager.SCROLL_STATE_IDLE){
                    for (Fragment f :
                            adapter.getFragments()) {
                        ((CollectionFragment)f).closeMultiChoice();
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
