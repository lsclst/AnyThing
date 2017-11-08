package com.lsc.anything;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.database.CollectionDao;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.utils.ShareUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class WebViewActivity extends ToolBarActivity {
    public static final String KEY_DATA = "gank_data";
    private static final String KEY_NEED_EDIT = "needEdit";
    @BindView(R.id.id_webview)
    WebView mWebView;
    @BindView(R.id.error_view)
    RelativeLayout mErrorView;
    @BindView(R.id.id_web_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.id_btn_like)
    ImageView mBtnLike;
    private GankItem mGankItem;
    private boolean isSaved;
    private CollectionDao mDao;

    @Override
    protected void setUpToolBar(Toolbar toolBar) {
        toolBar.setTitle("");
        setSupportActionBar(toolBar);

    }

    @Override
    protected void initData() {
        Bundle bundleExtra = getIntent().getExtras();
        if (bundleExtra != null) {
            mGankItem = bundleExtra.getParcelable(KEY_DATA);
            mWebView.loadUrl(mGankItem.getUrl());
            mDao = new CollectionDao();
            GankItem collectionById = mDao.getCollectionById(this, mGankItem.get_id());
            if (isSaved = collectionById != null) {
                mBtnLike.getDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            } else {
                mBtnLike.getDrawable().clearColorFilter();
            }
        }
    }

    @Override
    protected void initView() {
        if (getIntent().getBooleanExtra(KEY_NEED_EDIT, false)) {
            mBtnLike.setVisibility(View.VISIBLE);
        } else {
            mBtnLike.setVisibility(View.GONE);
        }
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setUseWideViewPort(true);

        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mErrorView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mErrorView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
            }
        };

        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                getSupportActionBar().setTitle(title);
            }


        };
        mWebView.setWebChromeClient(chromeClient);
        mWebView.setWebViewClient(client);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.web_shared:
                if (mGankItem != null) {

                    ShareUtil.ShareNormalText(this, String.format("Title:%s \n %s", mGankItem.getDesc(), mGankItem.getUrl()));
                }
                break;
            case R.id.web_open_in_browser:
                if (mGankItem != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(mGankItem.getUrl()));
                    startActivity(i);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    public static void start(Context context, @NonNull GankItem item, boolean isNeedEdit) {
        Intent i = new Intent(context, WebViewActivity.class);
        Bundle b = new Bundle();
        b.putParcelable(KEY_DATA, item);
        i.putExtra(KEY_NEED_EDIT, isNeedEdit);
        i.putExtras(b);
        context.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            mWebView.setWebViewClient(null);
            finish();
        }
    }

    @Override
    protected boolean canGoBack() {
        return true;
    }

    @OnClick(R.id.id_btn_like)
    public void onLikeClick(View v) {
        if (isSaved) {
            mBtnLike.getDrawable().clearColorFilter();
            mDao.deleteCollectionById(this, mGankItem.get_id());
        } else {
            mBtnLike.getDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            mGankItem.setSaveType(GankItem.TYPE_ARTICLE);
            mDao.save(this, mGankItem);
        }
        isSaved = !isSaved;
    }
}
