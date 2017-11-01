package com.lsc.anything;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.base.BaseActivity;
import com.lsc.anything.entity.splsh.Splash;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lsc on 2017/10/11 0011.
 *
 * @author lsc
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.id_iv_splash)
    ImageView mSplashView;
    @BindView(R.id.id_splash_app)
    TextView mAppView;
    private Splash mSplash;
    private Disposable subscribe;

    @Override
    protected void initData() {
        subscribe = ApiHolder.getInstance().getSplashService().getSplash()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Splash>() {
                    @Override
                    public void accept(@NonNull Splash splash) throws Exception {
                        mSplash = splash;
                        Glide.with(SplashActivity.this).load(splash.getData().getUrl())
                                .centerCrop()
                                .into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        mSplashView.setImageDrawable(resource);
                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);
                                        mAppView.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 3000);
        mSplashView.setSystemUiVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }


    @OnClick(R.id.id_splash_dl)
    public void onDownloadClick(View v) {

    }

    @OnClick(R.id.id_splash_like)
    public void onLike(View v) {

    }

    @Override
    public void onBackPressed() {

    }
}
