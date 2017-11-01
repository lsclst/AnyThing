package com.lsc.anything.module.search;

import android.text.TextUtils;
import android.util.Log;

import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.entity.gank.GankResult;
import com.lsc.anything.utils.ToastUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lsc on 2017/10/23 0023.
 *
 * @author lsc
 */

public class SearchPresenterImpl implements SearchContract.SearchPresenter {

    private CompositeDisposable mCompositeDisposable;
    private SearchContract.SearchView mSearchView;
    private int page;
    private String mcurKeyword;

    public SearchPresenterImpl(SearchContract.SearchView searchView) {
        mSearchView = searchView;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void release() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onLoadMore() {
        if (!TextUtils.isEmpty(mcurKeyword)) {
            page++;
            Log.e("lsc", "onLoadMore: ");
            getresult(ApiHolder.REQUEST_TYPE_LOADMORE, mcurKeyword);
        }
    }

    @Override
    public void search(String keyword) {
        mcurKeyword = keyword;
        page = 1;
        getresult(ApiHolder.REQUEST_TYPE_REFRESH, keyword);
    }

    private void getresult(final int requestType, String keyword) {
        mSearchView.showProgressView();
        Disposable disposable = ApiHolder.getInstance().getGankService().search(keyword, page)
                .delay(3, TimeUnit.SECONDS)
                .map(new Function<GankResult, List<GankItem>>() {
                    @Override
                    public List<GankItem> apply(@NonNull GankResult gankResult) throws Exception {
                        if (!gankResult.isError() && gankResult.getResults().size() > 0) {
                            return gankResult.getResults();
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GankItem>>() {
                    @Override
                    public void accept(@NonNull List<GankItem> gankItems) throws Exception {
                        mSearchView.showSearchResult(requestType, gankItems);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                        mSearchView.hideProgressView();
                        if (throwable instanceof NullPointerException) {
                            ToastUtil.showMsg("没有更多相关结果");
                        } else {
                            ToastUtil.showErrorMsg(throwable.getMessage());
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mSearchView.hideProgressView();
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        Log.e("lsc", "accept: " + disposable.isDisposed());
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
