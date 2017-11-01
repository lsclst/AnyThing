package com.lsc.anything.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsc.anything.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lsc on 2017/9/8 0008.
 *
 * @author lsc
 */

public abstract class BaseFragment extends Fragment {

    Unbinder mUnbinder;

    @LayoutRes
    protected abstract int getContentViewId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    protected void showToast(String msg) {
        ToastUtil.showMsg(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
