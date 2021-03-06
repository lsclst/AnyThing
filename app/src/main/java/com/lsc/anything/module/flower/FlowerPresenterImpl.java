package com.lsc.anything.module.flower;

import android.content.Context;

import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.database.DataBaseHelper;
import com.lsc.anything.database.SizeDao;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.entity.gank.GankResult;
import com.lsc.anything.widget.glide.Size;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public class FlowerPresenterImpl implements FlowerContract.FlowerPresenter {
    private static final String TAG = FlowerPresenterImpl.class.getSimpleName();
    private CompositeDisposable mCompositeDisposable;
    private FlowerContract.FlowerView mFlowerView;
    private Context mContext;
    private int mPage;
    private SizeDao mSizeDao;

    public FlowerPresenterImpl(Context c, FlowerContract.FlowerView flowerView) {
        mFlowerView = flowerView;
        mCompositeDisposable = new CompositeDisposable();
        mContext = c;
        mSizeDao = new SizeDao();
    }

    @Override
    public void release() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getFlowers(final int requestType) {
        if (requestType == ApiHolder.REQUEST_TYPE_REFRESH) {
            mFlowerView.showProgressView();
            mPage = 1;
        } else if (requestType == ApiHolder.REQUEST_TYPE_LOADMORE) {
            mPage += 1;
        }

        Disposable disposable = ApiHolder.getInstance().getGankService().getMeizi(mPage)
                .map(new Function<GankResult, List<GankItem>>() {
                    @Override
                    public List<GankItem> apply(GankResult gankResult) throws Exception {
                        if (!gankResult.isError()) {
                            List<GankItem> results = gankResult.getResults();
                            Size size;
                            for (GankItem item :
                                    results) {
                                size = mSizeDao.getSizeById(item.get_id());
                                if (size != null) {
                                    item.setSize(size);
                                }
                                boolean idExists = DataBaseHelper.getInstance(mContext.getApplicationContext())
                                        .getCollectionsDao().idExists(item.get_id());
                                item.setLike(idExists);

                            }
                            return results;
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        new Consumer<List<GankItem>>() {
                            @Override
                            public void accept(@NonNull List<GankItem> gankItems) throws Exception {
                                mFlowerView.showFlowers(requestType, gankItems);
                                mFlowerView.hideProgressView();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                mFlowerView.showErrorMessage("出错了" + throwable.getMessage());
                                mFlowerView.hideProgressView();
                            }
                        });

        mCompositeDisposable.add(disposable);
    }

}
