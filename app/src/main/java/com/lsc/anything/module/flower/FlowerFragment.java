package com.lsc.anything.module.flower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.lsc.anything.R;
import com.lsc.anything.api.ApiHolder;
import com.lsc.anything.base.ListFragment;
import com.lsc.anything.database.SizeDao;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.widget.RadioImageView;
import com.lsc.anything.widget.footerview.Footer;
import com.lsc.anything.widget.glide.Size;
import com.lsc.anything.widget.recylerview.BaseViewHolder;
import com.lsc.anything.widget.recylerview.HeaderAndFooterAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lsc on 2017/9/21 0021.
 *
 * @author lsc
 */

public class FlowerFragment extends ListFragment<GankItem> implements FlowerContract.FlowerView {
    private static final String TAG = FlowerFragment.class.getSimpleName();
    public static final String KEY_RESULT = "changeids";
    private FlowerAdapter mFlowerAdapter;
    private FlowerContract.FlowerPresenter mFlowerPresenter;
    private boolean isGridLayout = true;

    public static FlowerFragment getInstance() {
        return new FlowerFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_FIRST_USER && resultCode == Activity.RESULT_OK) {
            List<GankItem> gankItems = mFlowerAdapter.getData();
            HashMap<Integer, Boolean> result = (HashMap) data.getSerializableExtra(KEY_RESULT);
            Set<Map.Entry<Integer, Boolean>> entries = result.entrySet();
            for (Map.Entry<Integer, Boolean> entry :
                    entries) {
                int pos = entry.getKey();
                boolean islike = entry.getValue();
                GankItem item = gankItems.get(pos);
                item.setLike(islike);
                mFlowerAdapter.getData().set(pos, item);
                mFlowerAdapter.notifyItemChanged(pos);
            }
        }
    }

    @Override
    public boolean prepareFetchData() {
        return super.prepareFetchData();
    }

    @Override
    public boolean prepareFetchData(boolean forceUpdate) {
        isVisibleToUser = true;
        return super.prepareFetchData(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onLoadMore() {
        mFlowerPresenter.getFlowers(ApiHolder.REQUEST_TYPE_LOADMORE);
    }

    @Override
    public void fetchData() {
        mFlowerPresenter.getFlowers(ApiHolder.REQUEST_TYPE_REFRESH);
    }

    @Override
    public void onRefresh() {
        mFlowerPresenter.getFlowers(ApiHolder.REQUEST_TYPE_REFRESH);

    }


    @Override
    protected void initData() {
        super.initData();
        mFlowerPresenter = new FlowerPresenterImpl(getContext(), this);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        //        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //                return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        return new GridLayoutManager(getContext(), 2);
    }

    public void changeListLayout() {
        if (isGridLayout) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        isGridLayout = !isGridLayout;
    }

    public boolean isGridLayout() {
        return isGridLayout;
    }

    @Override
    protected HeaderAndFooterAdapter<GankItem> getAdapter(List<GankItem> datas) {
        mFlowerAdapter = new FlowerAdapter(getContext(), datas);
        mFlowerAdapter.setFooterView(R.layout.footer_view, new Footer(Footer.Type.TYPE_LOADING));
        mFlowerAdapter.setOnItemClickListener(new HeaderAndFooterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, int position, Object item) {
                showDetail(mFlowerAdapter.getData(), position);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, Object item) {

            }
        });
        return mFlowerAdapter;
    }

    @Override
    public void showProgressView() {
        showProgress();
        mFlowerAdapter.updateFooter(new Footer(Footer.Type.TYPE_LOADING));
    }

    @Override
    public void hideProgressView() {
        hideProgress();
    }

    @Override
    public void showErrorMessage(String errorMsg) {
        showToast(errorMsg);
        mFlowerAdapter.updateFooter(new Footer(Footer.Type.TYPE_NO_MORE));
    }

    @Override
    public void showFlowers(int requestType, List<GankItem> flowers) {
        if (requestType == ApiHolder.REQUEST_TYPE_REFRESH) {

            mFlowerAdapter.setData(flowers);
        } else if (requestType == ApiHolder.REQUEST_TYPE_LOADMORE) {
            mFlowerAdapter.appendDatas(flowers);
        }
    }

    @Override
    public void showDetail(List<GankItem> gankItems, int position) {
        FlowerDetailActivity.startForResult(this, (ArrayList<GankItem>) gankItems, position, true);
    }

    private static class FlowerAdapter extends HeaderAndFooterAdapter<GankItem> {
        private SizeDao mSizeDao;

        FlowerAdapter(Context context, List<GankItem> data) {
            super(context, data);
            mSizeDao = new SizeDao();
        }

        public FlowerAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemViewId() {
            return R.layout.flower_item;
        }

        @Override
        protected void onDataViewBind(BaseViewHolder holder, final int position, boolean isPayLoad) {
            RadioImageView imgView = holder.getViewById(R.id.id_flower_img);
            final Size size = getData().get(position).getSize();
            if (size != null) {
                imgView.setOriginalSize(size.getImgWidth(), size.getImgHeight());
            }
            Glide.with(mContext).load(mData.get(position).getUrl())
                    .asBitmap().fitCenter().placeholder(R.drawable.ic_image_holder).into(new BitmapImageViewTarget(imgView) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int bitmapWidth = resource.getWidth();
                    int bitmapHeight = resource.getHeight();
                    if (size == null) {
                        mSizeDao.saveSize(new Size(mData.get(position).get_id(), bitmapWidth, bitmapHeight, mData.get(position).getUrl()));
                        ((RadioImageView) view).setOriginalSize(bitmapWidth, bitmapHeight);
                    }
                    view.setImageBitmap(resource);
                }
            });
            showItemAnimation(holder, position);
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
