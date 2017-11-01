package com.lsc.anything;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.utils.ShareUtil;

import butterknife.BindView;

public class WebViewActivity extends ToolBarActivity {
    private static final String KEY_URL = "url";
    public static final String KEY_DES = "des";
    @BindView(R.id.id_webview)
    WebView mWebView;
    @BindView(R.id.error_view)
    RelativeLayout mErrorView;
    @BindView(R.id.id_web_progress)
    ProgressBar mProgressBar;
    //    private GankItem mGankItem;
    private String mUrl, mDes;

    @Override
    protected void setUpToolBar(Toolbar toolBar) {
        toolBar.setTitle("");
        setSupportActionBar(toolBar);

    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra(KEY_URL);
        mDes = getIntent().getStringExtra(KEY_DES);
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void initView() {
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
                ShareUtil.ShareNormalText(this, String.format("Title:%s \n %s", mDes, mUrl));
                break;
            case R.id.web_open_in_browser:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mUrl));
                startActivity(i);
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

    public static void start(Context context, String url, String des) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra(KEY_URL, url);
        i.putExtra(KEY_DES, des);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected boolean canGoBack() {
        return true;
    }
}
