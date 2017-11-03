package com.lsc.anything.module.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lsc.anything.R;
import com.lsc.anything.base.LazyLoadFragment;
import com.lsc.anything.module.about.AboutActivity;
import com.lsc.anything.module.collection.CollectionActivity;
import com.lsc.anything.utils.FileUtil;
import com.lsc.anything.widget.dialog.ClearCacheDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public class SettingFragment extends LazyLoadFragment {

    private Map<String, Long> mSizeMap = new HashMap<>();

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    public static final String TAG = SettingFragment.class.getSimpleName();
    @BindView(R.id.id_cacheSize)
    TextView mTvCacheSize;


    @OnClick(R.id.ll_about_me)
    public void onAboutMeClick(View v) {
        //todo me
        AboutActivity.start(getContext());
    }


    @OnClick(R.id.ll_clear_cache)
    public void onClearCacheClick(View v) {
        ClearCacheDialog dialog = ClearCacheDialog.newInstance(mSizeMap, new ClearCacheDialog.ClearCallBack() {
            @Override
            public void onClearCache() {
                initData();
            }
        });
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.ll_collection)
    public void onCollectionClick(View v) {
        startActivity(new Intent(getContext(), CollectionActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
    }

    @Override
    protected void initView() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void fetchData() {


    }


    @Override
    protected void initData() {
        FileUtil.getallCachaSize().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Map<String, Long>>() {

                    @Override
                    public void accept(Map<String, Long> map) throws Exception {
                        mSizeMap.putAll(map);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        long size = mSizeMap.get(FileUtil.KEY_HTTP) + mSizeMap.get(FileUtil.KEY_IMAGE) + mSizeMap.get(FileUtil.KEY_WEB);
                        mTvCacheSize.setText(FileUtil.formatSize(size));
                    }
                });

    }
}
