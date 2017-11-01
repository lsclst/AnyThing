package com.lsc.anything.base;

import android.os.Bundle;

/**
 * Created by lsc on 2017/9/11 0011.
 *
 * @author lsc
 */

public abstract class LazyLoadFragment extends BaseFragment {
    private static final String TAG = LazyLoadFragment.class.getSimpleName();
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    //在initData之后执行
    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    /**
     * 加载数据的方法
     *
     * @param forceUpdate 是否强制更新
     * @return 是否执行更新
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

}
