package com.lsc.anything.module.study;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsc.anything.R;
import com.lsc.anything.WebViewActivity;
import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.base.ListFragment;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.utils.AvatarUtil;
import com.lsc.anything.utils.DateUtil;
import com.lsc.anything.widget.footerview.Footer;
import com.lsc.anything.widget.recylerview.BaseViewHolder;
import com.lsc.anything.widget.recylerview.HeaderAndFooterAdapter;

import java.util.List;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public class StudyListFragment extends ListFragment<GankItem> implements StudyContract.StudyView, HeaderAndFooterAdapter.OnItemClickListener {
    public static final String KEY_TYPE = "type";
    public static final String TYPE_ANDROID = "Android";
    public static final String TYPE_IOS = "Ios";
    public static final String TYPE_WEB = "Web";


    private boolean canLoadMore;
    private StudyContract.StudyPresenter mStudyPresenter;
    private StudyAdapter mAdapter;

    public static StudyListFragment getInstance(String type) {
        Bundle data = new Bundle();
        data.putString(KEY_TYPE, type);
        StudyListFragment fragment = new StudyListFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        String type = getArguments().getString(KEY_TYPE);
        mStudyPresenter = new StudyPresenterImpl(type, this);
    }

    @Override
    public void showProgressView() {
        showProgress();
        mAdapter.updateFooter(new Footer(Footer.Type.TYPE_LOADING));
    }

    @Override
    public void hideProgressView() {
        hideProgress();
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        showToast(errorMsg);
        mAdapter.updateFooter(new Footer(Footer.Type.TYPE_NO_MORE));
    }


    @Override
    public void showDetailStudy(GankItem study) {
        WebViewActivity.start(getContext(), study, true);
    }


    @Override
    public void showStudysResult(int requestType, List<com.lsc.anything.entity.gank.GankItem> studys) {
        canLoadMore = true;
        if (requestType == ApiHolder.REQUEST_TYPE_REFRESH) {
            mAdapter.setData(studys);
        } else {
            mAdapter.appendDatas(studys);
        }
    }

    @Override
    public void onLoadMore() {
        if (canLoadMore) {
            mAdapter.updateFooter(new Footer(Footer.Type.TYPE_LOADING));
            mStudyPresenter.getStudyGank(ApiHolder.REQUEST_TYPE_LOADMORE);
        }

    }

    @Override
    public void fetchData() {
        mStudyPresenter.getStudyGank(ApiHolder.REQUEST_TYPE_REFRESH);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected HeaderAndFooterAdapter<GankItem> getAdapter(List<GankItem> datas) {
        mAdapter = new StudyAdapter(getContext(), datas);
        mAdapter.setFooterView(R.layout.footer_view, new Footer(Footer.Type.TYPE_LOADING));
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStudyPresenter.release();
    }

    @Override
    public void onRefresh() {
        mStudyPresenter.getStudyGank(ApiHolder.REQUEST_TYPE_REFRESH);
    }

    @Override
    public void onItemClick(BaseViewHolder holder, int position, Object item) {
        showDetailStudy((GankItem) item);
    }

    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, Object item) {

    }

    private static class StudyAdapter extends HeaderAndFooterAdapter<GankItem> {

        public StudyAdapter(Context context) {
            super(context);
        }

        public StudyAdapter(Context context, List<GankItem> data) {
            super(context, data);
        }

        @Override
        protected int getItemViewId() {
            return R.layout.study_item;
        }

        @Override
        protected void onDataViewBind(BaseViewHolder holder, int position, boolean isPayLoad) {
            GankItem item = mData.get(position);
            ((TextView) holder.getViewById(R.id.id_study_title)).setText(item.getDesc());
            ((TextView) holder.getViewById(R.id.id_author)).setText(item.getWho());
            ((TextView) holder.getViewById(R.id.id_publish_at)).setText(DateUtil.getINSTANCE().formatDate(item.getPublishedAt()));
            ((ImageView) holder.getViewById(R.id.id_avatar)).setImageDrawable(AvatarUtil.getAvatar(holder.itemView.getContext()));
        }

        @Override
        protected void onFooterViewBind(BaseViewHolder holder, Object footer, boolean isPayLoad) {
            if (holder.getAdapterPosition() == 0) {
                holder.itemView.setVisibility(View.GONE);
                return;
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
                Footer f = (Footer) footer;
                if (f.getType() == Footer.Type.TYPE_NO_MORE) {
                    holder.getViewById(R.id.id_footer_pg).setVisibility(View.GONE);
                    holder.getViewById(R.id.footer_end).setVisibility(View.VISIBLE);
                } else if (f.getType() == Footer.Type.TYPE_LOADING) {
                    holder.getViewById(R.id.id_footer_pg).setVisibility(View.VISIBLE);
                    holder.getViewById(R.id.footer_end).setVisibility(View.GONE);
                }
            }
        }
    }
}
