package com.lsc.anything.module.study;

import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.api.gank.GankService;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.entity.gank.GankResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lsc on 2017/9/15 0015.
 *
 * @author lsc
 */

public class StudyPresenterImpl implements StudyContract.StudyPresenter {
    private static final String TAG = StudyPresenterImpl.class.getSimpleName();
    private StudyContract.StudyView mStudyView;
    private CompositeDisposable mCompositeDisposable;
    private int mPage;
    private GankService mGankService;
    private String mType;

    public StudyPresenterImpl(String type, StudyContract.StudyView studyView) {
        mStudyView = studyView;
        mCompositeDisposable = new CompositeDisposable();
        mGankService = ApiHolder.getInstance().getGankService();
        mType = type;
    }

    @Override
    public void release() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getStudyGank(final int requestType) {

        if (requestType == ApiHolder.REQUEST_TYPE_REFRESH) {
            mStudyView.showProgressView();
            mPage = 1;
        } else if (requestType == ApiHolder.REQUEST_TYPE_LOADMORE) {
            mPage += 1;
        }
        Observable<GankResult> observable;
        if (mType.equals(StudyListFragment.TYPE_ANDROID)) {
            observable = mGankService.getAndroidGank(mPage);
        } else if (mType.equals(StudyListFragment.TYPE_IOS)) {
            observable = mGankService.getIosGank(mPage);
        } else {
            observable = mGankService.getWebGank(mPage);
        }

        Disposable disposable = observable.map(new Function<GankResult, List<GankItem>>() {

            @Override
            public List<GankItem> apply(@NonNull GankResult gankResult) throws Exception {
                if (!gankResult.isError()) {
                    List<GankItem> results = gankResult.getResults();
                    return results.size() > 0 ? results : null;
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<GankItem>>() {
                    @Override
                    public void accept(@NonNull List<GankItem> gankItems) throws Exception {
                        mStudyView.showStudysResult(requestType, gankItems);
                        mStudyView.hideProgressView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mStudyView.hideProgressView();
                        mStudyView.showErrorMessage("发生错误!! " + throwable.getMessage());
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
