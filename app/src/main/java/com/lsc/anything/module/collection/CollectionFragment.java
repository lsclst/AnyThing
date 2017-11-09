package com.lsc.anything.module.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsc.anything.R;
import com.lsc.anything.WebViewActivity;
import com.lsc.anything.base.ListFragment;
import com.lsc.anything.database.CollectionDao;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.module.flower.FlowerDetailActivity;
import com.lsc.anything.utils.AvatarUtil;
import com.lsc.anything.utils.ToastUtil;
import com.lsc.anything.widget.recylerview.BaseViewHolder;
import com.lsc.anything.widget.recylerview.HeaderAndFooterAdapter;
import com.lsc.anything.widget.recylerview.MultiChoiceAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsc on 2017/10/31 0031.
 *
 * @author lsc
 */

public class CollectionFragment extends ListFragment<GankItem> {
    public static final String KEY_TYPE = "type";
    public static final int TYPE_IMAGE = 11;
    public static final int TYPE_ARTICLE = 22;

    private int mCurType;
    private MultiChoiceAdapter<GankItem> mAdapter;
    private CollectionDao mCollectionDao;

    public static CollectionFragment getInstance(int type) {
        CollectionFragment c = new CollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        c.setArguments(bundle);
        return c;
    }

    public void closeMultiChoice() {
        if (mAdapter != null) {
            mAdapter.finishedActionMode();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void initData() {
        super.initData();
        mCollectionDao = new CollectionDao();

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void fetchData() {
        mAdapter.setData(mCurType == TYPE_IMAGE ? mCollectionDao.getAllImage(getContext()) :
                mCollectionDao.getAllArticle(getContext()));
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        mCurType = getArguments().getInt(KEY_TYPE, TYPE_ARTICLE);
        return mCurType == TYPE_IMAGE ? new GridLayoutManager(getContext(), 2) : new LinearLayoutManager(getContext());
    }

    @Override
    protected HeaderAndFooterAdapter<GankItem> getAdapter(List<GankItem> datas) {
        MultiChoiceAdapter.OnMultiChoiceListener listener = new MultiChoiceAdapter.OnMultiChoiceListener() {
            @Override
            public boolean onMultiChoiceActionModeClick(ActionMode actionMode, int clickId) {
                switch (clickId) {
                    case R.id.id_menu_delete_all:
                        mAdapter.clearData();
                        mCollectionDao.deleteAll(getContext());
                        ToastUtil.showSuccessMsg(getString(R.string.delete_success));
                        break;
                    case R.id.id_menu_delete:
                        mCollectionDao.deleteCollections(getContext(), mAdapter.getSelectedItems());
                        mAdapter.deleteSelectedItems();
                        ToastUtil.showSuccessMsg(getString(R.string.delete_success));
                        break;
                    default:
                        actionMode.finish();
                        break;
                }
                return true;
            }

            @Override
            public void onMultiItemClick(ActionMode actionMode) {
                List<GankItem> selectedItems = mAdapter.getSelectedItems();
                actionMode.setTitle(String.format("已选择(%s)", selectedItems != null ? selectedItems.size() : 0));
            }

            @Override
            public void onMultiChoiceActionModeClose() {

                mAdapter.finishedActionMode();
            }
        };
        if (mCurType == TYPE_IMAGE) {
            mAdapter = new ImageAdapter(getContext(), R.menu.collection_menu, listener);
        } else {
            mAdapter = new ArticleAdapter(getContext(), R.menu.collection_menu, listener);
        }
        mAdapter.setOnItemClickListener(new HeaderAndFooterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, int position, Object item) {
                if (item instanceof GankItem) {
                    if (((GankItem) item).getSaveType() == GankItem.TYPE_ARTICLE) {
                        WebViewActivity.start(getContext(), (GankItem) item, false);
                    } else {
                        ArrayList<GankItem> items = (ArrayList<GankItem>) mAdapter.getData();
                        FlowerDetailActivity.startForResult(CollectionFragment.this, items, position, false);
                    }
                }
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, Object item) {

            }
        });
        return mAdapter;
    }

    @Override
    public void onRefresh() {

    }

    private static class ImageAdapter extends MultiChoiceAdapter<GankItem> {

        public ImageAdapter(Context context, List data, int menuid, OnMultiChoiceListener onMultiChoiceListener) {
            super(context, data, menuid, onMultiChoiceListener);
        }

        public ImageAdapter(Context context, int menuid, OnMultiChoiceListener onMultiChoiceListener) {
            super(context, menuid, onMultiChoiceListener);
        }

        @Override
        protected void onMultiChoiceViewBind(BaseViewHolder holder, int position, boolean isPayLoad, boolean isMultiChoiceOpen) {
            AppCompatCheckBox checkBox = holder.getViewById(R.id.id_checkbox);
            GankItem collection = getData().get(position);
            String localPath = collection.getLocalPath();
            File file = null;
            if (isMultiChoiceOpen) {
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(isItemCheck(position));
            } else {
                checkBox.setVisibility(View.GONE);
            }
            if (!isPayLoad) {
                checkBox.setChecked(isItemCheck(position));
            } else {
                ImageView targetView = holder.getViewById(R.id.id_flower_img);

                if (!TextUtils.isEmpty(localPath)) {
                    file = new File(localPath);
                }
                if (file != null && file.exists()) {
                    Glide.with(holder.itemView.getContext()).load(file).into(targetView);
                } else {
                    Glide.with(holder.itemView.getContext()).load(collection.getUrl()).into(targetView);
                }
            }

        }

        @Override
        protected int getItemViewId() {
            return R.layout.flower_item;
        }


    }

    private static class ArticleAdapter extends MultiChoiceAdapter<GankItem> {

        public ArticleAdapter(Context context, List data, int menuid, OnMultiChoiceListener onMultiChoiceListener) {
            super(context, data, menuid, onMultiChoiceListener);
        }

        public ArticleAdapter(Context context, int menuid, OnMultiChoiceListener onMultiChoiceListener) {
            super(context, menuid, onMultiChoiceListener);
        }

        @Override
        protected void onMultiChoiceViewBind(BaseViewHolder holder, int position, boolean isPayLoad, boolean isMultiChoiceOpen) {
            AppCompatCheckBox checkBox = holder.getViewById(R.id.id_checkbox);
            GankItem item = getData().get(position);
            if (isMultiChoiceOpen) {
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(isItemCheck(position));
            } else {
                checkBox.setVisibility(View.GONE);
            }
            if (!isPayLoad) {
                checkBox.setChecked(isItemCheck(position));
            } else {
                ((TextView) holder.getViewById(R.id.id_study_title)).setText(item.getDesc());
                ((TextView) holder.getViewById(R.id.id_author)).setText(item.getWho());
                ((TextView) holder.getViewById(R.id.id_publish_at)).setText(item.getPublishedAt());
                ((ImageView) holder.getViewById(R.id.id_avatar)).setImageDrawable(AvatarUtil.getAvatar(holder.itemView.getContext()));
            }
        }

        @Override
        protected int getItemViewId() {
            return R.layout.study_item;
        }


    }
}
