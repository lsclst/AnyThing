package com.lsc.anything.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsc.anything.R;
import com.lsc.anything.utils.FileUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lsc on 2017/10/27 0027.
 *
 * @author lsc
 */

public class ClearCacheDialog extends DialogFragment {

    @BindView(R.id.id_cb_http_cache)
    AppCompatCheckBox mCheckBoxHttpCache;
    @BindView(R.id.id_http_cache_size)
    TextView mHttpCacheSize;
    @BindView(R.id.id_cb_image_cache)
    AppCompatCheckBox mCheckBoxImageCache;
    @BindView(R.id.id_image_cache_size)
    TextView mImageCacheSize;
    @BindView(R.id.id_cb_web_cache)
    AppCompatCheckBox mCheckBoxWebCache;
    @BindView(R.id.id_web_cache_size)
    TextView mWebCacheSize;
    private Unbinder mUnbinder;
    private ClearCallBack mClearCallBack;

    public interface ClearCallBack {
        void onClearCache();
    }

    public static ClearCacheDialog newInstance(Map<String, Long> map, ClearCallBack callBack) {
        ClearCacheDialog dialog = new ClearCacheDialog();
        dialog.setClearCallBack(callBack);
        Bundle b = new Bundle();
        b.putString(FileUtil.KEY_HTTP, FileUtil.formatSize(map.get(FileUtil.KEY_HTTP)));
        b.putString(FileUtil.KEY_IMAGE, FileUtil.formatSize(map.get(FileUtil.KEY_IMAGE)));
        b.putString(FileUtil.KEY_WEB, FileUtil.formatSize(map.get(FileUtil.KEY_WEB)));
        dialog.setArguments(b);
        return dialog;
    }

    public void setClearCallBack(ClearCallBack clearCallBack) {
        mClearCallBack = clearCallBack;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_clear_cache, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        getDialog().setTitle(R.string.select_cache);
        mHttpCacheSize.setText(getArguments().getString(FileUtil.KEY_HTTP));
        mImageCacheSize.setText(getArguments().getString(FileUtil.KEY_IMAGE));
        mWebCacheSize.setText(getArguments().getString(FileUtil.KEY_WEB));
        return view;
    }


    @OnClick(R.id.id_negative_btn)
    public void onNegativeButtonClick(View v) {
        dismiss();
    }

    @OnClick(R.id.id_positive_btn)
    public void onPositiveButtonClick(View v) {
        Observable<Boolean> task = null;
        if (mCheckBoxHttpCache.isChecked()) {

            task = FileUtil.deleteHttpCache();
        }
        if (mCheckBoxImageCache.isChecked()) {
            if (task != null) {
                task = task.mergeWith(FileUtil.deleteImageCache());
            } else {
                task = FileUtil.deleteImageCache();
            }
        }
        if (mCheckBoxWebCache.isChecked()) {
            if (task != null) {
                task = task.mergeWith(FileUtil.deleteWebCache());
            } else {
                task = FileUtil.deleteWebCache();
            }
        }

        if (task != null) {

            task.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            Log.e("lsc", "accept: " + aBoolean);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            dismiss();
                            if (mClearCallBack != null) {
                                mClearCallBack.onClearCache();
                            }
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            dismiss();
                            if (mClearCallBack != null) {
                                mClearCallBack.onClearCache();
                            }
                        }
                    });
        } else {
            dismiss();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
