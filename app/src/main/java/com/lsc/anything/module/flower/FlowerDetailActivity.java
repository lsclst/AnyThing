package com.lsc.anything.module.flower;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.lsc.anything.R;
import com.lsc.anything.base.ToolBarActivity;
import com.lsc.anything.database.CollectionDao;
import com.lsc.anything.entity.gank.GankItem;
import com.lsc.anything.module.collection.TransitionListenerAdapter;
import com.lsc.anything.utils.DownLoadUtil;
import com.lsc.anything.widget.HackViewPager;
import com.lsc.anything.widget.viewpagertransfomer.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class FlowerDetailActivity extends ToolBarActivity {
    private static final String KEY_DATA = "data";
    private static final String KEY_POS = "pos";
    private static final String KEY_ACTION = "action";
    public static final String KEY_SEN = "share_element_name";
    public static final String KEY_STRAT_POS = "startPos";
    public static final String KEY_END_POS = "endPos";

    private static final String TAG = FlowerDetailActivity.class.getSimpleName();
    @BindView(R.id.id_fab)
    ImageView mLikeButton;
    @BindView(R.id.id_viewpager)
    HackViewPager mViewPager;

    private List<GankItem> mGankItems;
    private DetailAdapter mAdapter;
    private CollectionDao mCollectionDao;
    private HashMap<Integer, Boolean> mChangeItems;
    private SmallBang mSmallBang;
    private boolean mIsneedEdit;
    private int startPos, endPos;

    private SharedElementCallback mSharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (startPos != endPos) {
                names.clear();
                sharedElements.clear();
                PhotoView photoView = (PhotoView) mAdapter.getCurPhotoView();
                String transitionName = ViewCompat.getTransitionName(photoView);
                names.add(transitionName);
                sharedElements.put(transitionName, photoView);
            }
        }
    };

    @Override
    protected void setUpToolBar(Toolbar toolBar) {
        toolBar.setTitle("Flower");
    }

    public static void startForResult(Fragment fragment, ArrayList<GankItem> datas, int pos, ImageView shareElement, boolean needEdit) {
        String transitionName = ViewCompat.getTransitionName(shareElement);
        Intent i = new Intent(fragment.getContext(), FlowerDetailActivity.class);
        i.putParcelableArrayListExtra(KEY_DATA, datas);
        i.putExtra(KEY_POS, pos);
        i.putExtra(KEY_ACTION, needEdit);
        i.putExtra(KEY_SEN, transitionName);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), shareElement, transitionName);
        fragment.startActivityForResult(i, Activity.RESULT_FIRST_USER, optionsCompat.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        setEnterSharedElementCallback(mSharedElementCallback);
    }

    @OnClick(R.id.id_fab)
    public void onFabClick(View v) {
        mSmallBang.bang(v, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                int pos = mViewPager.getCurrentItem();
                GankItem gankItem = mGankItems.get(pos);
                if (gankItem.isLike()) {
                    mCollectionDao.deleteCollectionById(FlowerDetailActivity.this, gankItem.get_id());
                    gankItem.setLike(false);
                    mGankItems.set(mViewPager.getCurrentItem(), gankItem);
                    mLikeButton.getDrawable().clearColorFilter();
                    mChangeItems.put(pos, false);
                } else {
                    gankItem.setSaveType(GankItem.TYPE_IMG);
                    mCollectionDao.save(FlowerDetailActivity.this, gankItem);
                    mChangeItems.put(pos, true);
                    gankItem.setLike(true);
                    mGankItems.set(mViewPager.getCurrentItem(), gankItem);
                    mLikeButton.getDrawable().setColorFilter(ContextCompat.getColor(FlowerDetailActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void onAnimationEnd() {
            }
        });
    }

    @Override
    protected boolean canGoBack() {
        return true;
    }

    @Override
    protected void initData() {
        mCollectionDao = new CollectionDao();
        mChangeItems = new HashMap<>();
        mGankItems = getIntent().getParcelableArrayListExtra(KEY_DATA);
        for (int i = 0; i < mGankItems.size(); i++) {
            GankItem item = mGankItems.get(i);
            GankItem collectionById = mCollectionDao.getCollectionById(this, item.get_id());
            if (collectionById != null) {
                item.setLike(true);
                mGankItems.set(i, item);
            }
        }
        startPos = getIntent().getIntExtra(KEY_POS, 0);
        if (startPos == 0 && mGankItems.get(0).isLike() && mLikeButton.getVisibility() == View.VISIBLE) {
            mLikeButton.getDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }
        mAdapter = new DetailAdapter(this, mGankItems);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(startPos, true);
        mAdapter.setOnItemClickListener(new DetailAdapter.onItemClickListener() {
            @Override
            public void onItemClick() {
                hideOrShowAppBar();
            }
        });
    }

    @Override
    protected void initView() {
        getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                hideOrShowAppBar();
            }


            @Override
            public void onTransitionStart(Transition transition) {
                super.onTransitionStart(transition);
                hideOrShowAppBar();
            }
        });
        mIsneedEdit = getIntent().getBooleanExtra(KEY_ACTION, false);
        mLikeButton.setVisibility(mIsneedEdit ? View.VISIBLE : View.GONE);
        mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                endPos = position;
                if (mIsneedEdit) {
                    GankItem item = mGankItems.get(position);
                    if (item.isLike()) {
                        mLikeButton.getDrawable().setColorFilter(ContextCompat.getColor(FlowerDetailActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        mLikeButton.getDrawable().clearColorFilter();
                    }
                }
            }
        });
        if (mIsneedEdit) {
            mSmallBang = SmallBang.attach2Window(FlowerDetailActivity.this);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_flower_detail;
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra(FlowerFragment.KEY_RESULT, mChangeItems);
        i.putExtra(KEY_STRAT_POS, startPos);
        i.putExtra(KEY_END_POS, endPos);
        i.putExtra(KEY_SEN, ViewCompat.getTransitionName(mAdapter.getCurPhotoView()));
        setResult(RESULT_OK, i);
        finishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flower_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GankItem gankItem = mGankItems.get(mViewPager.getCurrentItem());
        switch (item.getItemId()) {
            case R.id.id_menu_download:
                gankItem.setLocalPath(DownLoadUtil.IMAGE_FOLDER + gankItem.getFileName());
                mCollectionDao.save(this, gankItem);
                DownLoadUtil.getInstance().downloadPic(this, gankItem.getUrl(), gankItem.getFileName());
                break;
            case R.id.id_menu_set_wallpaper:
                DownLoadUtil.getInstance().setWallPaper(this, gankItem.getUrl(), gankItem.getFileName());
                break;
            case R.id.id_menu_share:
                DownLoadUtil.getInstance().sharePic(this, gankItem.getUrl(), gankItem.getFileName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class DetailAdapter extends PagerAdapter {

        List<GankItem> mdata;
        Context mContext;
        onItemClickListener mOnItemClickListener;
        View curPhotoView;

        interface onItemClickListener {
            void onItemClick();
        }

        public void setOnItemClickListener(onItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        DetailAdapter(Context context, List<GankItem> mdata) {
            this.mdata = mdata;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mdata.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GankItem item = mdata.get(position);
            PhotoView photoView = new PhotoView(mContext);
            ViewCompat.setTransitionName(photoView, mdata.get(position).get_id());
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick();
                    }
                }
            });
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Glide.with(mContext).load(item.getUrl()).asBitmap().fitCenter().into(new BitmapImageViewTarget(photoView) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    ActivityCompat.startPostponedEnterTransition((AppCompatActivity) mContext);
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            curPhotoView = (PhotoView) object;
        }

        public View getCurPhotoView() {
            return curPhotoView;
        }
    }
}
