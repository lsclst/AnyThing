package com.lsc.anything.module.study;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lsc.anything.R;
import com.lsc.anything.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by lsc on 2017/9/13 0013.
 *
 * @author lsc
 */

public class StudyFragment extends BaseFragment {

    @BindView(R.id.id_tabBar)
    TabLayout mTabLayout;
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_study;
    }

    @Override
    protected void initView() {
        String[] titles = getResources().getStringArray(R.array.gank_type);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new MainAdapter(getFragmentManager(), titles));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }

    public static StudyFragment getInstance() {
        return new StudyFragment();
    }

    private static class MainAdapter extends FragmentPagerAdapter {
        String[] mTitles;
        String[] contentType = new String[]{StudyListFragment.TYPE_ANDROID, StudyListFragment.TYPE_IOS, StudyListFragment.TYPE_WEB};

        public MainAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int i) {
            //            return StudyItemFragment.getInstance(contentType[i]);
            return StudyListFragment.getInstance(contentType[i]);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
