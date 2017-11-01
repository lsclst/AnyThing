package com.lsc.anything.module.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lsc.anything.App;
import com.lsc.anything.R;
import com.lsc.anything.WebViewActivity;
import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.utils.AvatarUtil;
import com.lsc.anything.utils.DateUtil;
import com.lsc.anything.utils.DensityUtil;
import com.lsc.anything.utils.KeyBoardUtil;
import com.lsc.anything.utils.SpfUtil;
import com.lsc.anything.utils.ToastUtil;
import com.lsc.anything.widget.SearchEditText;
import com.lsc.anything.widget.recylerview.BaseViewHolder;
import com.lsc.anything.widget.recylerview.HeaderAndFooterAdapter;
import com.lsc.anything.widget.recylerview.OnLoadMoreScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lsc on 2017/10/19 0019.
 *
 * @author lsc
 */

public class SearchActivity extends ToolBarActivity implements SearchContract.SearchView, OnLoadMoreScrollListener.onLoadMoreListener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    @BindView(R.id.id_search_history)
    ListView mHistoryView;
    @BindView(R.id.id_search_result)
    RecyclerView mResultView;
    @BindView(R.id.id_search_edt)
    SearchEditText mSearchEditText;
    @BindView(R.id.id_search_swf)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private HistoryAdapter mHistoryAdapter;
    private SearchResultAdapter mSearchResultAdapter;
    private View mFooterView;
    private boolean canLoadmore;
    private boolean keywordhasUse;

    private SearchContract.SearchPresenter mSearchPresenter;
    private List<String> mHistory;

    @Override
    protected void initData() {
        mHistory = SpfUtil.getInstance().getSearchHistories();
        if (mHistory == null) {
            mHistory = new ArrayList<>();
        }

        mHistoryAdapter = new HistoryAdapter(this, R.layout.search_history_item, mHistory);
        mHistoryAdapter.setNotifyOnChange(true);
        mHistoryView.setAdapter(mHistoryAdapter);
        if (mHistory.size() > 0) {
            showHistories();
        }


        mSearchResultAdapter = new SearchResultAdapter(this);
        mSearchResultAdapter.setOnItemClickListener(new HeaderAndFooterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, int position, Object item) {
                if (item instanceof GankItem) {
                    WebViewActivity.start(SearchActivity.this, ((GankItem) item).getUrl(), ((GankItem) item).getDesc());
                }
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, Object item) {

            }
        });
        mResultView.setAdapter(mSearchResultAdapter);
    }

    @Override
    protected void initView() {
        mSearchPresenter = new SearchPresenterImpl(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setProgressViewEndTarget(true, DensityUtil.getScreenHeight(this) / 2);
        mFooterView = LayoutInflater.from(this).inflate(R.layout.search_history_footer, null);
        mHistoryView.addFooterView(mFooterView);
        mHistoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KeyBoardUtil.closeKeyBoard(SearchActivity.this, mSearchEditText);
                if (position == parent.getCount() - 1) {
                    mHistoryAdapter.clear();
                    mSearchEditText.setText("");
                    mHistoryView.removeFooterView(mFooterView);
                } else {
                    keywordhasUse = true;
                    mHistoryView.post(new Runnable() {
                        @Override
                        public void run() {
                            mHistoryView.animate().translationYBy(-mHistoryView.getHeight()).setDuration(500);
                        }
                    });
                    mSearchEditText.setText((String) parent.getAdapter().getItem(position));
                    mSearchPresenter.search((String) parent.getAdapter().getItem(position));
                    sort((String) parent.getAdapter().getItem(position));
                }
            }
        });

        mSearchEditText.setOnClearClickListener(new SearchEditText.onClearClickListener() {
            @Override
            public void onClear() {
                if (keywordhasUse) {
                    mSearchResultAdapter.clearData();
                    mSearchPresenter.release();
                    mHistoryView.post(new Runnable() {
                        @Override
                        public void run() {
                            mHistoryView.animate().translationYBy(mHistoryView.getHeight()).setDuration(500).start();
                        }
                    });
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                showHistories();
                keywordhasUse = false;
            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.closeKeyBoard(App.APPContext, (EditText) v);
                    keywordhasUse = true;
                    if (!mHistory.contains(v.getText().toString())) {
                        mHistoryAdapter.insert(v.getText().toString(), 0);
                    } else {
                        sort(v.getText().toString());
                    }
                    mHistoryView.post(new Runnable() {
                        @Override
                        public void run() {
                            mHistoryView.animate().translationYBy(-mHistoryView.getHeight()).setDuration(500);
                        }
                    });

                    mSearchPresenter.search(v.getText().toString());
                    return true;
                }
                return false;
            }

        });

        mResultView.setLayoutManager(new LinearLayoutManager(this));
        mResultView.addOnScrollListener(new OnLoadMoreScrollListener(this));
    }

    private void sort(String s) {
        mHistoryAdapter.remove(s);
        mHistoryAdapter.insert(s, 0);
        mHistory.remove(s);
        mHistory.add(0, s);

    }

    @Override
    protected void onStop() {
        super.onStop();
        SpfUtil.getInstance().saveSearchHistories(mHistory);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.release();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected boolean canGoBack() {
        return true;
    }

    public static void start(Context c) {
        Intent i = new Intent(c, SearchActivity.class);
        c.startActivity(i);
    }

    //view start
    @Override
    public void showProgressView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideProgressView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        ToastUtil.showErrorMsg(errorMsg);
    }

    @Override
    public void showHistories() {
        if (mHistoryAdapter.getCount() <= 0) {
            mFooterView.setVisibility(View.GONE);
        }
        mResultView.setVisibility(View.GONE);
        mHistoryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchResult(int requestType, List<GankItem> searchResult) {
        if (mResultView.getVisibility() == View.GONE) {
            mResultView.setVisibility(View.VISIBLE);
        }
        if (requestType == ApiHolder.REQUEST_TYPE_REFRESH) {
            mSearchResultAdapter.clearData();
            mSearchResultAdapter.setData(searchResult);
        } else if (requestType == ApiHolder.REQUEST_TYPE_LOADMORE) {
            mSearchResultAdapter.appendDatas(searchResult);
        }
        canLoadmore = true;
    }

    @Override
    public void onLoadMore() {
        if (canLoadmore) {
            mSearchPresenter.onLoadMore();
            canLoadmore = false;
        }
    }

    private static class HistoryAdapter extends ArrayAdapter<String> {

        int resId;

        public HistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            resId = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resId, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(getItem(position));
            holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(getItem(position));
                }
            });
            return convertView;
        }

        static class ViewHolder {
            TextView textView;
            ImageView deleteImageView;

            ViewHolder(View itemView) {
                textView = (TextView) itemView.findViewById(R.id.id_history);
                deleteImageView = (ImageView) itemView.findViewById(R.id.id_delete);
            }
        }
    }

    private static class SearchResultAdapter extends HeaderAndFooterAdapter<GankItem> {

        public SearchResultAdapter(Context context) {
            super(context);
        }

        public SearchResultAdapter(Context context, List<GankItem> data) {
            super(context, data);
        }

        @Override
        protected int getItemViewId() {
            return R.layout.study_item;
        }

        @Override
        protected void onDataViewBind(BaseViewHolder holder, int position) {
            GankItem item = mData.get(position);
            ((TextView) holder.getViewById(R.id.id_study_title)).setText(item.getDesc());
            ((TextView) holder.getViewById(R.id.id_author)).setText(item.getWho());
            ((TextView) holder.getViewById(R.id.id_publish_at)).setText(DateUtil.getINSTANCE().formatDate(item.getPublishedAt()));
            ((ImageView) holder.getViewById(R.id.id_avatar)).setImageDrawable(AvatarUtil.getAvatar(holder.itemView.getContext()));
        }

    }
}
