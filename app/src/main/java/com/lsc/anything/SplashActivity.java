package com.lsc.anything;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.base.BaseActivity;
import com.lsc.anything.database.CollectionDao;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.entity.splsh.Splash;
import com.lsc.anything.entity.splsh.SplashDetail;
import com.lsc.anything.utils.DownLoadUtil;
import com.lsc.anything.utils.FileUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

/**
 * Created by lsc on 2017/10/11 0011.
 *
 * @author lsc
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.id_iv_splash)
    ImageView mSplashView;
    @BindView(R.id.id_splash_dl)
    ImageView mBtnDownLoad;
    @BindView(R.id.id_splash_like)
    ImageView mBtnLike;
    private Disposable subscribe;
    private Handler mHandler;
    private SplashDetail.DataBean mDataBean;
    private SmallBang mSmallBang;
    private GankItem mGankItem;
    private CollectionDao mDao;

    @Override
    protected void initData() {
        mDao = new CollectionDao();
        subscribe = ApiHolder.getInstance().getSplashService().getSplash().flatMap(new Function<Splash, ObservableSource<SplashDetail>>() {
            @Override
            public ObservableSource<SplashDetail> apply(Splash splash) throws Exception {
                if (!TextUtils.isEmpty(splash.getRecommend())) {
                    return ApiHolder.getInstance().getSplashService().getSplashDetail(splash.getRecommend());
                }
                return null;
            }
        }).map(new Function<SplashDetail, SplashDetail.DataBean>() {
            @Override
            public SplashDetail.DataBean apply(SplashDetail splashDetail) throws Exception {
                List<SplashDetail.DataBean> data = splashDetail.getData();
                if (data != null && data.size() > 0) {
                    int randomInt = (int) (Math.random() * (data.size() - 1));
                    return data.get(randomInt);
                }
                return null;
            }
        })
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SplashDetail.DataBean>() {

                    @Override
                    public void accept(SplashDetail.DataBean dataBean) throws Exception {
                        mDataBean = dataBean;
                        Glide.with(SplashActivity.this).load(dataBean.big)
                                .centerCrop()
                                .into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        mSplashView.setImageDrawable(resource);
                                        mBtnDownLoad.setVisibility(View.VISIBLE);
                                        mBtnLike.setVisibility(View.VISIBLE);
                                        mBtnLike.animate().scaleX(1.0f).scaleY(1.0f).alpha(0.8f).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator())
                                                .start();
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                jumpToMainActivity();
                                            }
                                        }, 3000);
                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);
                                        jumpToMainActivity();
                                    }
                                });
                        mGankItem = mDao.getCollectionById(SplashActivity.this, dataBean.key);
                        if (mGankItem != null) {
                            mBtnLike.getDrawable().setColorFilter(ContextCompat.getColor(SplashActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        jumpToMainActivity();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.dispose();
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mHandler = new Handler();
        mSplashView.setSystemUiVisibility(View.GONE);
        mBtnLike.setAlpha(0f);
        mBtnLike.setScaleX(0f);
        mBtnLike.setScaleY(0f);
        mSmallBang = SmallBang.attach2Window(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }


    @OnClick(R.id.id_splash_dl)
    public void onDownloadClick(View v) {
        if (mDataBean != null) {
            String nameFromPath = FileUtil.getFileNameFromPath(mDataBean.big);
            DownLoadUtil.getInstance().downloadPic(this, mDataBean.big, nameFromPath);
        }
    }

    @OnClick(R.id.id_splash_like)
    public void onLike(View v) {
        if (mDataBean != null) {

            mSmallBang.bang(v, new SmallBangListener() {
                @Override
                public void onAnimationStart() {
                    if (mGankItem != null) {
                        mBtnLike.getDrawable().clearColorFilter();
                        int i = mDao.deleteCollectionById(SplashActivity.this, mGankItem.get_id());
                        Log.e("lsc", "delete: "+i );
                    } else {
                        mBtnLike.getDrawable().setColorFilter(ContextCompat.getColor(SplashActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                        String nameFromPath = FileUtil.getFileNameFromPath(mDataBean.big);
                        mGankItem = new GankItem();
                        mGankItem.setSaveType(GankItem.TYPE_IMG);
                        mGankItem.set_id(mDataBean.key);
                        mGankItem.setUrl(mDataBean.big);
                        mGankItem.setLocalPath(DownLoadUtil.IMAGE_FOLDER + nameFromPath);
                        mDao.save(SplashActivity.this, mGankItem);
                    }

                }

                @Override
                public void onAnimationEnd() {
                    jumpToMainActivity();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {

    }

    private void jumpToMainActivity() {
        mHandler.removeCallbacksAndMessages(null);
        finish();
        startActivity(new Intent(SplashActivity.this, MainActivity.class));

    }
}
